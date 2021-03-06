package pl.psnc.indigo.omt.sampleapp.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.TokenResponse;
import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.api.model.InputFile;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.TaskStatus;
import pl.psnc.indigo.omt.callbacks.TaskCreationCallback;
import pl.psnc.indigo.omt.callbacks.TasksCallback;
import pl.psnc.indigo.omt.iam.IAMHelper;
import pl.psnc.indigo.omt.sampleapp.R;
import pl.psnc.indigo.omt.sampleapp.callbacks.OnTaskListListener;
import pl.psnc.indigo.omt.utils.Log;

public class TasksActivity extends IndigoActivity implements OnTaskListListener {
    private static final String TAG = "TasksActivity";
    private static final int REFRESH_INTERVAL = 5000;
    private int counter = 0;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.tasks_list) RecyclerView mRecyclerView;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    private TasksListAdapter mListAdapter;
    private List<Task> mTasks;
    private final Handler mHandler = new Handler();

    @Override protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new TasksListAdapter(this);
        mRecyclerView.setAdapter(mListAdapter);
        mFab.setOnClickListener(mOnFabClickListener);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        AuthState authState = IAMHelper.readAuthState(getApplicationContext());
        if (authState.isAuthorized()) {
            loadTasks(savedInstanceState, authState);
        }

        if (getIntent().getExtras() != null) {
            AuthorizationResponse resp = AuthorizationResponse.fromIntent(getIntent());
            AuthorizationException ex = AuthorizationException.fromIntent(getIntent());
            AuthState authStateFromIntent = IAMHelper.readAuthState(getApplicationContext());
            authStateFromIntent.update(resp, ex);
            IAMHelper.writeAuthState(authStateFromIntent, getApplicationContext());
            if (resp != null) {
                getIntent().getExtras().clear();
                // authorization succeeded
                AuthorizationService service = new AuthorizationService(getApplicationContext());
                service.performTokenRequest(resp.createTokenExchangeRequest(),
                    new AuthorizationService.TokenResponseCallback() {
                        @Override public void onTokenRequestCompleted(TokenResponse response,
                            AuthorizationException ex) {
                            if (response != null) {
                                // exchange succeeded
                                AuthState authStateAfter =
                                    IAMHelper.readAuthState(getApplicationContext());
                                authStateAfter.update(response, ex);
                                IAMHelper.writeAuthState(authStateAfter, getApplicationContext());
                                Log.i(TAG, "access_token: " + authStateAfter.getAccessToken());
                                Log.i(TAG, "refresh_token: " + authStateAfter.getRefreshToken());
                                loadTasks(savedInstanceState, authStateAfter);
                            } else {
                                // authorization failed, check ex for more details
                                if (ex != null) ex.printStackTrace();
                            }
                        }
                    });
            }
        }
    }

    private void loadTasks(Bundle savedInstanceState, AuthState authState) {
        if (authState != null && authState.isAuthorized()) {
            if (savedInstanceState == null || mTasks == null) {
                ;
                AuthorizationService service = new AuthorizationService(getApplicationContext());
                authState.performActionWithFreshTokens(service, new AuthState.AuthStateAction() {
                    @Override public void execute(@Nullable String s, @Nullable String s1,
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
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            });
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

                    Indigo.createTask(createDummyTask(), authState, new TaskCreationCallback() {
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
        final AuthState authState = IAMHelper.readAuthState(getApplicationContext());
        Indigo.getTasks(TaskStatus.ANY, authState, new TasksCallback() {
            @Override public void onSuccess(List<Task> tasks) {
                Log.i(TAG, "Refresh token = " + authState.getRefreshToken());
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

    private static final class PeriodicRunnable implements Runnable {
        private List<Task> mList;
        private WeakReference<TasksListAdapter> mAdapter = null;
        private WeakReference<Handler> mHandler = null;

        private PeriodicRunnable(List<Task> tasks, TasksListAdapter adapter, Handler handler) {
            mList = tasks;
            mAdapter = new WeakReference<TasksListAdapter>(adapter);
            mHandler = new WeakReference<Handler>(handler);
            Log.i(TAG, "PeriodicRunnable created");
        }

        @Override public void run() {
            final AuthState authState = IAMHelper.readAuthState(Indigo.getApplicationContext());
            Indigo.getTasks(TaskStatus.ANY, authState, new TasksCallback() {
                @Override public void onSuccess(List<Task> tasks) {
                    Log.i(TAG, "Refresh token = " + authState.getRefreshToken());
                    Log.i(TAG, "Got tasks from handlerthread");
                    try {
                        if (mList != null) mList = tasks;
                        if (mAdapter.get() != null) mAdapter.get().setTasks(tasks);
                        if (mAdapter.get() != null) mAdapter.get().notifyDataSetChanged();
                        //schedule refreshing task
                        if (!tasks.isEmpty()) {
                            if (mHandler.get() != null) {
                                mHandler.get()
                                    .postDelayed(
                                        new PeriodicRunnable(mList, mAdapter.get(), mHandler.get()),
                                        REFRESH_INTERVAL);
                                Log.i(TAG, "Posting next runnable...");
                                Log.i(TAG,
                                    "--------------------------------------------------------------------------------------------------------");
                            }
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override public void onError(Exception e) {
                    Log.e(TAG, (e.getMessage() != null) ? e.getMessage()
                        : "Something wrong happend here!");
                }
            });
        }
    }

    ;

    private Runnable mTaskUpdate = new Runnable() {
        @Override public void run() {
            AuthState authState = IAMHelper.readAuthState(getApplicationContext());
            Indigo.getTasks(TaskStatus.ANY, authState, new TasksCallback() {
                @Override public void onSuccess(List<Task> tasks) {
                    mTasks = (ArrayList) tasks;
                    mListAdapter.setTasks(mTasks);
                    mListAdapter.notifyDataSetChanged();
                }

                @Override public void onError(Exception e) {
                    Log.e(TAG, (e.getMessage() != null) ? e.getMessage()
                        : "Something wrong happend here!");
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

    @Override public void onUpdate(List<Task> list) {
        if (list != null) {
            mTasks = list;
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
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
            holder.id.setText(t.getId());
            holder.description.setText(t.getDescription());
            holder.itemDate.setText(t.getDate());
            holder.status.setText(t.getStatus());
            holder.all.setOnClickListener(mClickListener);
            holder.all.setTag(holder);
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

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onResume() {
        super.onResume();
        mHandler.postDelayed(new PeriodicRunnable(mTasks, mListAdapter, mHandler), 500);
    }

    @Override public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG, "onMemoryLow()");
    }

    @Override public void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    private Task createDummyTask() {
        File file1 = new File(getExternalFilesDir(null) + File.separator + "sayhello.sh");
        File file2 = new File(getExternalFilesDir(null) + File.separator + "sayhello.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Task task = new Task();
        task.setDescription("TasksAPI v2 - creating task");
        task.setApplication("2");
        task.setUser("test");

        InputFile if1 = new InputFile();
        if1.setFile(file1);
        if1.setName(file1.getName());

        InputFile if2 = new InputFile();
        if2.setFile(file2);
        if2.setName(file2.getName());
        ArrayList<InputFile> files = new ArrayList<>();
        files.add(if1);
        files.add(if2);
        task.setInputFiles(files);
        return task;
    }
}
