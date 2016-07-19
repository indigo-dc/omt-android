package pl.psnc.indigo.omt.api;

import android.net.Uri;
import android.os.Handler;
import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.api.model.TaskStatus;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.threads.TasksHandlerThread;

/**
 * Created by michalu on 14.07.16.
 */
public class GetTasksJob extends ApiHelper implements ApiJob {
    public static final String ENDPOINT = "tasks";
    private String mStatus = TaskStatus.ANY;
    private String mUser;
    private String mApplication;

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

    public GetTasksJob(Uri rootApiAddress, String status, String user) {
        super(rootApiAddress);
        this.mStatus = status;
        this.mUser = user;
    }

    public GetTasksJob(OkHttpClient client, Uri rootApiAddress, String status, String user) {
        super(client, rootApiAddress);
        this.mStatus = status;
        this.mUser = user;
    }

    public GetTasksJob(OkHttpClient client, Uri rootApiAddress, String status, String user,
            String application) {
        super(client, rootApiAddress);
        this.mStatus = status;
        this.mUser = user;
        this.mApplication = application;
    }

    public GetTasksJob(Uri rootApiAddress, String status, String user, String application) {
        super(rootApiAddress);
        this.mStatus = status;
        this.mUser = user;
        this.mApplication = application;
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

    public String getStatus() {
        return this.mStatus;
    }

    public String getUser() {
        return this.mUser;
    }

    public String getmApplication() {
        return this.mApplication;
    }
}
