package pl.psnc.indigo.omt.sampleapp;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.psnc.indigo.omt.BuildConfig;
import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.api.ApiHelper;
import pl.psnc.indigo.omt.api.GetTaskDetailsJob;
import pl.psnc.indigo.omt.api.GetTasksJob;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.TaskStatus;
import pl.psnc.indigo.omt.callbacks.TaskCreationCallback;
import pl.psnc.indigo.omt.callbacks.TaskDetailsCallback;
import pl.psnc.indigo.omt.callbacks.TasksCallback;
import pl.psnc.indigo.omt.exceptions.IndigoException;

/**
 * Created by michalu on 18.07.16.
 */
@RunWith(AndroidJUnit4.class) public class MainActivityTest
    extends ActivityInstrumentationTestCase2<TestActivity> {

    private static final String TAG = "MainActivityTest";

    OkHttpClient mockClient;

    public MainActivityTest() {
        super(TestActivity.class);
    }

    @Before public void setup() {
        System.setProperty("dexmaker.dexcache",
            getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

    public OkHttpClient getMockedClient(String file) {
        OkHttpClient client =
            new OkHttpClient.Builder().addInterceptor(new MockInterceptor(file, getActivity()))
                .build();
        return client;
    }

    @Test(expected = IndigoException.class) public void test_getRootAddressForTasks()
        throws IndigoException {
        GetTasksJob job =
            new GetTasksJob(getMockedClient("versions.json"), ApiHelper.DEFAULT_ADDRESS);
        Uri address = job.getFullUri("tasks");
        assertEquals(Uri.parse(BuildConfig.FGAPI_ADDRESS + "/v1.0/tasks").toString(),
            address.toString());
    }

    @Test(expected = IndigoException.class)
    public void test_getRootAddressForTasksWithUserAndStatus() throws IndigoException {
        GetTasksJob job =
            new GetTasksJob(getMockedClient("versions.json"), ApiHelper.DEFAULT_ADDRESS,
                TaskStatus.ANY, "brunor");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("user", job.getUser());
        queryParams.put("status", job.getStatus());
        Uri address = job.getFullUri("tasks", null, queryParams);
        assertEquals(
            Uri.parse(BuildConfig.FGAPI_ADDRESS + "/v1.0/tasks?status=ANY&user=brunor").toString(),
            address.toString());
    }

    @Test(expected = IndigoException.class) public void test_getRootAddressForTaskDetails()
        throws IndigoException {
        GetTaskDetailsJob job = new GetTaskDetailsJob(1, getMockedClient("versions.json"),
            ApiHelper.DEFAULT_ADDRESS);
        Uri address =
            job.getFullUri("tasks", new String[] { String.valueOf(job.getTaskId()) }, null);
        assertEquals(Uri.parse(BuildConfig.FGAPI_ADDRESS + "/v1.0/tasks/1").toString(),
            address.toString());
    }

    @Test public void test_getAllTasks() throws InterruptedException {
        Log.d(TAG, "starting test_getAllTasks");
        Indigo.init("http://10.0.2.2:8888", "brunor");
        Indigo.getTasks(new TasksCallback() {
            @Override public void onSuccess(List<Task> result) {
                Log.i(TAG, result.toString());
                assertNotNull(result);
            }

            @Override public void onError(Exception exception) {
                assertNull(exception);
            }
        });
        Thread.sleep(12000);
    }

    @Test public void test_getTasksByStatus() throws InterruptedException {
        Log.d(TAG, "starting test_getTasksByStatus");
        Indigo.init("http://10.0.2.2:8888", "brunor");
        Indigo.getTasks(TaskStatus.WAITING, new TasksCallback() {
            @Override public void onSuccess(List<Task> result) {
                Log.i(TAG, result.toString());
                assertNotNull(result);
            }

            @Override public void onError(Exception exception) {
                assertNull(exception);
            }
        });
        Thread.sleep(12000);
    }

    @Test public void test_getTasksByStatusAndApplication() throws InterruptedException {
        Log.d(TAG, "starting test_getTasksByStatusAndApplication");
        Indigo.init("http://10.0.2.2:8888", "brunor");
        Indigo.getTasks(null, TaskStatus.WAITING, new TasksCallback() {
            @Override public void onSuccess(List<Task> result) {
                Log.i(TAG, result.toString());
                assertNotNull(result);
            }

            @Override public void onError(Exception exception) {
                assertNull(exception);
            }
        });
        Thread.sleep(12000);
    }

    @Test public void test_createTask() throws InterruptedException {
        Log.d(TAG, "starting test_createTask");
        Indigo.init("http://10.0.2.2:8888", "brunor");
        Indigo.createTask(new Task("brunor", "sample description", "2"),
            new TaskCreationCallback() {
                @Override public void onSuccess(Task result) {
                    assertNotNull(result);
                }

                @Override public void onError(Exception exception) {
                    assertNull(exception);
                }
            });

        Thread.sleep(12000);
    }

    @Test public void test_getTaskDetail() throws InterruptedException {
        Log.d(TAG, "starting test_getTaskDetail");
        Indigo.init("http://10.0.2.2:8888", "brunor");
        Indigo.getTask(1, new TaskDetailsCallback() {
            @Override public void onSuccess(Task result) {
                assertNotNull(result);
            }

            @Override public void onError(Exception exception) {
                assertNull(exception);
            }
        });

        Thread.sleep(12000);
    }
}
