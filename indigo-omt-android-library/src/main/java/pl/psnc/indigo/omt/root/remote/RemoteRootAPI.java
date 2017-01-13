package pl.psnc.indigo.omt.root.remote;

import android.util.Log;
import java.io.IOException;
import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.api.model.Root;
import pl.psnc.indigo.omt.root.RootOperations;
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

    @Override public String getRoot() {
        String root = null;
        Call<Root> call = mRootAPI.getRoot();
        try {
            Response<Root> rootResponse = call.execute();
            if (rootResponse != null && rootResponse.isSuccessful()) {
                root = "/"
                    + rootResponse.body().getVersions().get(0).getLinks().get(0).getHref()
                    + "/";
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }
        return root;
    }
}
