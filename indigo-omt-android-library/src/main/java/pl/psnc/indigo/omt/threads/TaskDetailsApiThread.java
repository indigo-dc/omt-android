package pl.psnc.indigo.omt.threads;

/**
 * Created by michalu on 24.03.16.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.psnc.indigo.omt.api.TasksApi;
import pl.psnc.indigo.omt.api.model.Task;

/**
 * Created by michalu on 23.03.16.
 */
public class TaskDetailsApiThread extends ApiHandlerThread {
    private static final String TAG = "TasksApiThread";

    public TaskDetailsApiThread(Handler responseHandler, TasksApi.TaskDetailsCallback callback) {
        super(TAG, responseHandler, callback);
    }

    public void callApi(String baseUrl, String endpoint, int taskId) {
        Bundle data = new Bundle();
        data.putString("baseUrl", baseUrl);
        data.putString("endpoint", endpoint);
        data.putInt("taskId", taskId);
        Message m = mWorkerHandler.obtainMessage();
        m.setData(data);
        m.sendToTarget();
    }

    public void prepareHandler() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override public boolean handleMessage(Message msg) {
                Bundle b = msg.getData();
                String url = b.getString("baseUrl");
                String endpoint = b.getString("endpoint");
                int taskId = b.getInt("taskId");
                handleRequest(url, endpoint, taskId);
                return true;
            }
        });
    }

    protected void handleRequest(String baseUrl, String endpoint, int taskId) {
        super.createFullAddress(baseUrl, endpoint, taskId);
        Log.d(TAG, "Calling " + mApiFullAddress);
        try {
            OkHttpClient okHttp = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().get().url(mApiFullAddress).build();
            Response response = okHttp.newCall(request).execute();
            Type taskType = new TypeToken<Task>() {
            }.getType();
            final Task task = new Gson().fromJson(response.body().string(), taskType);
            mResponseHandler.post(new Runnable() {
                @Override public void run() {
                    ((TasksApi.TaskDetailsCallback) mCallback).onSuccess(task);
                }
            });
        } catch (final IOException e) {
            mResponseHandler.post(new Runnable() {
                @Override public void run() {
                    mCallback.onError(e);
                }
            });
        } catch (final IllegalArgumentException e) {
            mResponseHandler.post(new Runnable() {
                @Override public void run() {
                    mCallback.onError(e);
                }
            });
        } catch (final NullPointerException e) {
            mResponseHandler.post(new Runnable() {
                @Override public void run() {
                    mCallback.onError(e);
                }
            });
        }
    }
}