package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.api.model.Application;
import pl.psnc.indigo.omt.applications.ApplicationAPI;
import pl.psnc.indigo.omt.callbacks.ApplicationByNameCallback;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.utils.HttpClientFactory;
import pl.psnc.indigo.omt.utils.Log;

/**
 * Created by michalu on 14.07.16.
 */
public class ApplicationByNameHandlerThread extends ApiHandlerThread implements ApiCallWorkflow {
    public static final String TAG = "AppsListHT";
    private ApplicationAPI mAppsAPI;
    private String mAppName;

    public ApplicationByNameHandlerThread(String appName, Handler workerHandler,
            Handler responseHandler, AuthState authState, IndigoCallback callback) {
        super(TAG, responseHandler, workerHandler, authState, callback);
        mAppName = appName;
    }

    @Override public void networkWork(String accessToken) {
        Log.i(TAG, "networkWork() started");
        try {
            mAppsAPI = new ApplicationAPI(HttpClientFactory.getClient(accessToken));
        } catch (final IndigoException e1) {
            mResponseHandler.get().post(new Runnable() {
                @Override public void run() {
                    if (mCallback.get() != null) mCallback.get().onError(e1);
                }
            });
            quitSafely();
        }

        final Application app = mAppsAPI.getApplication(mAppName);
        if (mResponseHandler.get() != null) {
            mResponseHandler.get().post(new Runnable() {
                @Override public void run() {
                    if (app == null) {
                        if (mCallback.get() != null) {
                            mCallback.get().onError(new IndigoException("No app downloaded"));
                            quitSafely();
                        }
                    } else {
                        if (mCallback.get() != null) {
                            ((ApplicationByNameCallback) mCallback.get()).onSuccess(app);
                            quitSafely();
                        }
                    }
                }
            });
        }
    }
}
