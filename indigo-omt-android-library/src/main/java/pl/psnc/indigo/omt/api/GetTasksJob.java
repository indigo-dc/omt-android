package pl.psnc.indigo.omt.api;

import android.os.Handler;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.model.TaskStatus;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.threads.TasksHandlerThread;

/**
 * Created by michalu on 14.07.16.
 */
public class GetTasksJob implements APIRunner {
    public static final String ENDPOINT = "tasks";
    private String mStatus = TaskStatus.ANY;
    private String mUser;
    private String mApplication;

    public GetTasksJob(String user) {
        this.mUser = user;
    }

    public GetTasksJob(String status, String user) {
        this.mStatus = status;
        this.mUser = user;
    }

    public GetTasksJob(String status, String user, String application) {
        this.mStatus = status;
        this.mUser = user;
        this.mApplication = application;
    }

    @Override public void doAsync(Handler responseHandler, AuthState authState, IndigoCallback callback) {
        new TasksHandlerThread(mUser, mStatus, mApplication, null, responseHandler, authState,
            callback).start();
    }
}
