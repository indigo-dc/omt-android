package pl.psnc.indigo.omt.threads;

import android.os.Handler;
import android.os.HandlerThread;
import java.util.ArrayList;
import java.util.Map;
import pl.psnc.indigo.omt.api.RootApi;
import pl.psnc.indigo.omt.exceptions.WrongApiUrlException;

/**
 * Created by michalu on 23.03.16.
 */
public abstract class ApiHandlerThread extends HandlerThread {
    protected Handler mWorkerHandler;
    protected Handler mResponseHandler;
    protected IndigoCallback mCallback;
    protected String mApiFullAddress;

    public ApiHandlerThread(String name, Handler responseHandler, IndigoCallback callback) {
        super(name);
        mResponseHandler = responseHandler;
        mCallback = callback;
    }

    protected void createFullAddress(String baseUrl, String endpoint,
            Map<String, String> parameters) {
        try {
            mApiFullAddress = RootApi.getRootForAddress(baseUrl).getURLAsString() + endpoint;
            if (parameters != null && parameters.size() > 0) {
                ArrayList values = new ArrayList<>(parameters.values());
                ArrayList keys = new ArrayList(parameters.keySet());
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < values.size(); i++) {
                    if (i == 0) sb.append("?");
                    if (i >= 0 && i < values.size() - 1) {
                        sb.append(keys.get(i));
                        sb.append("=");
                        sb.append(values.get(i));
                        sb.append("&");
                    } else {
                        sb.append(keys.get(i));
                        sb.append("=");
                        sb.append(values.get(i));
                    }
                }
                mApiFullAddress += sb.toString();
            }
        } catch (WrongApiUrlException e) {
            mCallback.onError(e);
            quit();
            return;
        }
    }

    protected void createFullAddress(String baseUrl, String endpoint, int itemId) {
        try {
            mApiFullAddress =
                RootApi.getRootForAddress(baseUrl).getURLAsString() + endpoint + "/" + itemId;
        } catch (WrongApiUrlException e) {
            mCallback.onError(e);
            quit();
            return;
        }
    }
}
