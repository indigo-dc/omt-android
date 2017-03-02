package pl.psnc.indigo.omt;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import junit.framework.Assert;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import pl.psnc.indigo.omt.api.model.InputFile;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.FutureGatewayHelper;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 13.01.17.
 */

public class TasksTest {
    String username = BuildConfig.FGAPI_USERNAME;
    String applicationId = "2";
    String filename1 = "sayhello.sh";
    String filename2 = "sayhello.txt";

    @Before public void setup() {
        try {
            FutureGatewayHelper.setServerAddress(BuildConfig.FGAPI_ADDRESS);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test public void test_GetAllTasks() throws IndigoException {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        TasksAPI tasksAPI = new TasksAPI(client);
        TasksWrapper tasks = tasksAPI.getTasks(username);
        Assert.assertNotNull(tasks);
    }

    @Test public void test_createTask() throws IndigoException {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        TasksAPI tasksAPI = new TasksAPI(client);

        File file1 = new File(filename1);
        File file2 = new File(filename2);
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Task task = new Task();
        task.setDescription("TasksAPI v2 - creating task");
        task.setApplication(applicationId);
        task.setUser(username);

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

    public void test_GetTaskDetails(int taskId) throws IndigoException {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        TasksAPI tasksAPI = new TasksAPI(client);
        Task task = tasksAPI.getTaskDetails(taskId);
        System.out.println(task);
        Assert.assertNotNull(task);
    }

    @Test public void test_CreateAndDeleteTask() throws IndigoException {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        TasksAPI tasksAPI = new TasksAPI(client);

        Task task = new Task();
        task.setDescription("TasksAPI v2 - creating&deleting task");
        task.setApplication(applicationId);
        task.setUser(username);

        Task taskAfterCreating = tasksAPI.createTask(task);
        System.out.println(taskAfterCreating);
        test_GetTaskDetails(Integer.parseInt(taskAfterCreating.getId()));
        Assert.assertNotNull(taskAfterCreating);
        Assert.assertTrue(tasksAPI.deleteTask(taskAfterCreating));
    }
}
