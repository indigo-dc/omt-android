package pl.psnc.indigo.omt.threads;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import java.lang.ref.WeakReference;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;
import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;
import pl.psnc.indigo.omt.utils.Log;
import pl.psnc.indigo.omt.utils.MessageCodes;

/**
 * Created by michalu on 17.01.17.
 */

public class ApiHandlerThread extends HandlerThread implements ApiCallWorkflow {
    public static final String TAG = "ApiHandlerThread";
    public static final String ACCESS_TOKEN_TAG = "accessToken";

    protected WeakReference<Handler> mResponseHandler;
    protected Handler mWorkerHandler;
    protected WeakReference<IndigoCallback> mCallback;
    protected AuthState mAuthState;

    public ApiHandlerThread(String name, Handler responseHandler, Handler workerHandler,
        AuthState authState, IndigoCallback callback) {
        super(name);
        this.mResponseHandler = new WeakReference<Handler>(responseHandler);
        this.mWorkerHandler = workerHandler;
        this.mCallback = new WeakReference<IndigoCallback>(callback);
        this.mAuthState = authState;
    }

    @Override protected void onLooperPrepared() {
        Log.i(TAG, "onLooperPrepared()");
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageCodes.MESSAGE_AUTHENTICATE: //TODO: rethink the mechanism
                        authenticate(mAuthState);
                        break;
                    case MessageCodes.MESSAGE_FG_REQUEST:
                        String token = msg.getData().getString(ACCESS_TOKEN_TAG);
                        networkWork(token);
                }
                return true;
            }
        });
        prepare();
    }

    @Override public synchronized void start() {
        super.start();
    }

    @Override public void prepare() {
        Log.i(TAG, "prepare()");
        Message m = mWorkerHandler.obtainMessage();
        m.what = MessageCodes.MESSAGE_AUTHENTICATE;
        m.sendToTarget();
    }

    @Override public void authenticate(AuthState authState) {
        Log.i(TAG, "authenticate()");
        Context ctx = Indigo.getApplicationContext();
        AuthorizationService authService = new AuthorizationService(ctx);
        authState.performActionWithFreshTokens(authService, new AuthState.AuthStateAction() {
            @Override public void execute(@Nullable String accessToken, @Nullable String s1,
                @Nullable AuthorizationException e) {
                Log.i(TAG, "performedActionWithFreshTokens()\nAccess token = " + accessToken);
                if (e != null) {
                    e.printStackTrace();
                }
                Message m = mWorkerHandler.obtainMessage();
                Bundle data = new Bundle();
                data.putString(ACCESS_TOKEN_TAG, accessToken);
                m.what = MessageCodes.MESSAGE_FG_REQUEST;
                m.setData(data);
                m.sendToTarget();
            }
        });
    }

    @Override public void networkWork(String accessToken) {
        //to override in the child
    }
}