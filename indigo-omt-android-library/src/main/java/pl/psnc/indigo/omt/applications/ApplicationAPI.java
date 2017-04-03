package pl.psnc.indigo.omt.applications;

import java.util.List;
import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.api.BaseAPI;
import pl.psnc.indigo.omt.api.model.Application;
import pl.psnc.indigo.omt.applications.remote.RemoteApplicationAPI;
import pl.psnc.indigo.omt.applications.remote.RetrofitApplicationAPI;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.root.RootAPI;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 06.02.17.
 */

public class ApplicationAPI implements ApplicationOperations {

    private BaseAPI mBaseAPI;
    private RemoteApplicationAPI mRemoteApplicationAPI;

    public ApplicationAPI(OkHttpClient okHttpClient) throws IndigoException {
        mBaseAPI = new BaseAPI(RootAPI.getInstance(HttpClientFactory.getNonIAMClient()));
        String url = mBaseAPI.getRoot();
        mRemoteApplicationAPI = new RemoteApplicationAPI(url, okHttpClient);
    }

    public ApplicationAPI(OkHttpClient okHttpClient, RetrofitApplicationAPI retrofitApplicationAPI)
        throws IndigoException {
        mBaseAPI = new BaseAPI(RootAPI.getInstance(okHttpClient));
        mRemoteApplicationAPI = new RemoteApplicationAPI(okHttpClient, retrofitApplicationAPI);
    }

    @Override public List<Application> getApplications() {
        return mRemoteApplicationAPI.getApplications();
    }

    @Override public Application getApplication(int appId) {
        List<Application> apps = mRemoteApplicationAPI.getApplications();
        if (apps != null && !apps.isEmpty()) {
            for (Application app : apps) {
                if (Integer.parseInt(app.getId()) == appId) return app;
            }
        }
        return null;
    }

    @Override public Application getApplication(String name) {
        List<Application> apps = mRemoteApplicationAPI.getApplications();
        if (apps != null && !apps.isEmpty()) {
            for (Application app : apps) {
                if (app.getName().equals(name)) return app;
            }
        }
        return null;
    }
}
