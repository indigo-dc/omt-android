package pl.psnc.indigo.omt.api;

import android.os.Handler;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;

/**
 * Created by michalu on 15.07.16.
 */
public interface APIRunner {

    void doAsync(Handler responseHandler, AuthState authState, IndigoCallback callback);
}
