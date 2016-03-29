package pl.psnc.indigo.omt.threads;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.psnc.indigo.omt.api.TasksApi;
import pl.psnc.indigo.omt.api.model.Task;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by michalu on 23.03.16.
 */
public class TasksApiThread extends ApiHandlerThread {
    private static final String TAG = "TasksApiThread";

    public TasksApiThread(Handler responseHandler, TasksApi.TasksCallback callback) {
        super(TAG, responseHandler, callback);
    }

    public void callApi(String baseUrl, String endpoint, HashMap<String, String> map) {
        Bundle data = new Bundle();
        data.putString("baseUrl", baseUrl);
        data.putString("endpoint", endpoint);
        data.putSerializable("map", map);
        Message m = mWorkerHandler.obtainMessage();
        m.setData(data);
        m.sendToTarget();
    }

    public void prepareHandler() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle b = msg.getData();
                String url = b.getString("baseUrl");
                String endpoint = b.getString("endpoint");
                HashMap<String, String> map = (HashMap<String, String>) b.getSerializable("map");
                handleRequest(url, endpoint, map);
                return true;
            }
        });
    }

    protected void handleRequest(String baseUrl, String endpoint, Map<String, String> parameters) {
        super.createFullAddress(baseUrl, endpoint, parameters);
        Log.d(TAG, "Calling " + apiFullAddress);
        try {
            OkHttpClient okHttp = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().get()
                    .url(apiFullAddress)
                    .build();
            Response response = okHttp.newCall(request).execute();
            Type listOfTasks = new TypeToken<List<Task>>() {
            }.getType();
            final List<Task> tasks = new Gson().fromJson(response.body().string(), listOfTasks);
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    ((TasksApi.TasksCallback) mCallback).onSuccess(tasks);
                }
            });
        } catch (final Exception e) {
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError(e);
                }
            });
        }
    }
}