package pl.psnc.indigo.omt.tasks.remote;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.json.TasksWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by michalu on 12.01.17.
 */

public interface RetrofitTasksAPI {

    @GET("tasks") Call<TasksWrapper> getTasks(@Query("user") String user,
        @Query("status") String status, @Query("application") String application);

    @GET("tasks") Call<TasksWrapper> getTasks(@Query("user") String user,
        @Query("status") String status);

    @POST("tasks") Call<Task> createTask(@Body Task task);

    @PATCH("tasks") Call<TasksWrapper> createTasks(@Body TasksWrapper tasks);

    @GET("tasks/{id}") Call<Task> getTaskDetails(@Path("id") int taskId);

    @PATCH("tasks/{id}") Call<Task> modifyTask(@Path("id") int taskId, @Body Task task);

    @DELETE("tasks/{id}") Call<Boolean> deleteTask(@Path("id") int taskId);

    @Multipart @POST Call<ResponseBody> uploadInputFile(@Url String url,
        @Part MultipartBody.Part file, @Query("user") String user);
}
