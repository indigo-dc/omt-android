package pl.psnc.indigo.omt.threads;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.psnc.indigo.omt.api.TasksApi;
import pl.psnc.indigo.omt.api.model.Task;

/**
 * Created by michalu on 23.03.16.
 */
public class TaskCreateApiThread extends ApiHandlerThread {
    private static final String TAG = "TaskCreateApiThread";

    public TaskCreateApiThread(Handler responseHandler, TasksApi.TaskCreationCallback callback) {
        super(TAG, responseHandler, callback);
    }

    public void callApi(String baseUrl, String endpoint, Task newTask) {
        Bundle data = new Bundle();
        data.putString("baseUrl", baseUrl);
        data.putString("endpoint", endpoint);
        data.putSerializable("task", newTask);
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
                Task newTask = (Task) b.getSerializable("task");
                handleRequest(url, endpoint, newTask);
                return true;
            }
        });
    }

    protected void handleRequest(String baseUrl, String endpoint, Task newTask) {
        HashMap userMap = new HashMap();
        Type taskType = new TypeToken<Task>() {
        }.getType();
        final String payload = new Gson().toJson(newTask, Task.class);
        if (newTask != null && newTask.getUser() != null)
            userMap.put("user", newTask.getUser());
        super.createFullAddress(baseUrl, endpoint, userMap);
        Log.d(TAG, "Calling " + apiFullAddress);
        try {
            OkHttpClient okHttp = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().post(RequestBody.create(TasksApi.MEDIA_TYPE_INDIGO, payload))
                    .url(apiFullAddress)
                    .build();

            Response response = okHttp.newCall(request).execute();

            final Task task = new Gson().fromJson(response.body().string(), taskType);
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    ((TasksApi.TaskCreationCallback) mCallback).onSuccess(task);
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