package pl.psnc.indigo.omt.api;

import android.os.Handler;
import pl.psnc.indigo.omt.callbacks.IndigoCallback;

/**
 * Created by michalu on 15.07.16.
 */
public interface ApiJob {
    void doAsyncJob(Handler workerHandler, Handler responseHandler, IndigoCallback callback);

    void doAsyncJob(Handler responseHandler, IndigoCallback callback);
}
