package edu.cuhk.csci3310.project;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectionListAdapter extends RecyclerView.Adapter<SelectionListAdapter.SelectionViewHolder> {

    // Views
    private LayoutInflater mInflater;
    private SelectionRequestFragment mParentFragment;

    // Variables
    private int layoutID;
    private ArrayList<String> mItemList;

    class SelectionViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        final SelectionListAdapter mAdapter;

        public SelectionViewHolder(@NonNull View itemView, @NonNull SelectionListAdapter adapter) {
            super(itemView);
            mTextView = (TextView) itemView;
            mAdapter = adapter;

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    removeItem(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public SelectionListAdapter(SelectionRequestFragment fragment, ArrayList<String> defaulSelectedtList, int rid) {
        this.mInflater = LayoutInflater.from(fragment.getContext());
        this.mParentFragment = fragment;
        this.mItemList = defaulSelectedtList;
        if (this.mItemList == null)
            this.mItemList = new ArrayList<>();
        this.layoutID = rid;
    }

    protected TextView createItemView(Context context){
        TextView itemView = new TextView(context);

        itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.rounded_rectangle, null));
        itemView.setGravity(Gravity.CENTER);
        // itemView.setPadding(4, 4, 4, 4);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(4, 4, 4, 4);
        itemView.setLayoutParams(params);

        return itemView;
    }

    public void addItem(String item){
        this.mItemList.add(item);
        this.notifyItemInserted(this.mItemList.size());
    }

    public void addItem(String item, int position){
        this.mItemList.add(position, item);
        this.notifyItemInserted(position);
    }

    public void addItem(ArrayList<String> items){
        // https://stackoverflow.com/questions/27845069/add-a-new-item-to-recyclerview-programmatically
        // https://guides.codepath.com/android/using-the-recyclerview
        // https://stackoverflow.com/questions/9030268/set-visibility-in-menu-programmatically-android
        this.mItemList.addAll(items);
        this.notifyItemRangeChanged(this.getItemCount(), items.size());
    }

    public void removeItem(int index){
        this.mItemList.remove(index);
        this.notifyItemRemoved(index);
    }

    public void removeItem(String item){
        int index = this.mItemList.indexOf(item);
        this.mItemList.remove(item);
        this.notifyItemRemoved(index);
    }

    public boolean hasItem(String item){
        return this.mItemList.contains(item);
    }

    public void clearItem(){
        // https://stackoverflow.com/questions/29978695/remove-all-items-from-recyclerview
        int size = this.mItemList.size();
        if (size <= 0) return;

        this.mItemList.clear();
        this.notifyItemRangeRemoved(0, size);
    }

    public ArrayList<String> getItemList(){
        return new ArrayList<String>(mItemList);
    }

    @NonNull
    @Override
    public SelectionListAdapter.SelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // View mItemView = mInflater.inflate(layoutID, parent, false);
        TextView mItemView = createItemView(parent.getContext());
        return new SelectionViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectionListAdapter.SelectionViewHolder holder, int position) {
        String mItemString = mItemList.get(position);
        holder.mTextView.setTag(position);
        holder.mTextView.setText(mItemString);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

}
