package pl.psnc.indigo.omt.sampleapp;

import android.app.Activity;
import java.io.BufferedInputStream;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static okhttp3.Protocol.HTTP_1_1;

final class MockInterceptor implements Interceptor {

    public MockInterceptor(String file, Activity activity) {
        try {
            responseBody = ResponseBody.create(MediaType.parse("application/json"),
                readResponseFromAssets(activity, file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Response.Builder responseBuilder = new Response.Builder();
    ResponseBody responseBody;

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        return responseBuilder.request(request)
            .body(responseBody)
            .code(200)
            .protocol(HTTP_1_1)
            .build();
    }

    private String readResponseFromAssets(Activity activity, String name) throws IOException {
        BufferedInputStream is = new BufferedInputStream(activity.getAssets().open(name));
        byte[] contents = new byte[1024];

        int bytesRead = 0;
        StringBuilder sb = new StringBuilder();
        while ((bytesRead = is.read(contents)) != -1) {
            sb.append(new String(contents, 0, bytesRead));
        }
        return sb.toString();
    }
}