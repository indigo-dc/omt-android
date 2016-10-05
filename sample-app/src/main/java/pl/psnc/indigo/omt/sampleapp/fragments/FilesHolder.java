package pl.psnc.indigo.omt.sampleapp.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import pl.psnc.indigo.omt.sampleapp.R;

/**
 * Created by michalu on 05.10.16.
 */

public class FilesHolder extends RecyclerView.ViewHolder {
    protected TextView mFileName;
    public FilesHolder(View itemView) {
        super(itemView);
        mFileName = (TextView) itemView.findViewById(R.id.file_list_item);
    }
}
