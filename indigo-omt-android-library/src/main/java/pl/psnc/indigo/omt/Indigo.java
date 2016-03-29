package pl.psnc.indigo.omt;

import java.util.HashMap;

import pl.psnc.indigo.omt.api.TasksApi;
import pl.psnc.indigo.omt.exceptions.NotInitilizedException;
import okhttp3.Authenticator;

/**
 * Created by michalu on 21.03.16.
 */
public class Indigo {
    private static String username;
    private static String url;
    private static String apiToken;
    private static Authenticator authenticator;
    private static boolean initialized = false;

    private Indigo() {

    }

    private static void checkInitialization() throws NotInitilizedException {
        if (!initialized)
            throw new NotInitilizedException("Library not initialized! Call Indigo.init() in your Application class");
    }

    /**
     * A method must be called in the application class of your app
     *
     * @param url
     * @param username
     * @param apiToken
     */
    public static void init(String url, String username, String apiToken) {
        Indigo.username = username;
        Indigo.url = url;
        Indigo.apiToken = apiToken;
        initialized = true;
    }

    /**
     * Gets all tasks assigned to given user
     * @param username
     * @param callback
     */
    public static void getTasks(String username, TasksApi.TasksCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        if (username != null && !username.isEmpty())
            params.put("user", username);
        new TasksApi(url).getTasks(params, callback);
    }

    /**
     * Gets all status assigned to given user and filtered by status
     * @param username
     * @param status
     * @param callback
     */
    public static void getTasks(String username, String status, TasksApi.TasksCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        if (username != null && !username.isEmpty())
            params.put("user", username);
        if (status != null && !status.isEmpty())
            params.put("status", status);
        new TasksApi(url).getTasks(params, callback);
    }

    /**
     * Gets all tasks related with given user and application filtered by status
     * @param username
     * @param application
     * @param status
     * @param callback
     */
    public static void getTasks(String username, String application, String status, TasksApi.TasksCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        if (username != null && !username.isEmpty())
            params.put("user", username);
        if (status != null && !status.isEmpty())
            params.put("status", status);
        if (application != null && !application.isEmpty())
            params.put("application", application);
        new TasksApi(url).getTasks(params, callback);
    }

    /**
     * Gets details about task
     * @param taskId
     * @param callback
     */

    public static void getTask(int taskId, TasksApi.TaskDetailsCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new TasksApi((url)).getTask(taskId,callback);
    }

}
