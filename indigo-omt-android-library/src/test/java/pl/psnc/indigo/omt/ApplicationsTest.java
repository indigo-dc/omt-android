package pl.psnc.indigo.omt;

import java.net.URISyntaxException;
import java.util.List;
import junit.framework.Assert;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import pl.psnc.indigo.omt.api.model.Application;
import pl.psnc.indigo.omt.applications.ApplicationAPI;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.utils.FutureGatewayHelper;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 13.01.17.
 */

public class ApplicationsTest {
    String username = BuildConfig.FGAPI_USERNAME;
    int id = 2;
    int wrongId = 1111;
    String appName = "SayHello";
    String wrongAppName = "SayBye";

    @Before public void setup() {
        try {
            FutureGatewayHelper.setServerAddress(BuildConfig.FGAPI_ADDRESS);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test public void test_GetAllApps() throws IndigoException {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        ApplicationAPI appAPI = new ApplicationAPI(client);
        List<Application> apps = appAPI.getApplications();
        Assert.assertNotNull(apps);
    }

    @Test public void test_GetAppByName() throws IndigoException {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        ApplicationAPI appAPI = new ApplicationAPI(client);
        Application app = appAPI.getApplication(appName);
        Assert.assertEquals(app.getName(), appName);
    }

    @Test public void test_GetAppById() throws IndigoException {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        ApplicationAPI appAPI = new ApplicationAPI(client);
        Application app = appAPI.getApplication(id);
        Assert.assertNotNull(app);
    }

    @Test public void test_GetNonExistingAppById() throws IndigoException {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        ApplicationAPI appAPI = new ApplicationAPI(client);
        Application app = appAPI.getApplication(wrongId);
        Assert.assertNull(app);
    }

    @Test public void test_GetAppByNonExistingName() throws IndigoException {
        OkHttpClient client = HttpClientFactory.getNonIAMClient();
        ApplicationAPI appAPI = new ApplicationAPI(client);
        Application app = appAPI.getApplication(wrongAppName);
        Assert.assertNull(app);
    }
}
