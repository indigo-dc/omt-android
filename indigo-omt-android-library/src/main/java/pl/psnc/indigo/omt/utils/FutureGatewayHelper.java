package pl.psnc.indigo.omt.utils;

import java.net.URI;
import java.net.URISyntaxException;
import pl.psnc.indigo.omt.BuildConfig;

/**
 * Created by michalu on 19.01.17.
 */

public class FutureGatewayHelper {
    private static String sServerAddress;

    public static String getServerAddress() {
        if (sServerAddress == null) sServerAddress = BuildConfig.FGAPI_ADDRESS;
        return sServerAddress;
    }

    public static void setServerAddress(String serverAddress) throws URISyntaxException {
        if (serverAddress == null) {
            sServerAddress = BuildConfig.FGAPI_ADDRESS;
        } else {
            sServerAddress = serverAddress;
        }
        new URI(sServerAddress);
    }
}
