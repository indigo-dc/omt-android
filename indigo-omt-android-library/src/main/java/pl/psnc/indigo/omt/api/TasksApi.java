package pl.psnc.indigo.omt.api;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.threads.ApiHandlerThread;
import pl.psnc.indigo.omt.threads.IndigoCallback;
import pl.psnc.indigo.omt.threads.TaskCreateApiThread;
import pl.psnc.indigo.omt.threads.TaskDetailsApiThread;
import pl.psnc.indigo.omt.threads.TasksApiThread;

/**
 * Created by michalu on 21.03.16.
 */
public class TasksApi extends Api {
    private ApiHandlerThread mWorker;
    private final String endpoint = "tasks";
    public static final MediaType MEDIA_TYPE_INDIGO
            = MediaType.parse("application/json");

    public TasksApi(String httpAddress) {
        super(httpAddress);
    }

    public interface TasksCallback extends IndigoCallback {
        void onSuccess(List<Task> result);
    }

    public interface TaskDetailsCallback extends IndigoCallback {
        void onSuccess(Task result);
    }

    public interface TaskCreationCallback extends IndigoCallback {
        void onSuccess(Task result);
    }

    public void getTasks(HashMap params, TasksCallback callback) {
        mWorker = new TasksApiThread(new Handler(Looper.getMainLooper()), callback);
        mWorker.start();
        ((TasksApiThread) mWorker).prepareHandler();
        ((TasksApiThread) mWorker).callApi(httpAddress, endpoint, params);
    }

    public void getTask(int taskId, TaskDetailsCallback callback) {
        mWorker = new TaskDetailsApiThread(new Handler(Looper.getMainLooper()), callback);
        mWorker.start();
        ((TaskDetailsApiThread) mWorker).prepareHandler();
        ((TaskDetailsApiThread) mWorker).callApi(httpAddress, endpoint, taskId);
    }

    public void createTask(Task newTask, TaskCreationCallback callback) {
        mWorker = new TaskCreateApiThread(new Handler(Looper.getMainLooper()), callback);
        mWorker.start();
        ((TaskCreateApiThread) mWorker).prepareHandler();
        ((TaskCreateApiThread) mWorker).callApi(httpAddress, endpoint, newTask);
    }
}
