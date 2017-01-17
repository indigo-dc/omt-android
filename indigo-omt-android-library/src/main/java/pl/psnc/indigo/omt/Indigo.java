package pl.psnc.indigo.omt;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.CreateTaskJob;
import pl.psnc.indigo.omt.api.DeleteTaskJob;
import pl.psnc.indigo.omt.api.GetTaskDetailsJob;
import pl.psnc.indigo.omt.api.GetTasksJob;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.TaskCreationCallback;
import pl.psnc.indigo.omt.callbacks.TaskDeleteCallback;
import pl.psnc.indigo.omt.callbacks.TaskDetailsCallback;
import pl.psnc.indigo.omt.callbacks.TasksCallback;
import pl.psnc.indigo.omt.exceptions.NotInitilizedException;

/**
 * A class which simplifies access to the FutureGateway API
 */
public class Indigo {
    private static String sUsername;
    private static boolean sInitialized = false;
    private static Context sApplicationContext;

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

    public static Context getApplicationContext() {
        return Indigo.sApplicationContext;
    }


    /**
     * A method must be called in the application class of your app - only for debug purposes
     *
     * @param application an instance of your application
     * @param username provided username to init the library. It should be gathered from the API
     */
    public static <T extends Application> void init(T application, String username) {
        sApplicationContext = application.getApplicationContext();
        sUsername = username;
        if (sApplicationContext != null) sInitialized = true;
    }

    /**
     * Gets all status assigned to given user and filtered by status
     *
     * @param status results will be filtered based on provided status
     * @param callback a callback to notify about the result of the operation
     */
    public static void getTasks(String status, AuthState authState, TasksCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new GetTasksJob(status, sUsername).doAsync(new Handler(Looper.getMainLooper()), authState,
            callback);
    }

    /**
     * Gets all tasks assigned to authenticated user
     *
     * @param callback a callback to notify about the result of the operation
     */

    public static void getTasks(AuthState authState, TasksCallback callback) {
        new GetTasksJob(sUsername).doAsync(new Handler(Looper.getMainLooper()), authState,
            callback);
    }

    /**
     * Gets all tasks related with given user and application filtered by status
     *
     * @param application results will be filtered basing on the application name
     * @param status results will be filtered based on the provided status
     * @param callback a callback to notify about the result of the operation
     */
    public static void getTasks(String application, String status, AuthState authState,
        TasksCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new GetTasksJob(status, sUsername, application).doAsync(new Handler(Looper.getMainLooper()),
            authState, callback);
    }

    /**
     * Gets details about task
     *
     * @param task the task which should be obtained
     * @param callback a callback to notify about the result of the operation
     */

    public static void getTask(Task task, AuthState authState, TaskDetailsCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new GetTaskDetailsJob(task).doAsync(new Handler(Looper.getMainLooper()), authState,
            callback);
    }

    /**
     * Executing the task
     *
     * @param newTask a task to execute
     * @param callback a callback to notify about the result of the operation
     */
    public static void createTask(Task newTask, AuthState authState,
        TaskCreationCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        newTask.setUser(sUsername);
        new CreateTaskJob(newTask).doAsync(new Handler(Looper.getMainLooper()), authState,
            callback);
    }

    /**
     * Deleting the task
     *
     * @param task task to delete
     * @param authState an object with auth data
     * @param callback callback informing about results
     */
    public static void deleteTask(Task task, AuthState authState, TaskDeleteCallback callback) {

        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new DeleteTaskJob(task).doAsync(new Handler(Looper.getMainLooper()), authState, callback);
    }
}
