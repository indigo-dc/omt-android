package pl.psnc.indigo.omt.tasks.remote;

import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.psnc.indigo.omt.BuildConfig;
import pl.psnc.indigo.omt.api.model.InputFile;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.TaskStatus;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.tasks.TasksOperations;
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
        mTasksRetrofitAPI =
            RetrofitFactory.getInstance(url, mHttpClient).create(RetrofitTasksAPI.class);
    }

    @Override public TasksWrapper getTasks(String user) {
        TasksWrapper tasks = null;
        Call<TasksWrapper> call = mTasksRetrofitAPI.getTasks(user, TaskStatus.ANY);
        try {
            Response<TasksWrapper> tasksResponse = call.execute();
            if (tasksResponse != null && tasksResponse.isSuccessful()) {
                tasks = tasksResponse.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override public TasksWrapper getTasks(String user, String status) {
        TasksWrapper tasks = null;
        Call<TasksWrapper> call = mTasksRetrofitAPI.getTasks(user, status);
        try {
            Response<TasksWrapper> tasksResponse = call.execute();
            if (tasksResponse != null && tasksResponse.isSuccessful()) {
                tasks = tasksResponse.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override public TasksWrapper getTasks(String user, String status, String application) {
        TasksWrapper tasks = null;
        Call<TasksWrapper> call = mTasksRetrofitAPI.getTasks(user, status, application);
        try {
            Response<TasksWrapper> tasksResponse = call.execute();
            if (tasksResponse != null && tasksResponse.isSuccessful()) {
                tasks = tasksResponse.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override public Task createTask(Task task) {
        Task result = null;
        Call<Task> call = mTasksRetrofitAPI.createTask(task);
        try {
            Response<Task> taskResponse = call.execute();
            if (taskResponse.isSuccessful()) {
                result = taskResponse.body();
                //uploading files
                String uploadUrl = result.getUploadUrl();
                for (InputFile iff : task.getInputFiles()) {
                    RequestBody requestFile =
                        RequestBody.create(MediaType.parse("application/octet-stream"), iff.getFile());
                    MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file[]", iff.getName(), requestFile);
                    String fullUploadUrl = BuildConfig.FGAPI_ADDRESS + uploadUrl;
                    Call<ResponseBody> callUpload =
                        mTasksRetrofitAPI.uploadInputFile(fullUploadUrl, body, task.getUser());
                    Response<ResponseBody> uploadResponse = callUpload.execute();
                    if (!uploadResponse.isSuccessful()) {
                        throw new IndigoException("Cannot upload file: "
                            + iff.getName()
                            + "\n"
                            + uploadResponse.errorBody());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndigoException e) {

        }
        return result;
    }

    @Override public TasksWrapper createTasks(TasksWrapper tasks) {
        return null;
    }

    @Override public Task getTaskDetails(int taskId) {
        return null;
    }

    @Override public Task modifyTask(int taskId, Task task) {
        return null;
    }

    @Override public Boolean deleteTask(int taskId) {
        return null;
    }

    @Override public void uploadInputFile(String url, File file) {
    }
}
