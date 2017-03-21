package pl.psnc.indigo.omt.utils;

import java.io.IOException;
import net.openid.appauth.AuthState;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.psnc.indigo.omt.BuildConfig;

/**
 * Created by michalu on 13.01.17.
 */

public class HttpClientFactory {
    public static OkHttpClient getClient(AuthState authState) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        final String accessToken = authState.getAccessToken();
        httpClient.addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", BuildConfig.FGAPI_CONTENT_TYPE);
                Request newRequest = requestBuilder.build();
                return chain.proceed(newRequest);
            }
        });
        return httpClient.build();
    }

    public static OkHttpClient getClient(final String accessToken) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", BuildConfig.FGAPI_CONTENT_TYPE);
                Request newRequest = requestBuilder.build();
                return chain.proceed(newRequest);
            }
        });
        return httpClient.build();
    }

    public static OkHttpClient getNonIAMClient() {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request.newBuilder()
                    .addHeader("Content-Type", BuildConfig.FGAPI_CONTENT_TYPE)
                    .build();
                Response response = chain.proceed(request);
                return response;
            }
        }).build();
    }
}
