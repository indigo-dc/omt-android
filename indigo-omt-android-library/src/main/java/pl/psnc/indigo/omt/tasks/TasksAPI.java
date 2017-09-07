package pl.psnc.indigo.omt.tasks;

import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.api.BaseAPI;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.root.RootAPI;
import pl.psnc.indigo.omt.tasks.remote.RemoteTasksAPI;
import pl.psnc.indigo.omt.tasks.remote.RetrofitTasksAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 13.01.17.
 */

public class TasksAPI implements TasksOperations {
  private BaseAPI mBaseAPI;
  private RemoteTasksAPI mTasksAPI;

  public TasksAPI(OkHttpClient okHttpClient) throws IndigoException {
    mBaseAPI = new BaseAPI(RootAPI.getInstance(HttpClientFactory.getNonIAMClient()));
    String url = mBaseAPI.getRoot();
    mTasksAPI = new RemoteTasksAPI(url, okHttpClient);
  }

  public TasksAPI(OkHttpClient okHttpClient, String url, RetrofitTasksAPI tasksRetrofitAPI)
      throws IndigoException {
    mBaseAPI = new BaseAPI(RootAPI.getInstance(HttpClientFactory.getNonIAMClient()));
    mTasksAPI = new RemoteTasksAPI(url, okHttpClient, tasksRetrofitAPI);
  }

  @Override public TasksWrapper getTasks() {
    return mTasksAPI.getTasks();
  }

  @Override public TasksWrapper getTasks(String status) {
    return mTasksAPI.getTasks(status);
  }

  @Override public TasksWrapper getTasks(String status, String application) {
    return mTasksAPI.getTasks(status, application);
  }

  @Override public Task createTask(Task task) {
    return mTasksAPI.createTask(task);
  }

  @Override public TasksWrapper createTasks(TasksWrapper tasks) {
    return mTasksAPI.createTasks(tasks);
  }

  @Override public Task getTaskDetails(int taskId) {
    return mTasksAPI.getTaskDetails(taskId);
  }

  @Override public Task modifyTask(int taskId, Task task) {
    return null;
  }

  @Override public boolean deleteTask(Task task) {
    return mTasksAPI.deleteTask(task);
  }

  @Override public void uploadInputFile(String url, Task task) {
    mTasksAPI.uploadInputFile(url, task);
  }
}
