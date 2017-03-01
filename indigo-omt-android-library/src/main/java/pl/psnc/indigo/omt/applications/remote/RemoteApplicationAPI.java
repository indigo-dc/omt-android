package pl.psnc.indigo.omt.applications.remote;

import java.io.IOException;
import java.util.List;
import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.api.model.Application;
import pl.psnc.indigo.omt.api.model.json.ApplicationsWrapper;
import pl.psnc.indigo.omt.applications.ApplicationOperations;
import pl.psnc.indigo.omt.utils.RetrofitFactory;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by michalu on 06.02.17.
 */

public class RemoteApplicationAPI implements ApplicationOperations {
    public static final String TAG = "RemoteTasksAPI";
    private RetrofitApplicationAPI mAppRetrofitAPI;
    private OkHttpClient mHttpClient;

    public RemoteApplicationAPI(String url, OkHttpClient client) {
        mHttpClient = client;
        mAppRetrofitAPI =
            RetrofitFactory.getInstance(url, mHttpClient).create(RetrofitApplicationAPI.class);
    }

    @Override public List<Application> getApplications() {
        List<Application> applications = null;
        Call<ApplicationsWrapper> call = mAppRetrofitAPI.getApplications();
        try {
            Response<ApplicationsWrapper> response = call.execute();
            if (response != null && response.isSuccessful()) {
                ApplicationsWrapper wrapper = response.body();
                if (wrapper != null && wrapper.getApplications() != null) {
                    applications = wrapper.getApplications();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return applications;
    }

    @Override public Application getApplication(int appId) {
        //no-op
        return null;
    }

    @Override public Application getApplication(String name) {
        //no-op
        return null;
    }
}
