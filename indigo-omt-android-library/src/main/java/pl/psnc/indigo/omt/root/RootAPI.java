package pl.psnc.indigo.omt.root;

import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.root.remote.RemoteRootAPI;
import pl.psnc.indigo.omt.utils.FutureGatewayHelper;

/**
 * Created by michalu on 12.01.17.
 */

public class RootAPI implements RootOperations {
    private RemoteRootAPI mRemoteRootAPI;
    private static RootAPI sInstance = null;
    private String mCachedRoot = null;

    private RootAPI(OkHttpClient client) {
        mRemoteRootAPI = new RemoteRootAPI(client);
    }

    public static synchronized RootAPI getInstance(OkHttpClient client) {
        if (sInstance == null) sInstance = new RootAPI(client);
        return sInstance;
    }

    @Override public String getRoot() throws IndigoException {
        if (mCachedRoot == null) {
            mCachedRoot = FutureGatewayHelper.getServerAddress() + mRemoteRootAPI.getRoot();
        }
        return mCachedRoot;
    }
}
