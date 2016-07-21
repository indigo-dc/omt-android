package pl.psnc.indigo.omt.threads;

import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import okhttp3.Request;
import okhttp3.Response;
import pl.psnc.indigo.omt.api.GetTasksJob;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.callbacks.TasksCallback;
import pl.psnc.indigo.omt.exceptions.IndigoException;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksHandlerThread extends HandlerThread implements IndigoHandlerThread {
    private Handler mResponseHandler;
    private Handler mWorkerHandler;
    private IndigoCallback mCallback;
    private GetTasksJob mApiJob;

    public TasksHandlerThread(GetTasksJob job, Handler responseHandler, IndigoCallback callback) {
        super("TasksHandlerThread");
        this.mResponseHandler = responseHandler;
        this.mCallback = callback;
        this.mApiJob = job;
    }

    public TasksHandlerThread(GetTasksJob job, Handler workerHandler, Handler responseHandler,
            IndigoCallback callback) {
        super("TasksHandlerThread");
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
        Message m = mWorkerHandler.obtainMessage();
        m.sendToTarget();
    }

    @Override public void makeRequest() {
        try {
            Uri address = mApiJob.getFullUri(GetTasksJob.ENDPOINT);
            Request request = new Request.Builder().url(address.getPath()).build();
            Response response = mApiJob.getClient().newCall(request).execute();

            Type listOfTasks = new TypeToken<List<Task>>() {
            }.getType();
            final List<Task> tasks = new Gson().fromJson(response.body().string(), listOfTasks);
            mResponseHandler.post(new Runnable() {
                @Override public void run() {
                    Collections.sort(tasks);
                    ((TasksCallback) mCallback).onSuccess(tasks);
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
