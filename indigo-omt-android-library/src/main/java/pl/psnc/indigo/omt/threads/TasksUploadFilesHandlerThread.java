package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.UploadFileCallback;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksUploadFilesHandlerThread extends ApiHandlerThread implements ApiCallWorkflow {
    public static final String TAG = "TasksUploadFilesHandlerThread";
    private TasksAPI mTasksAPI;
    private Task mCreatedTask;

    public TasksUploadFilesHandlerThread(Task task, Handler workerHandler, Handler responseHandler,
        AuthState authState, IndigoCallback callback) {
        super(TAG, responseHandler, workerHandler, authState, callback);
        this.mCreatedTask = task;
    }

    @Override public void networkWork(String accessToken) {
        try {
            mTasksAPI = new TasksAPI(HttpClientFactory.getClient(accessToken));
        } catch (final IndigoException e1) {
            mResponseHandler.get().post(new Runnable() {
                @Override public void run() {
                    if (mCallback.get() != null) mCallback.get().onError(e1);
                }
            });
            quitSafely();
        }
        mTasksAPI.uploadInputFile(mCreatedTask.getUploadUrl(), mCreatedTask);
        if (mResponseHandler.get() != null) {
            mResponseHandler.get().post(new Runnable() {
                @Override public void run() {
                    if (mCallback.get() != null) {
                        ((UploadFileCallback) mCallback.get()).onSuccess();
                    }
                    quitSafely();
                }
            });
        }
    }
}
