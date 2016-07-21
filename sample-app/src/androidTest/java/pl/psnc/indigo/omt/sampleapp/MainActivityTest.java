package pl.psnc.indigo.omt.sampleapp;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.psnc.indigo.omt.api.model.TaskStatus;
import pl.psnc.indigo.omt.api.GetTaskDetailsJob;
import pl.psnc.indigo.omt.api.GetTasksJob;
import pl.psnc.indigo.omt.api.ApiHelper;
import pl.psnc.indigo.omt.exceptions.IndigoException;

/**
 * Created by michalu on 18.07.16.
 */
@RunWith(AndroidJUnit4.class) public class MainActivityTest
    extends ActivityInstrumentationTestCase2<TestActivity> {

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
            new GetTasksJob(getMockedClient("versions.json"), ApiHelper.EMULATOR_LOCALHOST_ADDRESS);
        Uri address = job.getFullUri("tasks");
        assertEquals(Uri.parse("http://10.0.2.2:8888/v1.0/tasks").toString(), address.toString());
    }

    @Test(expected = IndigoException.class) public void test_getRootAddressForTasksWithUserAndStatus()
        throws IndigoException {
        GetTasksJob job =
            new GetTasksJob(getMockedClient("versions.json"), ApiHelper.EMULATOR_LOCALHOST_ADDRESS,
                TaskStatus.ANY, "brunor");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("user", job.getUser());
        queryParams.put("status", job.getStatus());
        Uri address = job.getFullUri("tasks", null, queryParams);
        assertEquals(Uri.parse("http://10.0.2.2:8888/v1.0/tasks?status=ANY&user=brunor").toString(),
            address.toString());
    }

    @Test(expected = IndigoException.class) public void test_getRootAddressForTaskDetails()
        throws IndigoException {
        GetTaskDetailsJob job = new GetTaskDetailsJob(1, getMockedClient("versions.json"),
            ApiHelper.EMULATOR_LOCALHOST_ADDRESS);
        Uri address =
            job.getFullUri("tasks", new String[] { String.valueOf(job.getTaskId()) }, null);
        assertEquals(Uri.parse("http://10.0.2.2:8888/v1.0/tasks/1").toString(), address.toString());
    }
}
