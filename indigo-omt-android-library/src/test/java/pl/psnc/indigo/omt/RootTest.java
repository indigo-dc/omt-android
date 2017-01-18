package pl.psnc.indigo.omt;

import junit.framework.Assert;
import okhttp3.OkHttpClient;
import org.junit.Test;
import pl.psnc.indigo.omt.exceptions.IndigoException;
import pl.psnc.indigo.omt.root.RootAPI;

/**
 * Created by michalu on 13.01.17.
 */

public class RootTest {
    @Test public void getRootSuccess() throws IndigoException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        RootAPI rootAPI = RootAPI.getInstance(client);
        String root = rootAPI.getRoot();
        Assert.assertNotNull(root);
    }
}
