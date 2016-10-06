package pl.psnc.indigo.omt.sampleapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.TokenResponse;
import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.TaskStatus;
import pl.psnc.indigo.omt.callbacks.TaskCreationCallback;
import pl.psnc.indigo.omt.callbacks.TasksCallback;
import pl.psnc.indigo.omt.iam.IAMHelper;

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
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(mOnFabClickListener);

        final AuthState authState = IAMHelper.readAuthState(getApplicationContext());
        Log.i(TAG, "access_token: " + authState.getAccessToken());
        Log.i(TAG, "refresh_token: " + authState.getRefreshToken());

        //for (authState.getLastAuthorizationResponse().additionalParameters.entrySet()
        //...
        if (getIntent().getExtras() != null) {
            AuthorizationResponse resp = AuthorizationResponse.fromIntent(getIntent());
            AuthorizationException ex = AuthorizationException.fromIntent(getIntent());
            if (resp != null) {
                // authorization succeeded
                authState.update(resp, ex);
                IAMHelper.writeAuthState(authState, getApplicationContext());
                final AuthorizationService service =
                    new AuthorizationService(getApplicationContext());
                service.performTokenRequest(resp.createTokenExchangeRequest(),
                    new AuthorizationService.TokenResponseCallback() {
                        @Override public void onTokenRequestCompleted(TokenResponse response,
                            AuthorizationException ex) {
                            if (response != null) {
                                // exchange succeeded
                                authState.update(response, ex);
                                IAMHelper.writeAuthState(authState, getApplicationContext());
                                if (authState.isAuthorized()) {
                                    if (mTasks == null) {
                                        authState.performActionWithFreshTokens(service,
                                            new AuthState.AuthStateAction() {
                                                @Override public void execute(@Nullable String s,
                                                    @Nullable String s1,
                                                    @Nullable AuthorizationException e) {
                                                    if (e == null) {
                                                        getTasks(null);
                                                    } else {
                                                        Log.e(TAG, e.getMessage());
                                                    }
                                                }
                                            });
                                    }
                                }
                                Log.i(TAG, response.jsonSerializeString());
                                Log.i(TAG, "access_token: " + response.accessToken);
                                Log.i(TAG, "refresh_token: " + response.refreshToken);
                            } else {
                                // authorization failed, check ex for more details
                                IAMHelper.writeForceReAuth(true, getApplicationContext());
                                ex.printStackTrace();
                            }
                        }
                    });
            } else {
                // authorization failed, check ex for more details
                if (ex != null) ex.printStackTrace();
                IAMHelper.writeForceReAuth(true, getApplicationContext());
            }
        }

        if (authState.isAuthorized()) {
            if (savedInstanceState == null || mTasks == null) {
                getTasks(null);
            }
        }
    }

    /**
     * OnClickListener to create dummy tasks
     */
    private View.OnClickListener mOnFabClickListener = new View.OnClickListener() {
        @Override public void onClick(View view) {
            final AuthState authState = IAMHelper.readAuthState(getApplicationContext());
            final AuthorizationService service = new AuthorizationService(getApplicationContext());
            authState.performActionWithFreshTokens(service, new AuthState.AuthStateAction() {
                @Override public void execute(@Nullable String s, @Nullable String s1,
                    @Nullable AuthorizationException e) {
                    IAMHelper.writeAuthState(authState, getApplicationContext());
                    Indigo.createTask(new Task(UUID.randomUUID().toString(), "2"),
                        new TaskCreationCallback() {
                            @Override public void onSuccess(Task result) {
                                Log.d(TAG, "Created task: " + result.toString());
                                getTasks(new Comparator<Task>() {
                                    @Override public int compare(Task task, Task t1) {
                                        Integer lCi = Integer.parseInt(task.getId());
                                        Integer rCi = Integer.parseInt(t1.getId());
                                        return lCi.compareTo(rCi);
                                    }
                                });
                            }

                            @Override public void onError(Exception exception) {
                                Log.e(TAG, "Creation task failed: " + exception.getMessage());
                            }
                        });
                }
            });
        }
    };

    /**
     * The method collects the tasks from the server and optionally sorting with given comparator
     *
     * @param comparator a comparator to compare tasks for sorting purpose
     */
    public void getTasks(final Comparator<Task> comparator) {
        Indigo.getTasks(TaskStatus.ANY, new TasksCallback() {
            @Override public void onSuccess(List<Task> tasks) {
                mTasks = (ArrayList) tasks;
                if (comparator != null) Collections.sort(tasks, comparator);
                mListAdapter.setTasks(mTasks);
                mListAdapter.notifyDataSetChanged();
            }

            @Override public void onError(Exception e) {
                Log.e(TAG,
                    (e.getMessage() != null) ? e.getMessage() : "Something wrong happend here!");
            }
        });
    }

    /**
     * on click listener handles showing details about the chosen task
     * the details are collected from the already download list of task
     */

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            TaskViewHolder viewHolder = (TaskViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Task t = mTasks.get(position);
            Intent intentWithTask = new Intent(v.getContext(), TaskDetailsActivity.class);
            intentWithTask.putExtra("task", t);
            startActivity(intentWithTask);
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
