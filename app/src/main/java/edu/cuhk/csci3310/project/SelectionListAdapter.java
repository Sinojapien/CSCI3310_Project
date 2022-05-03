package edu.cuhk.csci3310.project;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SelectionListAdapter extends RecyclerView.Adapter<SelectionListAdapter.SelectionViewHolder> {

    private int layoutResId;
    public ArrayList<String> mSelectionList;
    private ArrayList<String> mItemList;
    private LayoutInflater mInflater;

    class SelectionViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        final SelectionListAdapter mAdapter;

        public SelectionViewHolder(@NonNull View itemView, @NonNull SelectionListAdapter adapter) {
            super(itemView);
            mTextView = (TextView) itemView;
            mAdapter = adapter;
        }
    }

    public SelectionListAdapter(Context context, ArrayList<String> list, int rid) {
        mInflater = LayoutInflater.from(context);
        this.mSelectionList = list;
        this.mItemList = new ArrayList<>();
        layoutResId = rid;
    }

    public TextView createItemView(Context context){
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

    public void clearItem(){
        // https://stackoverflow.com/questions/29978695/remove-all-items-from-recyclerview
        int size = this.mItemList.size();
        if (size <= 0) return;

        this.mItemList.clear();
        this.notifyItemRangeRemoved(0, size); // this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectionListAdapter.SelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // View mItemView = mInflater.inflate(layoutResId, parent, false);
        TextView mItemView = createItemView(parent.getContext());
        return new SelectionViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectionListAdapter.SelectionViewHolder holder, int position) {
         String mItemString = mItemList.get(position);
        // Uri uri = Uri.parse(mImagePath);
        // holder.imageItemView.setImageURI(uri);
         holder.mTextView.setText(mItemString);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public ArrayList<String> getItemList(){
        return (ArrayList<String>) mItemList.clone();
    }

}
