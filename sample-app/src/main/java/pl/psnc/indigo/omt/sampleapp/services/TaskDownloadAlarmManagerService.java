package pl.psnc.indigo.omt.sampleapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.List;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;
import org.greenrobot.eventbus.EventBus;
import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.TaskStatus;
import pl.psnc.indigo.omt.callbacks.TasksCallback;
import pl.psnc.indigo.omt.iam.IAMHelper;
import pl.psnc.indigo.omt.sampleapp.helpers.TasksDownloadedCompleted;

/**
 * Created by michalu on 10.10.16.
 */

public class TaskDownloadAlarmManagerService extends IntentService {

    private static final String TAG = "AlarmManagerService";

    public TaskDownloadAlarmManagerService() {
        super(TAG);
    }

    @Override protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Talking from the TaskDownloadAlarmManagerService");
        final AuthState authState = IAMHelper.readAuthState(getApplicationContext());
        final AuthorizationService service = new AuthorizationService(getApplicationContext());
        if (authState.isAuthorized()) {
            authState.performActionWithFreshTokens(service, new AuthState.AuthStateAction() {
                @Override public void execute(@Nullable String s, @Nullable String s1,
                    @Nullable AuthorizationException e) {
                    Indigo.setAccessToken(s);
                    Indigo.getTasks(TaskStatus.ANY, new TasksCallback() {
                        @Override public void onSuccess(List<Task> result) {
                            EventBus.getDefault()
                                .post(new TasksDownloadedCompleted("New tasks avaialble", result));
                        }

                        @Override public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }
        stopSelf();
    }
}
