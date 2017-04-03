package pl.psnc.indigo.omt.tasks;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.psnc.indigo.omt.BuildConfig;
import pl.psnc.indigo.omt.api.model.TaskStatus;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.dispatcher.TasksResponsesDispatcher;
import pl.psnc.indigo.omt.tasks.remote.RetrofitTasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;
import pl.psnc.indigo.omt.utils.RetrofitFactory;

/**
 * Created by michalu on 13.01.17.
 */
@RunWith(MockitoJUnitRunner.class) public class TasksAPITest {
    String username = BuildConfig.FGAPI_USERNAME;
    String status = TaskStatus.DONE;
    String applicationId = "2";
    String filename1 = "sayhello.sh";
    String filename2 = "sayhello.txt";

    OkHttpClient client;
    MockWebServer server;
    String baseUrl;
    RetrofitTasksAPI service;

    @Before public void setup() throws IOException {
        client = HttpClientFactory.getNonIAMClient();
        server = new MockWebServer();
        server.setDispatcher(new TasksResponsesDispatcher());
        server.start();
        baseUrl = server.url("").toString();
        service = RetrofitFactory.getInstance(baseUrl, client).create(RetrofitTasksAPI.class);
    }

    @Test public void test_GetAllTasksByUsername() throws IndigoException, IOException {
        TasksAPI tasksAPI = new TasksAPI(client, baseUrl, service);
        TasksWrapper tasks = tasksAPI.getTasks(username);
        Assert.assertNotNull(tasks);
    }

    @Test public void test_GetAllTasksByUsernameAndStatus() throws IndigoException, IOException {
        TasksAPI tasksAPI = new TasksAPI(client, baseUrl, service);
        TasksWrapper tasks = tasksAPI.getTasks(username, status);
        Assert.assertNotNull(tasks);
    }

    @Test public void test_GetAllTasksByUsernameAndStatusAndApplication()
        throws IndigoException, IOException {
        TasksAPI tasksAPI = new TasksAPI(client, baseUrl, service);
        TasksWrapper tasks = tasksAPI.getTasks(username, status, applicationId);
        Assert.assertNotNull(tasks);
    }

    @After public void after() throws IOException {
        //Finish web server
        server.shutdown();
    }
}
