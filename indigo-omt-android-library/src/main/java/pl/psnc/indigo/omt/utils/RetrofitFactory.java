package pl.psnc.indigo.omt.utils;

import okhttp3.OkHttpClient;
import pl.psnc.indigo.omt.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by michalu on 30.11.16.
 */

public class RetrofitFactory {
    public static Retrofit getInstanceForRootAPI(OkHttpClient client) {
        return new Retrofit.Builder().baseUrl(BuildConfig.FGAPI_ADDRESS)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    public static Retrofit getInstance(String url, OkHttpClient client) {
        return new Retrofit.Builder().baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
}
