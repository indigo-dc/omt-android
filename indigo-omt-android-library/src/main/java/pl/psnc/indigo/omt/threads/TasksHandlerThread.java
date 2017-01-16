package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.google.gson.JsonSyntaxException;
import java.util.Collections;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.TasksCallback;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksHandlerThread extends HandlerThread implements IndigoHandlerThread {
    private static final String TAG = "TasksHandlerThread";
    private Handler mResponseHandler;
    private Handler mWorkerHandler;
    private IndigoCallback mCallback;
    private TasksAPI mTaskAPI;
    private String mApplication;
    private String mStatus;
    private String mUsername;

    public TasksHandlerThread(String username, String status, String application,
        Handler workerHandler, Handler responseHandler, IndigoCallback callback) {
        super("TasksHandlerThread");
        this.mResponseHandler = responseHandler;
        this.mWorkerHandler = workerHandler;
        this.mCallback = callback;
        this.mStatus = status;
        this.mApplication = application;
        this.mUsername = username;
    }

    @Override public void prepare() {
        if (mWorkerHandler == null) {
            mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
                @Override public boolean handleMessage(Message msg) {
                    mTaskAPI = new TasksAPI(HttpClientFactory.getNonIAMClient());
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
            if (mApplication != null && mStatus != null) {
                final TasksWrapper taskWrapper =
                    mTaskAPI.getTasks(mUsername, mStatus, mApplication);
                mResponseHandler.post(new Runnable() {
                    @Override public void run() {
                        if (taskWrapper.getTasks() != null) {
                            Collections.sort(taskWrapper.getTasks());
                            ((TasksCallback) mCallback).onSuccess(taskWrapper.getTasks());
                        }
                    }
                });
            } else if (mStatus != null && mApplication == null) {
                final TasksWrapper taskWrapper = mTaskAPI.getTasks(mUsername, mStatus);
                mResponseHandler.post(new Runnable() {
                    @Override public void run() {
                        if (taskWrapper.getTasks() != null) {
                            Collections.sort(taskWrapper.getTasks());
                            ((TasksCallback) mCallback).onSuccess(taskWrapper.getTasks());
                        }
                    }
                });
            } else {
                final TasksWrapper taskWrapper = mTaskAPI.getTasks(mUsername);
                mResponseHandler.post(new Runnable() {
                    @Override public void run() {
                        if (taskWrapper.getTasks() != null) {
                            Collections.sort(taskWrapper.getTasks());
                            ((TasksCallback) mCallback).onSuccess(taskWrapper.getTasks());
                        }
                    }
                });
            }
        } catch (IllegalArgumentException e) {
            mCallback.onError(e);
        } catch (JsonSyntaxException e) {
            mCallback.onError(e);
        } finally {
            quit();
        }
    }
}
