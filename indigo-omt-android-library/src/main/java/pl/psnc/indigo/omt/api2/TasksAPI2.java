package pl.psnc.indigo.omt.api2;

import android.os.Handler;
import android.os.Looper;

import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.api.TasksApi;
import pl.psnc.indigo.omt.threads.IndigoCallback;
import pl.psnc.indigo.omt.threads2.TasksHandlerThread;

/**
 * Created by michalu on 14.07.16.
 */
public class TasksAPI2 extends RootAPI2 {
    public static final String ENDPOINT = "tasks";
    private IndigoCallback mCallback;
    private Handler mResponseHandler;

    public TasksAPI2(TasksApi.TasksCallback callback) {
        mCallback = callback;
        mResponseHandler = new Handler(Looper.getMainLooper());
    }

    public TasksAPI2(TasksApi.TasksCallback callback, Handler responseHandler) {
        mCallback = callback;
        mResponseHandler = responseHandler;
    }

    @Override
    public OkHttpClient getClient() {
        return super.getClient();
    }

    public void callApi() {
        new TasksHandlerThread(mResponseHandler, this).start();
    }

    public IndigoCallback getCallback() {
        return mCallback;
    }
}
