package pl.psnc.indigo.omt.api;

/**
 * Created by michalu on 21.03.16.
 */
public abstract class Api {
    public static final String DEFAULT_INDIGO_URL = "http://10.0.3.2:8888";
    protected String mHttpAddress = null;

    public Api(String httpAddress) {
        this.mHttpAddress = (httpAddress != null) ? httpAddress : DEFAULT_INDIGO_URL;
    }
}
