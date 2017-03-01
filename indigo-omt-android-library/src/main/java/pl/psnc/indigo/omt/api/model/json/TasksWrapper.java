package pl.psnc.indigo.omt.api.model.json;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;
import pl.psnc.indigo.omt.api.model.Task;

/**
 * Created by michalu on 31.08.16.
 */
public class TasksWrapper implements Serializable {
    @SerializedName("tasks") private List<Task> mTasks;

    public List<Task> getTasks() {
        return this.mTasks;
    }

    public void setTasks(List<Task> mTasks) {
        this.mTasks = mTasks;
    }
}
