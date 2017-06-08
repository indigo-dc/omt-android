package pl.psnc.indigo.omt.utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by michalu on 30.11.16.
 */

public class RetrofitFactory {
  public static Retrofit getInstanceForRootAPI(OkHttpClient client) throws Exception {
    return new Retrofit.Builder().baseUrl(FutureGatewayHelper.getServerAddress())
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static Retrofit getInstance(String url, OkHttpClient client) throws Exception {
    return new Retrofit.Builder().baseUrl(url)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }
}
