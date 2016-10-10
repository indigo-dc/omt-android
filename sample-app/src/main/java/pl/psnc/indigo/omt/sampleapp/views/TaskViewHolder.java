package pl.psnc.indigo.omt.sampleapp.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import pl.psnc.indigo.omt.sampleapp.R;

/**
 * Created by michalu on 30.03.16.
 */
public class TaskViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout mAll;
    public TextView mId;
    public TextView mDescription;
    public TextView mUser;
    public TextView mItemDate;

    public TaskViewHolder(View view) {
        super(view);
        this.mAll = (RelativeLayout) view.findViewById(R.id.listitem_rl);
        this.mId = (TextView) view.findViewById(R.id.item_id);
        this.mDescription = (TextView) view.findViewById(R.id.item_description);
        this.mItemDate = (TextView) view.findViewById(R.id.item_date);
        this.mUser = (TextView) view.findViewById(R.id.item_user);
    }
}