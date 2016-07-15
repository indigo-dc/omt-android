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
import java.util.UUID;
import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.api.TasksApi;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.TaskStatus;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private TasksListAdapter mListAdapter;
    private List<Task> mTasks;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.listview_tasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new TasksListAdapter(this);
        mRecyclerView.setAdapter(mListAdapter);
        Indigo.getTasks2(new TasksApi.TasksCallback() {
            @Override public void onSuccess(List<Task> result) {
                Log.d(TAG, result.toString());
            }

            @Override public void onError(Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
        });
        if (savedInstanceState == null || mTasks == null) {
            getTasks();
        }
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Indigo.createTask(new Task("michalu", UUID.randomUUID().toString(), "2"),
                    new TasksApi.TaskCreationCallback() {
                        @Override public void onSuccess(Task result) {
                            Log.d(TAG, "Created task: " + result.toString());
                            getTasks();
                        }

                        @Override public void onError(Exception exception) {
                            Log.e(TAG, "Creation task failed: " + exception.getMessage());
                        }
                    });
            }
        });
    }

    public void getTasks() {
        Indigo.getTasks("michalu", TaskStatus.ANY, new TasksApi.TasksCallback() {
            @Override public void onSuccess(List<Task> tasks) {
                mTasks = (ArrayList) tasks;
                mListAdapter.setTasks(mTasks);
                mListAdapter.notifyDataSetChanged();
            }

            @Override public void onError(Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            TaskViewHolder viewHolder = (TaskViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Task t = mTasks.get(position);
            Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Task t = mTasks.get(position);
            Indigo.getTask(Integer.parseInt(t.getId()), new TasksApi.TaskDetailsCallback() {
                @Override public void onSuccess(Task result) {
                    Toast.makeText(getApplicationContext(), result.getDescription(),
                        Toast.LENGTH_SHORT).show();
                }

                @Override public void onError(Exception exception) {
                    Log.e(TAG, exception.getMessage());
                }
            });
        }
    };

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTasks = savedInstanceState.getParcelableArrayList("tasks");
            mListAdapter.setTasks(mTasks);
            mListAdapter.notifyDataSetChanged();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        if (mTasks != null && mTasks.size() > 0) {
            outState.putParcelableArrayList("tasks", (ArrayList) mTasks);
        }
        super.onSaveInstanceState(outState);
    }

    public class TasksListAdapter extends RecyclerView.Adapter<TaskViewHolder> {
        private Context mContext;
        private List<Task> mTasks;

        public TasksListAdapter(Context context) {
            this.mContext = context;
        }

        public void setTasks(List<Task> tasks) {
            this.mTasks = tasks;
        }

        public TasksListAdapter(Activity activity) {
            mContext = activity;
        }

        @Override public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, null);
            TaskViewHolder viewHolder = new TaskViewHolder(view);
            return viewHolder;
        }

        @Override public void onBindViewHolder(TaskViewHolder holder, int position) {
            Task t = mTasks.get(position);
            holder.mId.setText(t.getId());
            holder.mDescription.setText(t.getDescription());
            holder.mItemDate.setText(t.getDate());
            holder.mUser.setText(t.getUser());
            holder.mAll.setOnClickListener(mClickListener);
            holder.mAll.setTag(holder);
        }

        @Override public long getItemId(int position) {
            int id = Integer.parseInt(mTasks.get(position).getId());
            return id;
        }

        @Override public int getItemCount() {
            if (mTasks != null) return mTasks.size();
            return 0;
        }
    }
}
