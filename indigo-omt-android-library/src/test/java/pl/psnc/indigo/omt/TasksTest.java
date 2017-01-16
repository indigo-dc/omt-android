package pl.psnc.indigo.omt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import junit.framework.Assert;
import okhttp3.OkHttpClient;
import org.junit.Test;
import pl.psnc.indigo.omt.api.model.InputFile;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 13.01.17.
 */

public class TasksTest {
    @Test public void test_GetAllTasks() {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        TasksAPI tasksAPI = new TasksAPI(client);
        TasksWrapper tasks = tasksAPI.getTasks("brunor");
        Assert.assertNotNull(tasks);
    }

    @Test public void test_createTask() {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        TasksAPI tasksAPI = new TasksAPI(client);

        File file1 = new File("sayhello.sh");
        File file2 = new File("sayhello.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Task task = new Task();
        task.setDescription("TasksAPI v2 - creating task");
        task.setApplication("2");
        task.setUser("michalu-dev");

        InputFile if1 = new InputFile();
        if1.setFile(file1);
        if1.setName(file1.getName());

        InputFile if2 = new InputFile();
        if2.setFile(file2);
        if2.setName(file2.getName());
        ArrayList<InputFile> files = new ArrayList<>();
        files.add(if1);
        files.add(if2);
        task.setInputFiles(files);

        Task result = tasksAPI.createTask(task);
        Assert.assertNotNull(result);
    }

    @Test public void test_GetTaskDetails() {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        TasksAPI tasksAPI = new TasksAPI(client);

        int taskId = 266;
        Task task = tasksAPI.getTaskDetails(taskId);
        Assert.assertNotNull(task);
    }

    @Test public void test_CreateAndDeleteTask() {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        TasksAPI tasksAPI = new TasksAPI(client);

        Task task = new Task();
        task.setDescription("TasksAPI v2 - creating&deleting task");
        task.setApplication("2");
        task.setUser("michalu-dev");

        Task taskAfterCreating = tasksAPI.createTask(task);

        Assert.assertNotNull(taskAfterCreating);
        Assert.assertTrue(tasksAPI.deleteTask(taskAfterCreating));
    }
}
