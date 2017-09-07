package pl.psnc.indigo.omt.tasks.remote;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.psnc.indigo.omt.api.model.InputFile;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.tasks.TasksOperations;
import pl.psnc.indigo.omt.utils.FutureGatewayHelper;
import pl.psnc.indigo.omt.utils.RetrofitFactory;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by michalu on 12.01.17.
 */

public class RemoteTasksAPI implements TasksOperations {
  public static final String TAG = "RemoteTasksAPI";
  private RetrofitTasksAPI mTasksRetrofitAPI;
  private OkHttpClient mHttpClient;
  private String mUrl;

  public RemoteTasksAPI(String url, OkHttpClient client) {
    mHttpClient = client;
    mUrl = url;
    try {
      mTasksRetrofitAPI =
          RetrofitFactory.getInstance(url, mHttpClient).create(RetrofitTasksAPI.class);
    } catch (Exception e) {
      mTasksRetrofitAPI = null;
    }
  }

  public RemoteTasksAPI(String url, OkHttpClient client, RetrofitTasksAPI tasksRetrofitAPI) {
    mHttpClient = client;
    mUrl = url;
    mTasksRetrofitAPI = tasksRetrofitAPI;
  }

  @Override public TasksWrapper getTasks() {
    TasksWrapper tasks = null;
    try {
      Call<TasksWrapper> call = mTasksRetrofitAPI.getTasks();
      Response<TasksWrapper> tasksResponse = call.execute();
      if (tasksResponse != null && tasksResponse.isSuccessful()) {
        tasks = tasksResponse.body();
      }
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    return tasks;
  }

  @Override public TasksWrapper getTasks(String status) {
    TasksWrapper tasks = null;
    try {
      Call<TasksWrapper> call = mTasksRetrofitAPI.getTasks(status);
      Response<TasksWrapper> tasksResponse = call.execute();
      if (tasksResponse != null && tasksResponse.isSuccessful()) {
        tasks = tasksResponse.body();
      }
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    return tasks;
  }

  @Override public TasksWrapper getTasks(String status, String application) {
    TasksWrapper tasks = null;
    try {
      Call<TasksWrapper> call = mTasksRetrofitAPI.getTasks(status, application);
      Response<TasksWrapper> tasksResponse = call.execute();
      if (tasksResponse != null && tasksResponse.isSuccessful()) {
        tasks = tasksResponse.body();
      }
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    return tasks;
  }

  @Override public Task createTask(Task task) {
    Task result = null;
    try {
      Call<Task> call = mTasksRetrofitAPI.createTask(task);
      Response<Task> taskResponse = call.execute();
      if (taskResponse.isSuccessful()) {
        result = taskResponse.body();
        //uploading files
        uploadInputFile(result.getUploadUrl(), task);
      }
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override public TasksWrapper createTasks(TasksWrapper tasks) {
    //TODO: implement creating multiple tasks
    return null;
  }

  @Override public Task getTaskDetails(int taskId) {
    Task task = null;
    try {
      Call<Task> call = mTasksRetrofitAPI.getTaskDetails(taskId);
      Response<Task> taskResponse = call.execute();
      if (taskResponse.isSuccessful()) {
        task = taskResponse.body();
      }
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    return task;
  }

  @Override public Task modifyTask(int taskId, Task task) {
    return null;
  }

  @Override public boolean deleteTask(Task task) {
    boolean result = false;
    try {
      Call<ResponseBody> call = mTasksRetrofitAPI.deleteTask(Integer.parseInt(task.getId()));
      Response<ResponseBody> response = call.execute();
      if (response.isSuccessful()) {
        result = true;
      }
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override public void uploadInputFile(String url, Task task) {
    String uploadUrl = url;
    for (InputFile iff : task.getInputFiles()) {
      if (iff.getFile() == null) continue;
      RequestBody requestFile =
          RequestBody.create(MediaType.parse("application/octet-stream"), iff.getFile());
      MultipartBody.Part body =
          MultipartBody.Part.createFormData("file[]", iff.getName(), requestFile);
      if (uploadUrl.startsWith("/")) uploadUrl = uploadUrl.substring(1);
      String fullUploadUrl = FutureGatewayHelper.getServerAddress() + uploadUrl;
      try {
        Call<ResponseBody> callUpload =
            mTasksRetrofitAPI.uploadInputFile(fullUploadUrl, body, task.getUser());
        Response<ResponseBody> uploadResponse = callUpload.execute();
        if (!uploadResponse.isSuccessful()) {
          throw new IndigoException(
              "Cannot upload file: " + iff.getName() + "\n" + uploadResponse.errorBody());
        }
      } catch (IndigoException | NullPointerException | IOException e) {
        e.printStackTrace();
      }
    }
  }
}
