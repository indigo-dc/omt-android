package pl.psnc.indigo.omt.sampleapp.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import pl.psnc.indigo.omt.sampleapp.R;

/**
 * Created by michalu on 30.03.16.
 */
public class TaskViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout all;
    public TextView id;
    public TextView description;
    public Button status;
    public TextView itemDate;

    public TaskViewHolder(View view) {
        super(view);
        this.all = (RelativeLayout) view.findViewById(R.id.listitem_rl);
        this.id = (TextView) view.findViewById(R.id.item_id);
        this.description = (TextView) view.findViewById(R.id.item_description);
        this.itemDate = (TextView) view.findViewById(R.id.item_date);
        this.status = (Button) view.findViewById(R.id.item_status);
    }
}