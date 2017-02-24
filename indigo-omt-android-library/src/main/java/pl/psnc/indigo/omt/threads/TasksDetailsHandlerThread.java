package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.TaskDetailsCallback;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;
import pl.psnc.indigo.omt.utils.Log;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksDetailsHandlerThread extends ApiHandlerThread implements ApiCallWorkflow {
    public static final String TAG = "TasksDetailsHT";
    private TasksAPI mTasksAPI;
    private Task mTask;

    public TasksDetailsHandlerThread(Task task, Handler workerHandler, Handler responseHandler,
        AuthState authState, IndigoCallback callback) {
        super(TAG, responseHandler, workerHandler, authState, callback);
        this.mTask = task;
    }

    @Override public void networkWork(String accessToken) {
        Log.i(TAG, "networkWork() started");
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

        final Task taskWithDetails = mTasksAPI.getTaskDetails(Integer.parseInt(mTask.getId()));
        if (mResponseHandler.get() != null) {
            mResponseHandler.get().post(new Runnable() {
                @Override public void run() {
                    if (taskWithDetails == null) {
                        if (mCallback.get() != null) {
                            mCallback.get().onError(new IndigoException("No details downloaded"));
                            quitSafely();
                        }
                    } else {
                        if (mCallback.get() != null) {
                            ((TaskDetailsCallback) mCallback.get()).onSuccess(taskWithDetails);
                            quitSafely();
                        }
                    }
                }
            });
        }
    }
}
