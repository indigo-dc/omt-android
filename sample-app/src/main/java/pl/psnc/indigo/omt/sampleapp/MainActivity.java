package pl.psnc.indigo.omt.sampleapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.api.TasksApi;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.TaskStatus;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView mListView;
    private TasksListAdapter listAdapter;
    private List<Task> mTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview_tasks);
        listAdapter = new TasksListAdapter(this);
        mListView.setAdapter(listAdapter);
        if (savedInstanceState == null || mTasks == null)
            Indigo.getTasks("brunor", TaskStatus.ANY, new TasksApi.TasksCallback() {
                @Override
                public void onSuccess(List<Task> tasks) {
                    mTasks = (ArrayList) tasks;
                    listAdapter.setTasks(mTasks);
                    mListView.setOnItemClickListener(onItemClickListener);
                    listAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            });
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Task t = mTasks.get(position);
            Indigo.getTask(Integer.parseInt(t.getId()), new TasksApi.TaskDetailsCallback() {
                @Override
                public void onSuccess(Task result) {
                    Toast.makeText(getApplicationContext(), result.getDescription(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception exception) {

                }
            });
        }
    };

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTasks = savedInstanceState.getParcelableArrayList("tasks");
            listAdapter.setTasks(mTasks);
            listAdapter.notifyDataSetChanged();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mTasks != null && mTasks.size() > 0)
            outState.putParcelableArrayList("tasks", (ArrayList) mTasks);
        super.onSaveInstanceState(outState);
    }

    public class TasksListAdapter extends BaseAdapter {
        private Context context;
        private List<Task> tasks;

        class ViewHolder {
            public TextView id;
            public TextView description;
            public TextView user;
            public TextView date;
        }

        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
        }

        public TasksListAdapter(Activity activity) {
            context = activity;

        }

        @Override
        public int getCount() {
            if (tasks != null)
                return tasks.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return tasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            int id = Integer.parseInt(tasks.get(position).getId());
            return id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                rowView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.id = (TextView) rowView.findViewById(R.id.item_id);
                viewHolder.description = (TextView) rowView.findViewById(R.id.item_description);
                viewHolder.date = (TextView) rowView.findViewById(R.id.item_date);
                viewHolder.user = (TextView) rowView.findViewById(R.id.item_user);
                rowView.setTag(viewHolder);
            }
            Task t = tasks.get(position);
            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.id.setText(t.getId());
            holder.description.setText(t.getDescription());
            holder.date.setText(t.getDate());
            holder.user.setText(t.getUser());

            return rowView;
        }
    }

}
