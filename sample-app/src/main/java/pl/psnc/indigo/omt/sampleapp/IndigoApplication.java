package pl.psnc.indigo.omt.sampleapp;

import android.app.Application;
import java.net.URISyntaxException;
import pl.psnc.indigo.omt.Indigo;

/**
 * Created by michalu on 24.03.16.
 */
public class IndigoApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        try {
            Indigo.init(this, "test", "http://rhus-147.man.poznan.pl/apis/");
        } catch (URISyntaxException e) {
            throw new RuntimeException("Bad server address. It should be http(s)://ip(:port)/");
        }
    }
}
