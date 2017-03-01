package pl.psnc.indigo.omt.sampleapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import pl.psnc.indigo.omt.BuildConfig;
import pl.psnc.indigo.omt.api.model.InputFile;
import pl.psnc.indigo.omt.api.model.OutputFile;
import pl.psnc.indigo.omt.sampleapp.R;

/**
 * Created by michalu on 15.09.16.
 */
public class FragmentWithListAndCaption extends Fragment {
    private TextView mCaptionText;
    private RecyclerView mList;
    private String mCaption;
    private List<? extends Object> mFiles;
    private FileListAdapter mFileListAdapter;

    public static <T> FragmentWithListAndCaption create(String caption, List<T> files) {
        FragmentWithListAndCaption instance = new FragmentWithListAndCaption();
        instance.mCaption = caption;
        instance.mFiles = files;
        return instance;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_with_list_and_caption, container, false);
        mCaptionText = (TextView) v.findViewById(R.id.fragment_caption);
        mList = (RecyclerView) v.findViewById(R.id.fragment_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(linearLayoutManager);
        mCaptionText.setText(mCaption);
        mFileListAdapter = new FileListAdapter(mFiles);
        mList.setAdapter(mFileListAdapter);
        mFileListAdapter.notifyDataSetChanged();
        return v;
    }

    public class FileListAdapter extends RecyclerView.Adapter<FilesHolder> {
        private List<? extends Object> mFiles;

        public FileListAdapter(List<? extends Object> files) {
            this.mFiles = files;
        }

        @Override public FilesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.filelist_item, null);
            FilesHolder viewHolder = new FilesHolder(view);
            return viewHolder;
        }

        @Override public void onBindViewHolder(FilesHolder holder, int position) {
            if (mFiles.get(position) instanceof InputFile) {
                InputFile i = (InputFile) mFiles.get(position);
                holder.mFileName.setText(i.getName());
                holder.mFileName.setTextColor(Color.BLACK);
                holder.mFileName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else {
                OutputFile i = (OutputFile) mFiles.get(position);
                holder.mFileName.setText(i.getName());
                if (i.getUrl() != null) {
                    holder.mFileName.setTag(holder);
                    holder.mFileName.setOnClickListener(mOnClickListener);
                    holder.mFileName.setTextColor(Color.BLUE);
                    holder.mFileName.setCompoundDrawablesWithIntrinsicBounds(
                        android.R.drawable.arrow_down_float, 0, 0, 0);
                }
            }
        }

        @Override public long getItemId(int position) {
            int id = mFiles.get(position).hashCode();
            return id;
        }

        @Override public int getItemCount() {
            if (mFiles != null) return mFiles.size();
            return 0;
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            FilesHolder fh = (FilesHolder) v.getTag();
            int position = fh.getAdapterPosition();
            if (mFiles.get(position) instanceof OutputFile) {
                OutputFile o = (OutputFile) mFiles.get(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(BuildConfig.FGAPI_ADDRESS + "/v1.0/" + o.getUrl().toString()));
                startActivity(browserIntent);
            }
        }
    };
}
