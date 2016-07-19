package pl.psnc.indigo.omt.api;

import android.net.Uri;
import android.os.Handler;
import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.threads.TasksDetailsHandlerThread;

/**
 * Created by michalu on 14.07.16.
 */
public class GetTaskDetailsJob extends ApiHelper implements ApiJob {
    public static final String ENDPOINT = "tasks";
    private int mTaskId;

    public GetTaskDetailsJob(int taskId) {
        mTaskId = taskId;
    }

    public GetTaskDetailsJob(int taskId, OkHttpClient client) {
        super(client);
        mTaskId = taskId;
    }

    public GetTaskDetailsJob(int taskId, Uri rootApiAddress) {
        super(rootApiAddress);
        mTaskId = taskId;
    }

    public GetTaskDetailsJob(int taskId, OkHttpClient client, Uri rootApiAddress) {
        super(client, rootApiAddress);
        mTaskId = taskId;
    }

    @Override public OkHttpClient getClient() {
        return super.getClient();
    }

    @Override public void doAsyncJob(Handler workerHandler, Handler responseHandler,
        IndigoCallback callback) {
        new TasksDetailsHandlerThread(this, workerHandler, responseHandler, callback).start();
    }

    @Override public void doAsyncJob(Handler responseHandler, IndigoCallback callback) {
        new TasksDetailsHandlerThread(this, null, responseHandler, callback).start();
    }

    public int getTaskId() {
        return this.mTaskId;
    }
}
