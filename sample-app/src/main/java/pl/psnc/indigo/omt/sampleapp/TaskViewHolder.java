package pl.psnc.indigo.omt.sampleapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by michalu on 30.03.16.
 */
public class TaskViewHolder extends RecyclerView.ViewHolder {
    protected RelativeLayout all;
    protected TextView id;
    protected TextView description;
    protected TextView user;
    protected TextView date;

    public TaskViewHolder(View view) {
        super(view);
        this.all = (RelativeLayout) view.findViewById(R.id.listitem_rl);
        this.id = (TextView) view.findViewById(R.id.item_id);
        this.description = (TextView) view.findViewById(R.id.item_description);
        this.date = (TextView) view.findViewById(R.id.item_date);
        this.user = (TextView) view.findViewById(R.id.item_user);
    }
}