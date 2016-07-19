package pl.psnc.indigo.omt.threads;

import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import okhttp3.Request;
import okhttp3.Response;
import pl.psnc.indigo.omt.api.GetTaskDetailsJob;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.TaskDetailsCallback;
import pl.psnc.indigo.omt.exceptions.IndigoException;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksDetailsHandlerThread extends HandlerThread implements IndigoHandlerThread {
    private Handler mResponseHandler;
    private Handler mWorkerHandler;
    private IndigoCallback mCallback;
    private GetTaskDetailsJob mApiJob;

    public TasksDetailsHandlerThread(GetTaskDetailsJob job, Handler responseHandler,
        IndigoCallback callback) {
        super("TasksDetailsHandlerThread");
        this.mResponseHandler = responseHandler;
        this.mCallback = callback;
        this.mApiJob = job;
    }

    public TasksDetailsHandlerThread(GetTaskDetailsJob job, Handler workerHandler,
        Handler responseHandler, IndigoCallback callback) {
        super("TasksDetailsHandlerThread");
        this.mResponseHandler = responseHandler;
        this.mWorkerHandler = workerHandler;
        this.mCallback = callback;
        this.mApiJob = job;
    }

    @Override public void prepare() {
        if (mWorkerHandler == null) {
            mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
                @Override public boolean handleMessage(Message msg) {
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
            Uri address = mApiJob.getFullUri(GetTaskDetailsJob.ENDPOINT,
                new String[] { String.valueOf(mApiJob.getTaskId()) }, null);
            Request request = new Request.Builder().url(address.getPath()).build();
            Response response = mApiJob.getClient().newCall(request).execute();

            Type taskType = new TypeToken<Task>() {
            }.getType();
            final Task task = new Gson().fromJson(response.body().string(), taskType);
            mResponseHandler.post(new Runnable() {
                @Override public void run() {
                    ((TaskDetailsCallback) mCallback).onSuccess(task);
                }
            });
        } catch (IndigoException e) {
            mCallback.onError(e);
        } catch (IOException e) {
            mCallback.onError(e);
        } catch (IllegalArgumentException e) {
            mCallback.onError(e);
        } finally {
            quit();
        }
    }
}
