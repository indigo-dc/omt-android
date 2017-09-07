package pl.psnc.indigo.omt.sampleapp;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.api.model.InputFile;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.root.RootAPI;
import pl.psnc.indigo.omt.sampleapp.views.TestActivity;
import pl.psnc.indigo.omt.tasks.TasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 18.07.16.
 */
@RunWith(AndroidJUnit4.class) public class TasksActivityTest
    extends ActivityInstrumentationTestCase2<TestActivity> {

  private static final String DONE = "done";
  private static final String APPLICATION = "2";
  private static final int KNOWN_TASK_ID = 500;
  private static final String FG_ADDRESS = "http://62.3.168.16";
  private static final String EXPECTED_ROOT = "http://62.3.168.16/v1.0/";

  android.app.Application app;

  public TasksActivityTest() {
    super(TestActivity.class);
  }

  @Before public void setup() throws URISyntaxException {
    System.setProperty("dexmaker.dexcache",
        getInstrumentation().getTargetContext().getCacheDir().getPath());
  }

  @Test(expected = URISyntaxException.class) public void test_getRootAddressForTasks()
      throws URISyntaxException, IndigoException {
    RootAPI rootAPI = RootAPI.getInstance(HttpClientFactory.getNonIAMClient());
    String root = rootAPI.getRoot();
    assertEquals(root, EXPECTED_ROOT);
  }

  @Test public void test_getAllTasks()
      throws InterruptedException, URISyntaxException, IndigoException {
    app = getActivity().getApplication();
    Indigo.init(app, FG_ADDRESS);
    TasksAPI tasksAPI = new TasksAPI(HttpClientFactory.getNonIAMClient());
    TasksWrapper t = tasksAPI.getTasks();
    assertNotNull(t);
  }

  @Test public void test_getTasksByStatus()
      throws InterruptedException, URISyntaxException, IndigoException {
    app = getActivity().getApplication();
    Indigo.init(app, FG_ADDRESS);
    TasksAPI tasksAPI = new TasksAPI(HttpClientFactory.getNonIAMClient());
    TasksWrapper t = tasksAPI.getTasks(DONE);
    assertNotNull(t);
  }

  @Test public void test_getTasksByStatusAndApplication()
      throws InterruptedException, URISyntaxException, IndigoException {
    app = getActivity().getApplication();
    Indigo.init(app, FG_ADDRESS);
    TasksAPI tasksAPI = new TasksAPI(HttpClientFactory.getNonIAMClient());
    TasksWrapper t = tasksAPI.getTasks(DONE, APPLICATION);
    assertNotNull(t);
  }

  @Test public void test_createTask()
      throws InterruptedException, URISyntaxException, IndigoException {
    app = getActivity().getApplication();
    Indigo.init(app, FG_ADDRESS);
    TasksAPI tasksAPI = new TasksAPI(HttpClientFactory.getNonIAMClient());
    Task t = tasksAPI.createTask(createDummyTask(getActivity()));
    assertNotNull(t);
  }

  @Test public void test_getTaskDetail()
      throws InterruptedException, URISyntaxException, IndigoException {
    app = getActivity().getApplication();
    Indigo.init(app, FG_ADDRESS);
    TasksAPI tasksAPI = new TasksAPI(HttpClientFactory.getNonIAMClient());
    Task t = tasksAPI.getTaskDetails(KNOWN_TASK_ID);
    assertNotNull(t);
  }

  @Test public void test_deleteTask()
      throws InterruptedException, URISyntaxException, IndigoException {
    app = getActivity().getApplication();
    Indigo.init(app, FG_ADDRESS);
    TasksAPI tasksAPI = new TasksAPI(HttpClientFactory.getNonIAMClient());
    Task t = tasksAPI.createTask(createDummyTask(getActivity()));
    assertTrue(tasksAPI.deleteTask(t));
  }

  private Task createDummyTask(Context ctx) {
    File file1 = new File(ctx.getExternalFilesDir(null) + File.separator + "sayhello.sh");
    File file2 = new File(ctx.getExternalFilesDir(null) + File.separator + "sayhello.txt");
    try {
      file1.createNewFile();
      file2.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Task task = new Task();
    task.setDescription("TasksAPI v2 - creating task");
    task.setApplication("2");
    task.setUser("test");

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
    return task;
  }
}
