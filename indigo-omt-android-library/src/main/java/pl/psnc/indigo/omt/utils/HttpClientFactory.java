package pl.psnc.indigo.omt.utils;

import java.io.IOException;
import net.openid.appauth.AuthState;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by michalu on 13.01.17.
 */

public class HttpClientFactory {
    public static OkHttpClient getClient(AuthState authState) {
        final String accessToken = authState.getAccessToken();
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request.newBuilder()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/vnd.indigo-datacloud.apiserver+json")
                    .build();
                Response response = chain.proceed(request);
                return response;
            }
        }).build();
    }

    public static OkHttpClient getClient(final String accessToken) {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request.newBuilder()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/vnd.indigo-datacloud.apiserver+json")
                    .build();
                Response response = chain.proceed(request);
                return response;
            }
        }).build();
    }

    public static OkHttpClient getNonIAMClient() {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request.newBuilder()
                    .addHeader("Authorization", "Bearer {}")
                    .addHeader("Content-Type", "application/vnd.indigo-datacloud.apiserver+json")
                    .build();
                Response response = chain.proceed(request);
                return response;
            }
        }).build();
    }
}
