package pl.psnc.indigo.omt.api;

import android.os.Handler;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.threads.TasksCreateHandlerThread;

/**
 * Created by michalu on 14.07.16.
 */
public class CreateTaskJob implements APIRunner {
    public static final String ENDPOINT = "tasks";
    private Task mTask;

    public CreateTaskJob(Task task) {
        mTask = task;
    }

    @Override public void doAsync(Handler responseHandler, IndigoCallback callback) {
        new TasksCreateHandlerThread(mTask, null, responseHandler, callback).start();
    }

    public Task getTask() {
        return this.mTask;
    }
}
