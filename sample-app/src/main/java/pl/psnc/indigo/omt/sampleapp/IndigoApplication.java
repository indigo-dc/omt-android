package pl.psnc.indigo.omt.sampleapp;

import android.app.Application;

import pl.psnc.indigo.omt.Indigo;

/**
 * Created by michalu on 24.03.16.
 */
public class IndigoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Indigo.init("http://10.0.3.2:8888", "USER", "API_TOKEN");
    }
}