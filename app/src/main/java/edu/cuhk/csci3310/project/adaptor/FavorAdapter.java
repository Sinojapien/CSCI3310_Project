package edu.cuhk.csci3310.project.adaptor;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.cuhk.csci3310.project.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import edu.cuhk.csci3310.project.database.TaskType;
import edu.cuhk.csci3310.project.model.Favor;


/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class FavorAdapter extends FirestoreAdapter<FavorAdapter.ViewHolder> {

    public interface OnFavorSelectedListener {
        void onFavorSelected(DocumentSnapshot favor);
    }

    // adapter field
    private OnFavorSelectedListener mListener;

    public FavorAdapter(Query query, OnFavorSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_favor, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_favor_taskType;
        TextView item_favor_requesterName;
        TextView item_favor_status;

        public ViewHolder(View itemView) {
            super(itemView);
            item_favor_taskType = itemView.findViewById(R.id.item_favor_taskType);
            item_favor_requesterName = itemView.findViewById(R.id.item_favor_requesterName);
            item_favor_status = itemView.findViewById(R.id.item_favor_status);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnFavorSelectedListener listener) {

            // check what type of favor it is before casting to object
            snapshot.get("taskType");
            Favor favor = snapshot.toObject(Favor.class);
            Resources resources = itemView.getResources();

            item_favor_taskType.setText(favor.getTaskTypeString());
            item_favor_status.setText(favor.getStatusString());

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onFavorSelected(snapshot);
                    }
                }
            });
        }

    }
}
