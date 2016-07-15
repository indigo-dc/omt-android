package pl.psnc.indigo.omt.api2;

import android.net.Uri;
import android.os.Handler;
import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.threads.IndigoCallback;
import pl.psnc.indigo.omt.threads2.TasksHandlerThread;

/**
 * Created by michalu on 14.07.16.
 */
public class GetTasksJob extends RootApi implements ApiJob {
    public static final String ENDPOINT = "tasks";

    public GetTasksJob() {
    }

    public GetTasksJob(OkHttpClient client) {
        super(client);
    }

    public GetTasksJob(Uri rootApiAddress) {
        super(rootApiAddress);
    }

    public GetTasksJob(OkHttpClient client, Uri rootApiAddress) {
        super(client, rootApiAddress);
    }

    @Override public OkHttpClient getClient() {
        return super.getClient();
    }

    @Override public void doAsyncJob(Handler workerHandler, Handler responseHandler,
        IndigoCallback callback) {
        new TasksHandlerThread(this, workerHandler, responseHandler, callback).start();
    }

    @Override public void doAsyncJob(Handler responseHandler, IndigoCallback callback) {
        new TasksHandlerThread(this, null, responseHandler, callback).start();
    }
}
