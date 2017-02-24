package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.TaskCreationCallback;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksCreateHandlerThread extends ApiHandlerThread implements ApiCallWorkflow {
    public static final String TAG = "TasksCreateHandlerThread";
    private TasksAPI mTasksAPI;
    private Task mTaskToCreate;

    public TasksCreateHandlerThread(Task task, Handler workerHandler, Handler responseHandler,
        AuthState authState, IndigoCallback callback) {
        super(TAG, responseHandler, workerHandler, authState, callback);
        this.mTaskToCreate = task;
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
        final Task task = mTasksAPI.createTask(mTaskToCreate);
        if (mResponseHandler.get() != null) {
            mResponseHandler.get().post(new Runnable() {
                @Override public void run() {
                    if (task == null) {
                        if (mCallback.get() != null) {
                            mCallback.get().onError(new IndigoException("Task == null"));
                        }
                        quitSafely();
                    } else {
                        if (mCallback.get() != null) {
                            ((TaskCreationCallback) mCallback.get()).onSuccess(task);
                        }
                        quitSafely();
                    }
                }
            });
        }
    }
}
