package pl.psnc.indigo.omt.api;

import android.os.Handler;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.threads.TasksDeleteHandlerThread;

/**
 * Created by michalu on 14.07.16.
 */
public class DeleteTaskJob implements APIRunner {
    private Task mTask;

    public DeleteTaskJob(Task task) {
        mTask = task;
    }

    @Override public void doAsync(Handler responseHandler, AuthState authState, IndigoCallback callback) {
        new TasksDeleteHandlerThread(mTask, null, responseHandler, authState, callback).start();
    }

    public Task getTask() {
        return this.mTask;
    }
}
