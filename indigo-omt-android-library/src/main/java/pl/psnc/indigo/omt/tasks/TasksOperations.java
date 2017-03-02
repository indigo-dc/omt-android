package pl.psnc.indigo.omt.tasks;

import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;

/**
 * Created by michalu on 13.01.17.
 */

public interface TasksOperations {
    TasksWrapper getTasks(String user);

    TasksWrapper getTasks(String user, String status);

    TasksWrapper getTasks(String user, String status, String application);

    Task createTask(Task task);

    TasksWrapper createTasks(TasksWrapper tasks);

    Task getTaskDetails(int taskId);

    Task modifyTask(int taskId, Task task);

    boolean deleteTask(Task task);

    void uploadInputFile(String url, Task task);
}
