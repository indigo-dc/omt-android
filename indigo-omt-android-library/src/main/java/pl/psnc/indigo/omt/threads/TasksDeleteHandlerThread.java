package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.TaskDeleteCallback;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksDeleteHandlerThread extends HandlerThread implements IndigoHandlerThread {
    private Handler mResponseHandler;
    private Handler mWorkerHandler;
    private IndigoCallback mCallback;
    private TasksAPI mTasksAPI;
    private Task mTaskToCreate;

    public TasksDeleteHandlerThread(Task task, Handler responseHandler, IndigoCallback callback) {
        super("TasksCreateHandlerThread");
        this.mResponseHandler = responseHandler;
        this.mCallback = callback;
        this.mTaskToCreate = task;
    }

    public TasksDeleteHandlerThread(Task task, Handler workerHandler, Handler responseHandler,
        IndigoCallback callback) {
        super("TasksCreateHandlerThread");
        this.mResponseHandler = responseHandler;
        this.mWorkerHandler = workerHandler;
        this.mCallback = callback;
        this.mTaskToCreate = task;
    }

    @Override public void prepare() {
        if (mWorkerHandler == null) {
            mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
                @Override public boolean handleMessage(Message msg) {
                    mTasksAPI = new TasksAPI(HttpClientFactory.getNonIAMClient());
                    makeRequest();
                    return true;
                }
            });
        }
    }

    @Override public synchronized void start() {
        super.start();
        prepare();
        Message m = mWorkerHandler.obtainMessage();
        m.sendToTarget();
    }

    @Override public void makeRequest() {
        try {
            final boolean deleted = mTasksAPI.deleteTask(mTaskToCreate);
            mResponseHandler.post(new Runnable() {
                @Override public void run() {
                    ((TaskDeleteCallback) mCallback).onSuccess(deleted);
                }
            });
        } catch (IllegalArgumentException e) {
            mCallback.onError(e);
        } finally {
            quit();
        }
    }
}
