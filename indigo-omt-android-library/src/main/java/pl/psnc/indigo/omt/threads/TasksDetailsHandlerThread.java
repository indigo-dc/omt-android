package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import net.openid.appauth.AuthState;
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
    private TasksAPI mTasksAPI;
    private Task mTask;

    public TasksDetailsHandlerThread(Task task, Handler workerHandler, Handler responseHandler,
        AuthState authState, IndigoCallback callback) {
        super(TAG, responseHandler, workerHandler, authState, callback);
        this.mTask = task;
    }

    @Override public void networkWork(String accessToken) {
        try {
            mTasksAPI = new TasksAPI(HttpClientFactory.getClient(accessToken));
        } catch (IndigoException e1) {
            mCallback.onError(e1);
        }

        final Task taskWithDetails = mTasksAPI.getTaskDetails(Integer.parseInt(mTask.getId()));
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
}
