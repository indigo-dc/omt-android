package pl.psnc.indigo.omt.api;

import android.os.Handler;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.threads.TasksDetailsHandlerThread;

/**
 * Created by michalu on 14.07.16.
 */
public class GetTaskDetailsJob implements APIRunner {
    public static final String ENDPOINT = "tasks";
    private Task mTask;

    public GetTaskDetailsJob(Task task) {
        mTask = task;
    }

    @Override public void doAsync(Handler responseHandler, AuthState authState, IndigoCallback callback) {
        new TasksDetailsHandlerThread(mTask, null, responseHandler, authState, callback).start();
    }

    public Task getTask() {
        return this.mTask;
    }
}
