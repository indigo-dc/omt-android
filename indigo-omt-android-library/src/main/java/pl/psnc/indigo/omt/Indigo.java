package pl.psnc.indigo.omt;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.net.URISyntaxException;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.ApplicationByIdCallback;
import pl.psnc.indigo.omt.callbacks.ApplicationByNameCallback;
import pl.psnc.indigo.omt.callbacks.ApplicationsCallback;
import pl.psnc.indigo.omt.callbacks.TaskCreationCallback;
import pl.psnc.indigo.omt.callbacks.TaskDeleteCallback;
import pl.psnc.indigo.omt.callbacks.TaskDetailsCallback;
import pl.psnc.indigo.omt.callbacks.TasksCallback;
import pl.psnc.indigo.omt.exceptions.NotInitilizedException;
import pl.psnc.indigo.omt.threads.ApplicationByIdHandlerThread;
import pl.psnc.indigo.omt.threads.ApplicationByNameHandlerThread;
import pl.psnc.indigo.omt.threads.ApplicationsListHandlerThread;
import pl.psnc.indigo.omt.threads.TasksCreateHandlerThread;
import pl.psnc.indigo.omt.threads.TasksDeleteHandlerThread;
import pl.psnc.indigo.omt.threads.TasksDetailsHandlerThread;
import pl.psnc.indigo.omt.threads.TasksHandlerThread;
import pl.psnc.indigo.omt.utils.FutureGatewayHelper;

/**
 * A class which simplifies access to the FutureGateway API
 */
public class Indigo {
    private static final Handler UI_HANDLER = new Handler(Looper.getMainLooper());
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
     * A method must be called in the application class of your app
     *
     * @param application an instance of your application
     * @param username provided username to init the library. It should be gathered from the API
     */
    public static <T extends Application> void init(T application, String username)
        throws URISyntaxException {
        FutureGatewayHelper.setServerAddress(BuildConfig.FGAPI_ADDRESS);
        sApplicationContext = application.getApplicationContext();
        sUsername = username;
        if (sApplicationContext != null) sInitialized = true;
    }

    /**
     * A method must be called in the application class of your app
     *
     * @param application an instance of your application
     * @param username provided username to init the library. It should be gathered from the API
     * @param serverAddress address of the FutureGateway instance with port without last "/"
     * @throws URISyntaxException
     */

    public static <T extends Application> void init(T application, String username,
        String serverAddress) throws URISyntaxException {
        FutureGatewayHelper.setServerAddress(serverAddress);
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
        new TasksHandlerThread(sUsername, status, null, null, UI_HANDLER, authState,
            callback).start();
    }

    /**
     * Gets all tasks assigned to authenticated user
     *
     * @param callback a callback to notify about the result of the operation
     */

    public static void getTasks(AuthState authState, TasksCallback callback) {
        new TasksHandlerThread(sUsername, null, null, null, UI_HANDLER, authState,
            callback).start();
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
        new TasksHandlerThread(sUsername, status, application, null, UI_HANDLER, authState,
            callback).start();
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
        new TasksDetailsHandlerThread(task, null, UI_HANDLER, authState, callback).start();
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
        new TasksCreateHandlerThread(newTask, null, UI_HANDLER, authState, callback).start();
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
        new TasksDeleteHandlerThread(task, null, UI_HANDLER, authState, callback).start();
    }

    /**
     * Getting applications list
     */
    public static void getApplications(AuthState authState, ApplicationsCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new ApplicationsListHandlerThread(null, UI_HANDLER, authState, callback).start();
    }

    /**
     * Get an application by name
     */
    public static void getApplications(String appName, AuthState authState,
        ApplicationByNameCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new ApplicationByNameHandlerThread(appName, null, UI_HANDLER, authState, callback).start();
    }

    /**
     * Get an application by id
     */
    public static void getApplications(String id, AuthState authState,
        ApplicationByIdCallback callback) {
        try {
            checkInitialization();
        } catch (NotInitilizedException e) {
            callback.onError(e);
            return;
        }
        new ApplicationByIdHandlerThread(id, null, UI_HANDLER, authState, callback).start();
    }
}
