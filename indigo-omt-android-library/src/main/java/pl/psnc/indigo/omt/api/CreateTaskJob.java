package pl.psnc.indigo.omt.api;

import android.net.Uri;
import android.os.Handler;
import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.threads.TasksCreateHandlerThread;

/**
 * Created by michalu on 14.07.16.
 */
public class CreateTaskJob extends ApiHelper implements ApiJob {
    public static final String ENDPOINT = "tasks";
    private Task mTask;

    public CreateTaskJob(Task task) {
        mTask = task;
    }

    public CreateTaskJob(Task task, OkHttpClient client) {
        super(client);
        mTask = task;
    }

    public CreateTaskJob(Task task, Uri rootApiAddress) {
        super(rootApiAddress);
        mTask = task;
    }

    public CreateTaskJob(Task task, OkHttpClient client, Uri rootApiAddress) {
        super(client, rootApiAddress);
        mTask = task;
    }

    @Override public OkHttpClient getClient() {
        return super.getClient();
    }

    @Override public void doAsyncJob(Handler workerHandler, Handler responseHandler,
        IndigoCallback callback) {
        new TasksCreateHandlerThread(this, workerHandler, responseHandler, callback).start();
    }

    @Override public void doAsyncJob(Handler responseHandler, IndigoCallback callback) {
        new TasksCreateHandlerThread(this, null, responseHandler, callback).start();
    }

    public Task getTask() {
        return this.mTask;
    }
}
