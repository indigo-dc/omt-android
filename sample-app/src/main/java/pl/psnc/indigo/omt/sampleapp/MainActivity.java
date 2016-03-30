package pl.psnc.indigo.omt.sampleapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.api.TasksApi;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.TaskStatus;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private TasksListAdapter listAdapter;
    private List<Task> mTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.listview_tasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new TasksListAdapter(this);
        mRecyclerView.setAdapter(listAdapter);
        if (savedInstanceState == null || mTasks == null) {
            Indigo.getTasks("brunor", TaskStatus.ANY, new TasksApi.TasksCallback() {
                @Override
                public void onSuccess(List<Task> tasks) {
                    mTasks = (ArrayList) tasks;
                    listAdapter.setTasks(mTasks);
                    listAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            });
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Indigo.createTask(new Task("michalu", "2"), new TasksApi.TaskCreationCallback() {
                    @Override
                    public void onSuccess(Task result) {
                        Log.d(TAG, "Created task: " + result.toString());
                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e(TAG, "Creation task failed: " + exception.getMessage());
                    }
                });
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TaskViewHolder viewHolder = (TaskViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Task t = mTasks.get(position);
            Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

        }
    };


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
                    Log.e(TAG, exception.getMessage());
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

    public class TasksListAdapter extends RecyclerView.Adapter<TaskViewHolder> {
        private Context context;
        private List<Task> tasks;

        public TasksListAdapter(Context context) {
            this.context = context;
        }

        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
        }

        public TasksListAdapter(Activity activity) {
            context = activity;

        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, null);
            TaskViewHolder viewHolder = new TaskViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            Task t = tasks.get(position);
            holder.id.setText(t.getId());
            holder.description.setText(t.getDescription());
            holder.date.setText(t.getDate());
            holder.user.setText(t.getUser());
            holder.all.setOnClickListener(clickListener);
            holder.all.setTag(holder);
        }

        @Override
        public long getItemId(int position) {
            int id = Integer.parseInt(tasks.get(position).getId());
            return id;
        }

        @Override
        public int getItemCount() {
            if (tasks != null)
                return tasks.size();
            return 0;
        }
    }

}
