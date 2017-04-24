package pl.psnc.indigo.omt.root.remote;

import java.io.IOException;
import java.net.SocketTimeoutException;
import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.api.model.Root;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.root.RootOperations;
import pl.psnc.indigo.omt.utils.Log;
import pl.psnc.indigo.omt.utils.RetrofitFactory;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by michalu on 12.01.17.
 */

public class RemoteRootAPI implements RootOperations {
    public static final String TAG = "RemoteTasksAPI";
    private RetrofitRootAPI mRootAPI;
    private OkHttpClient mHttpClient;

    public RemoteRootAPI(OkHttpClient client) {
        mHttpClient = client;
        mRootAPI = RetrofitFactory.getInstanceForRootAPI(mHttpClient).create(RetrofitRootAPI.class);
    }

    @Override public String getRoot() throws IndigoException {
        String root = null;
        Call<Root> call = mRootAPI.getRoot();
        try {
            Response<Root> rootResponse = call.execute();
            if (rootResponse != null && rootResponse.isSuccessful()) {
                Root rootObject = rootResponse.body();
                if (rootObject == null) {
                    throw new IndigoException("Can't get root object from the FG API");
                }
                root = rootObject.getVersions().get(0).getLinks().get(0).getHref() + "/";
            }
        } catch (SocketTimeoutException e) {
            throw new IndigoException(
                "Can't get root object from the FG API. Reason: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }
        return root;
    }
}
