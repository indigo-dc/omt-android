package pl.psnc.indigo.omt;

import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import pl.psnc.indigo.omt.api.CreateTaskJob;
import pl.psnc.indigo.omt.api.GetTaskDetailsJob;
import pl.psnc.indigo.omt.api.GetTasksJob;
import pl.psnc.indigo.omt.api.ApiHelper;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.TaskCreationCallback;
import pl.psnc.indigo.omt.callbacks.TaskDetailsCallback;
import pl.psnc.indigo.omt.callbacks.TasksCallback;
import pl.psnc.indigo.omt.exceptions.NotInitilizedException;

/**
 * A class which simplifies access to the FutureGateway API
 */
public class Indigo {
    private static String sUsername;
    private static String sUrl;
    private static String sApiToken;
    private static boolean sInitialized = false;

    private Indigo() {

    }

    /**
     * Checking initialization of the library
     *
     * @throws NotInitilizedException if library is not initalized
     */

    private static void checkInitialization() throws NotInitilizedException {
        if (!sInitialized) {
            throw new NotInitilizedException(
                "Library not initialized! Call Indigo.init() in your Application class");
        }
    }

    /**
     * A method must be called in the application class of your app
     *
     * @param url domain or IP address of the FutureGateway instance
     */
    public static void init(String url) {
        if (sInitialized) return;
        Indigo.sUrl = url;
        sInitialized = true;
    }

    /**
     * A method must be called in the application class of your app - only for debug purposes
     *
     * @param url domain or IP address of the FutureGateway instance
     * @param username provided username to init the library. It should be gathered from the API
     */
    public static void init(String url, String username) {
        if (sInitialized) return;
        Indigo.sUrl = url;
        Indigo.sUsername = username;
        sInitialized = true;
    }

    /**
     * Gets all status assigned to given user and filtered by status
     *
     * @param status results will be filtered based on provided status
     * @param callback a callback to notify about the result of the operation
     */
    public static void getTasks(String status, TasksCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new GetTasksJob(ApiHelper.EMULATOR_LOCALHOST_ADDRESS, status, sUsername).doAsyncJob(
                new Handler(Looper.getMainLooper()), callback);
    }

    /**
     * Gets all tasks assigned to authenticated user
     *
     * @param callback a callback to notify about the result of the operation
     */

    public static void getTasks(TasksCallback callback) {
        new GetTasksJob(ApiHelper.EMULATOR_LOCALHOST_ADDRESS).doAsyncJob(
                new Handler(Looper.getMainLooper()), callback);
    }

    /**
     * Gets all tasks related with given user and application filtered by status
     *
     * @param application results will be filtered basing on the application name
     * @param status results will be filtered based on the provided status
     * @param callback a callback to notify about the result of the operation
     */
    public static void getTasks(String application, String status, TasksCallback callback) {
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
        new GetTasksJob(ApiHelper.EMULATOR_LOCALHOST_ADDRESS, status, sUsername,
            application).doAsyncJob(new Handler(Looper.getMainLooper()), callback);
    }

    /**
     * Gets details about task
     *
     * @param taskId id of the task which should be obtained
     * @param callback a callback to notify about the result of the operation
     */

    public static void getTask(int taskId, TaskDetailsCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new GetTaskDetailsJob(taskId, ApiHelper.EMULATOR_LOCALHOST_ADDRESS).doAsyncJob(
                new Handler(Looper.getMainLooper()), callback);
    }

    /**
     * Executing the task
     *
     * @param newTask a task to execute
     * @param callback a callback to notify about the result of the operation
     */
    public static void createTask(Task newTask, TaskCreationCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new CreateTaskJob(newTask, ApiHelper.EMULATOR_LOCALHOST_ADDRESS).doAsyncJob(
                new Handler(Looper.getMainLooper()), callback);
    }
}
