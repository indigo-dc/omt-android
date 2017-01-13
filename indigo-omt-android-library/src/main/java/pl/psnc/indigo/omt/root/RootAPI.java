package pl.psnc.indigo.omt.root;

import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.BuildConfig;
import pl.psnc.indigo.omt.root.remote.RemoteRootAPI;

/**
 * Created by michalu on 12.01.17.
 */

public class RootAPI implements RootOperations {
    private RemoteRootAPI mRemoteRootAPI;
    private static RootAPI sInstance = null;

    private RootAPI(OkHttpClient client) {
        mRemoteRootAPI = new RemoteRootAPI(client);
    }

    public static synchronized RootAPI getInstance(OkHttpClient client) {
        if (sInstance == null) sInstance = new RootAPI(client);
        return sInstance;
    }

    @Override public String getRoot() {
        try {
            return BuildConfig.FGAPI_ADDRESS + mRemoteRootAPI.getRoot();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
