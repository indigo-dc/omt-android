package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import java.util.Collections;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.TasksCallback;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

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
        mTaskAPI = new TasksAPI(HttpClientFactory.getClient(token));
        if (mApplication != null && mStatus != null) {
            final TasksWrapper taskWrapper = mTaskAPI.getTasks(mUsername, mStatus, mApplication);
            mResponseHandler.post(new Runnable() {
                @Override public void run() {
                    if (taskWrapper != null && taskWrapper.getTasks() != null) {
                        Collections.sort(taskWrapper.getTasks());
                        ((TasksCallback) mCallback).onSuccess(taskWrapper.getTasks());
                    }
                }
            });
        } else if (mStatus != null && mApplication == null) {
            final TasksWrapper taskWrapper = mTaskAPI.getTasks(mUsername, mStatus);
            mResponseHandler.post(new Runnable() {
                @Override public void run() {
                    if (taskWrapper != null && taskWrapper.getTasks() != null) {
                        Collections.sort(taskWrapper.getTasks());
                        ((TasksCallback) mCallback).onSuccess(taskWrapper.getTasks());
                    }
                }
            });
        } else {
            final TasksWrapper taskWrapper = mTaskAPI.getTasks(mUsername);
            mResponseHandler.post(new Runnable() {
                @Override public void run() {
                    if (taskWrapper != null && taskWrapper.getTasks() != null) {
                        Collections.sort(taskWrapper.getTasks());
                        ((TasksCallback) mCallback).onSuccess(taskWrapper.getTasks());
                    }
                }
            });
        }
        quit();
    }
}
