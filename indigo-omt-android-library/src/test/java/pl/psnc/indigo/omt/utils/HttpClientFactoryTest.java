package pl.psnc.indigo.omt.utils;

import java.io.IOException;
import net.openid.appauth.AuthState;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by michalu on 21.03.17.
 */
@RunWith(MockitoJUnitRunner.class) public class HttpClientFactoryTest {
    String token = "example-token";

    @Mock AuthState authState;

    @Before public void setup() {
        Mockito.when(authState.getAccessToken()).thenReturn(token);
    }

    @Test public void httpclient_getClientByAutStateObject() throws Exception {
        assertNotNull(HttpClientFactory.getClient(authState));
    }

    @Test public void httpclient_getClientByAccessToken() throws Exception {
        assertNotNull(HttpClientFactory.getClient("example-token"));
    }

    @Test public void httpclient_getNonIAMClient() throws Exception {
        assertNotNull(HttpClientFactory.getNonIAMClient());
    }

    @Test public void httpclient_checkForToken() throws Exception {
        OkHttpClient client = HttpClientFactory.getClient(token);
        Request request = new Request.Builder().url("http://example.org").build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {

        }
        Request after = response.request();
        assertTrue(after.headers().get("Authorization").equals("Bearer " + token));
    }

    @Test public void httpclient_checkForTokenWithAuthStateObject() throws Exception {
        OkHttpClient client = HttpClientFactory.getClient(authState);
        Request request = new Request.Builder().url("http://example.org").build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {

        }
        Request after = response.request();
        assertTrue(after.headers().get("Authorization").equals("Bearer " + token));
    }
}