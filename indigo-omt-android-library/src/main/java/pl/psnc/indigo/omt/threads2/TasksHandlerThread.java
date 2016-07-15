package pl.psnc.indigo.omt.threads2;

import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;
import pl.psnc.indigo.omt.api.TasksApi;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api2.RootAPI2;
import pl.psnc.indigo.omt.api2.TasksAPI2;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.threads.IndigoCallback;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksHandlerThread extends HandlerThread {
    private Handler mResponseHandler;
    private Handler mWorkerHandler;
    private IndigoCallback mCallback;
    private TasksAPI2 mTaskApi;

    public TasksHandlerThread(Handler mResponseHandler, TasksAPI2 taskApi) {
        super("TasksHandlerThread");
        this.mResponseHandler = mResponseHandler;
        this.mCallback = taskApi.getCallback();
        this.mTaskApi = taskApi;
    }

    public TasksHandlerThread(Handler workerHandler, Handler mResponseHandler, TasksAPI2 taskApi) {
        super("TasksHandlerThread");
        this.mResponseHandler = mResponseHandler;
        this.mWorkerHandler = workerHandler;
        this.mCallback = taskApi.getCallback();
        this.mTaskApi = taskApi;
    }


    public void prepareHandler() {
        if (mWorkerHandler != null)
            mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    callApi(mTaskApi);
                    return true;
                }
            });
    }

    @Override
    public synchronized void start() {
        super.start();
        prepareHandler();
        Message m = mWorkerHandler.obtainMessage();
        m.sendToTarget();
    }

    public void callApi(TasksAPI2 api) {
        try {
            Uri address = api.getFullAddress(RootAPI2.DEFAULT_ADDRESS, TasksAPI2.ENDPOINT);
            Request request = new Request.Builder().url(address.toString()).build();
            Response response = api.getClient().newCall(request).execute();

            Type listOfTasks = new TypeToken<List<Task>>() {
            }.getType();
            final List<Task> tasks = new Gson().fromJson(response.body().string(), listOfTasks);
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    ((TasksApi.TasksCallback) mCallback).onSuccess(tasks);
                }
            });

        } catch (IndigoException e) {
            mCallback.onError(e);
        } catch (IOException e) {
            mCallback.onError(e);
        } finally {
            quit();
        }
    }
}
