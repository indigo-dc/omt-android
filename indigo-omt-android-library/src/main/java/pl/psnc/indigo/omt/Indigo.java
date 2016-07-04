package pl.psnc.indigo.omt;

import java.util.HashMap;
import okhttp3.Authenticator;
import pl.psnc.indigo.omt.api.TasksApi;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.exceptions.NotInitilizedException;

/**
 * Created by michalu on 21.03.16.
 */
public class Indigo {
    private static String sUsername;
    private static String sUrl;
    private static String sApiToken;
    private static Authenticator sAuthenticator;
    private static boolean sInitialized = false;

    private Indigo() {

    }

    private static void checkInitialization() throws NotInitilizedException {
        if (!sInitialized) {
            throw new NotInitilizedException(
                "Library not initialized! Call Indigo.init() in your Application class");
        }
    }

    /**
     * A method must be called in the application class of your app
     */
    public static void init(String url) {
        if (sInitialized) return;
        Indigo.sUrl = url;
        sInitialized = true;
    }

    /**
     * Gets all tasks assigned to given user
     */
    public static void getTasks(TasksApi.TasksCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        if (sUsername != null && !sUsername.isEmpty()) params.put("user", sUsername);
        new TasksApi(sUrl).getTasks(params, callback);
    }

    /**
     * Gets all status assigned to given user and filtered by status
     */
    public static void getTasks(String status, TasksApi.TasksCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        if (sUsername != null && !sUsername.isEmpty()) params.put("user", sUsername);
        if (status != null && !status.isEmpty()) params.put("status", status);
        new TasksApi(sUrl).getTasks(params, callback);
    }

    /**
     * Gets all tasks related with given user and application filtered by status
     */
    public static void getTasks(String application, String status,
        TasksApi.TasksCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        if (sUsername != null && !sUsername.isEmpty()) params.put("user", sUsername);
        if (status != null && !status.isEmpty()) params.put("status", status);
        if (application != null && !application.isEmpty()) params.put("application", application);
        new TasksApi(sUrl).getTasks(params, callback);
    }

    /**
     * Gets details about task
     */

    public static void getTask(int taskId, TasksApi.TaskDetailsCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new TasksApi((sUrl)).getTask(taskId, callback);
    }

    public static void createTask(Task newTask, TasksApi.TaskCreationCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new TasksApi((sUrl)).createTask(newTask, callback);
    }
}
