package pl.psnc.indigo.omt.sampleapp;

import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.applications.ApplicationAPI;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.sampleapp.views.TestActivity;
import pl.psnc.indigo.omt.utils.HttpClientFactory;

/**
 * Created by michalu on 18.07.16.
 */
@RunWith(AndroidJUnit4.class) public class ApplicationsActivityTest
    extends ActivityInstrumentationTestCase2<TestActivity> {

  private static final int KNOWN_APP_ID = 2;
  private static final int WRONG_APP_ID = 22222;
  private static final String KNOWN_APP_NAME = "SayHello";
  private static final String WRONG_APP_NAME = "SayBye";
  private static final String FG_ADDRESS = "http://62.3.168.16";

  android.app.Application app;

  public ApplicationsActivityTest() {
    super(TestActivity.class);
  }

  @Before public void setup() throws URISyntaxException {
    System.setProperty("dexmaker.dexcache",
        getInstrumentation().getTargetContext().getCacheDir().getPath());
  }

  @Test public void test_getAllApplications()
      throws InterruptedException, URISyntaxException, IndigoException {
    app = getActivity().getApplication();
    Indigo.init(app, FG_ADDRESS);
    ApplicationAPI applicationAPI = new ApplicationAPI(HttpClientFactory.getNonIAMClient());
    assertNotNull(applicationAPI.getApplications());
  }

  @Test public void test_getApplicationsByName()
      throws InterruptedException, URISyntaxException, IndigoException {
    app = getActivity().getApplication();
    Indigo.init(app, FG_ADDRESS);
    ApplicationAPI applicationAPI = new ApplicationAPI(HttpClientFactory.getNonIAMClient());
    assertNotNull(applicationAPI.getApplication(KNOWN_APP_NAME));
  }

  @Test public void test_getApplicationsByWrongName()
      throws InterruptedException, URISyntaxException, IndigoException {
    app = getActivity().getApplication();
    Indigo.init(app, FG_ADDRESS);
    ApplicationAPI applicationAPI = new ApplicationAPI(HttpClientFactory.getNonIAMClient());
    assertNull(applicationAPI.getApplication(WRONG_APP_NAME));
  }

  @Test public void test_getApplicationsById()
      throws InterruptedException, URISyntaxException, IndigoException {
    app = getActivity().getApplication();
    Indigo.init(app, FG_ADDRESS);
    ApplicationAPI applicationAPI = new ApplicationAPI(HttpClientFactory.getNonIAMClient());
    assertNotNull(applicationAPI.getApplication(KNOWN_APP_ID));
  }

  @Test public void test_getApplicationsByWrongId()
      throws InterruptedException, URISyntaxException, IndigoException {
    app = getActivity().getApplication();
    Indigo.init(app, FG_ADDRESS);
    ApplicationAPI applicationAPI = new ApplicationAPI(HttpClientFactory.getNonIAMClient());
    assertNull(applicationAPI.getApplication(WRONG_APP_ID));
  }
}
