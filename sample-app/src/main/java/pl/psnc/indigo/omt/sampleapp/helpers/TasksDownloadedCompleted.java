package pl.psnc.indigo.omt.sampleapp.helpers;

import java.util.List;
import pl.psnc.indigo.omt.api.model.Task;

/**
 * Created by michalu on 07.10.16.
 */

public class TasksDownloadedCompleted extends MessageEvent {
    private List<Task> mTasks;

    public TasksDownloadedCompleted(String message, List<Task> list) {
        super(message);
        mTasks = list;
    }

    public List<Task> getTasks() {
        return this.mTasks;
    }

    public void setTasks(List<Task> mTasks) {
        this.mTasks = mTasks;
    }
}
