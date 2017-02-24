package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.TaskDeleteCallback;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksDeleteHandlerThread extends ApiHandlerThread implements ApiCallWorkflow {
    public static final String TAG = "TasksDeleteHandlerThread";
    private TasksAPI mTasksAPI;
    private Task mTaskToDelete;

    public TasksDeleteHandlerThread(Task task, Handler workerHandler, Handler responseHandler,
        AuthState authState, IndigoCallback callback) {
        super(TAG, responseHandler, workerHandler, authState, callback);
        this.mTaskToDelete = task;
    }

    @Override public void networkWork(String accessToken) {
        try {
            mTasksAPI = new TasksAPI(HttpClientFactory.getClient(accessToken));
        }  catch (final IndigoException e1) {
            mResponseHandler.get().post(new Runnable() {
                @Override public void run() {
                    if (mCallback.get() != null) mCallback.get().onError(e1);
                }
            });
            quitSafely();
        }
        final boolean deleted = mTasksAPI.deleteTask(mTaskToDelete);
        if (mResponseHandler.get() != null) {
            mResponseHandler.get().post(new Runnable() {
                @Override public void run() {
                    if (mCallback.get() != null) {
                        ((TaskDeleteCallback) mCallback.get()).onSuccess(deleted);
                    }
                    quitSafely();
                }
            });
        }
    }
}
