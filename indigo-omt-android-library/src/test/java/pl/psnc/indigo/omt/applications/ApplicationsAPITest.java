package pl.psnc.indigo.omt.applications;

import java.io.IOException;
import java.util.List;
import junit.framework.Assert;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;
import pl.psnc.indigo.omt.BuildConfig;
import pl.psnc.indigo.omt.api.model.Application;
import pl.psnc.indigo.omt.applications.remote.RetrofitApplicationAPI;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.dispatcher.ApplicationsResponsesDispatcher;
import pl.psnc.indigo.omt.utils.HttpClientFactory;
import pl.psnc.indigo.omt.utils.RetrofitFactory;

/**
 * Created by michalu on 13.01.17.
 */

public class ApplicationsAPITest {
    String username = BuildConfig.FGAPI_USERNAME;
    int id = 2;
    int wrongId = 1111;
    String appName = "SayHello";
    String wrongAppName = "SayBye";

    OkHttpClient client;
    MockWebServer server;
    String baseUrl;
    RetrofitApplicationAPI service;

    ApplicationAPI api;

    @Before public void setup() throws IOException, IndigoException {
        client = HttpClientFactory.getNonIAMClient();
        server = new MockWebServer();
        server.setDispatcher(new ApplicationsResponsesDispatcher());
        server.start();
        baseUrl = server.url("").toString();
        service = RetrofitFactory.getInstance(baseUrl, client).create(RetrofitApplicationAPI.class);
        api = new ApplicationAPI(client, service);
    }

    @Test public void test_GetAllApps() throws IndigoException {
        List<Application> apps = api.getApplications();
        Assert.assertNotNull(apps);
    }

    @Test public void test_GetAppByName() throws IndigoException {
        Application app = api.getApplication(appName);
        Assert.assertEquals(app.getName(), appName);
    }

    @Test public void test_GetAppById() throws IndigoException {
        Application app = api.getApplication(id);
        Assert.assertNotNull(app);
    }

    @Test public void test_GetNonExistingAppById() throws IndigoException {
        Application app = api.getApplication(wrongId);
        Assert.assertNull(app);
    }

    @Test public void test_GetAppByNonExistingName() throws IndigoException {
        Application app = api.getApplication(wrongAppName);
        Assert.assertNull(app);
    }
}
