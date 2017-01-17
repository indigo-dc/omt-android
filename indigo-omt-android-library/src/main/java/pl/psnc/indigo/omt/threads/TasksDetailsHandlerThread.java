package pl.psnc.indigo.omt.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;
import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.TaskDetailsCallback;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksDetailsHandlerThread extends ApiHandlerThread implements ApiCallWorkflow {
    public static final String TAG = "TasksDetailsHandlerThread";
    private Handler mResponseHandler;
    private Handler mWorkerHandler;
    private IndigoCallback mCallback;
    private TasksAPI mTasksAPI;
    private Task mTask;
    private AuthState mAuthState;

    public TasksDetailsHandlerThread(Task task, Handler workerHandler, Handler responseHandler,
        AuthState authState, IndigoCallback callback) {
        super(TAG, responseHandler, workerHandler, authState, callback);
        this.mResponseHandler = responseHandler;
        this.mWorkerHandler = workerHandler;
        this.mCallback = callback;
        this.mAuthState = authState;
        this.mTask = task;
    }

    @Override public void prepare() {
        if (mWorkerHandler == null) {
            mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
                @Override public boolean handleMessage(Message msg) {
                    authenticate(mAuthState);
                    return true;
                }
            });
        }
    }

    @Override public synchronized void start() {
        super.start();
        prepare();
        mWorkerHandler.obtainMessage().sendToTarget();
    }

    @Override public void authenticate(AuthState authState) {
        Context ctx = Indigo.getApplicationContext();
        AuthorizationService authService = new AuthorizationService(ctx);
        authState.performActionWithFreshTokens(authService, new AuthState.AuthStateAction() {
            @Override public void execute(@Nullable String s, @Nullable String s1,
                @Nullable AuthorizationException e) {
                mTasksAPI = new TasksAPI(HttpClientFactory.getClient(s));

                final Task taskWithDetails =
                    mTasksAPI.getTaskDetails(Integer.parseInt(mTask.getId()));
                mResponseHandler.post(new Runnable() {
                    @Override public void run() {
                        if (taskWithDetails == null) {
                            mCallback.onError(new IndigoException("No details downloaded"));
                        } else {
                            ((TaskDetailsCallback) mCallback).onSuccess(taskWithDetails);
                        }
                    }
                });
                quit();
            }
        });
    }
}
