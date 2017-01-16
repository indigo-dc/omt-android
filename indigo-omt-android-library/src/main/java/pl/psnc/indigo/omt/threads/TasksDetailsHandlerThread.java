package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.TaskDetailsCallback;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksDetailsHandlerThread extends HandlerThread implements IndigoHandlerThread {
    private Handler mResponseHandler;
    private Handler mWorkerHandler;
    private IndigoCallback mCallback;
    private TasksAPI mTasksAPI;
    private Task mTask;

    public TasksDetailsHandlerThread(Task task, Handler workerHandler, Handler responseHandler,
        IndigoCallback callback) {
        super("TasksDetailsHandlerThread");
        this.mResponseHandler = responseHandler;
        this.mWorkerHandler = workerHandler;
        this.mCallback = callback;
        this.mTask = task;
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
        mWorkerHandler.obtainMessage().sendToTarget();
    }

    @Override public void makeRequest() {
        try {
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
        } catch (IllegalArgumentException e) {
            mCallback.onError(e);
        } finally {
            quit();
        }
    }
}
