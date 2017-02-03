package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import java.util.Collections;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.TasksCallback;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;
import pl.psnc.indigo.omt.utils.Log;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksHandlerThread extends ApiHandlerThread implements ApiCallWorkflow {
    private static final String TAG = "TasksHandlerThread";
    private TasksAPI mTaskAPI;
    private String mApplication;
    private String mStatus;
    private String mUsername;

    public TasksHandlerThread(String username, String status, String application,
        Handler workerHandler, Handler responseHandler, AuthState authState,
        IndigoCallback callback) {
        super(TAG, responseHandler, workerHandler, authState, callback);
        this.mStatus = status;
        this.mApplication = application;
        this.mUsername = username;
    }

    @Override public void networkWork(String token) {
        Log.i(TAG, "networkWork() started");
        try {
            mTaskAPI = new TasksAPI(HttpClientFactory.getClient(token));
        } catch (IndigoException e) {
            if (mCallback.get() != null) mCallback.get().onError(e);
            quitSafely();
        }

        try {
            if (mApplication != null && mStatus != null) {
                final TasksWrapper taskWrapper =
                    mTaskAPI.getTasks(mUsername, mStatus, mApplication);
                if (mResponseHandler.get() != null) {
                    mResponseHandler.get().post(new Runnable() {
                        @Override public void run() {
                            if (taskWrapper != null && taskWrapper.getTasks() != null) {
                                Log.i(TAG, "Got non-null taskWrapper object");
                                Collections.sort(taskWrapper.getTasks());
                                if (mCallback.get() != null) {
                                    Log.i(TAG, "Callback is not null");
                                    ((TasksCallback) mCallback.get()).onSuccess(
                                        taskWrapper.getTasks());
                                }
                            }
                            Log.i(TAG, "quitSafely()");
                            quitSafely();
                        }
                    });
                }
            } else if (mStatus != null && mApplication == null) {
                final TasksWrapper taskWrapper = mTaskAPI.getTasks(mUsername, mStatus);
                if (mResponseHandler.get() != null) {
                    mResponseHandler.get().post(new Runnable() {
                        @Override public void run() {
                            if (taskWrapper != null && taskWrapper.getTasks() != null) {
                                Log.i(TAG, "Got non-null taskWrapper object");
                                Collections.sort(taskWrapper.getTasks());
                                if (mCallback.get() != null) {
                                    Log.i(TAG, "Callback is not null");
                                    ((TasksCallback) mCallback.get()).onSuccess(
                                        taskWrapper.getTasks());
                                }
                            }
                            Log.i(TAG, "quitSafely()");
                            quitSafely();
                        }
                    });
                }
            } else {
                final TasksWrapper taskWrapper = mTaskAPI.getTasks(mUsername);
                if (mResponseHandler.get() != null) {
                    mResponseHandler.get().post(new Runnable() {
                        @Override public void run() {
                            if (taskWrapper != null && taskWrapper.getTasks() != null) {
                                Log.i(TAG, "Got non-null taskWrapper object");
                                Collections.sort(taskWrapper.getTasks());
                                if (mCallback.get() != null) {
                                    Log.i(TAG, "Callback is not null");
                                    ((TasksCallback) mCallback.get()).onSuccess(
                                        taskWrapper.getTasks());
                                }
                            }
                            Log.i(TAG, "quitSafely()");
                            quitSafely();
                        }
                    });
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.i(TAG, "quitSafely() after exception");
            quitSafely();
        }
    }
}
