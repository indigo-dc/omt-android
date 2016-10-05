package pl.psnc.indigo.omt.api;

import android.net.Uri;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.psnc.indigo.omt.BuildConfig;
import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.api.model.Root;
import pl.psnc.indigo.omt.exceptions.IndigoException;

/**
 * Created by michalu on 14.07.16.
 */
public class ApiHelper {
    public static final Uri DEFAULT_ADDRESS = Uri.parse(BuildConfig.FGAPI_ADDRESS);
    public static final Uri LOCALHOST_ADDRESS = Uri.parse("http://localhost:8888");

    private OkHttpClient mClient;
    private Uri mRootApiAddress;

    public ApiHelper(OkHttpClient client) {
        mClient = client;
        mRootApiAddress = DEFAULT_ADDRESS;
    }

    public ApiHelper(OkHttpClient client, Uri httpAddress) {
        mClient = client;
        mRootApiAddress = httpAddress;
    }

    public ApiHelper() {
        this.mClient = createClient();
        mRootApiAddress = DEFAULT_ADDRESS;
    }

    public ApiHelper(Uri httpAddress) {
        this.mClient = createClient();
        mRootApiAddress = httpAddress;
    }

    protected Root getRootForUri(Uri baseUri) throws IndigoException {
        Root root = null;
        try {
            Request request = prepareRequest().url(baseUri.toString()).build();
            Response response = mClient.newCall(request).execute();
            Type rootType = new TypeToken<Root>() {
            }.getType();
            root = new Gson().fromJson(response.body().string(), rootType);
        } catch (IOException e) {
            String message = "Failed to connect to FG";
            throw new IndigoException(message);
        } catch (IllegalArgumentException e) {
            String message = "Failed to connect to FG";
            throw new IndigoException(message);
        }
        return root;
    }

    private OkHttpClient createClient() {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request newRequest = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + Indigo.getApiToken())
                    .addHeader("Content-Type", "application/json")
                    .build();
                Response response = chain.proceed(newRequest);
                return response;
            }
        }).build();
    }

    public Uri getFullUri(String endpoint) throws IndigoException {
        Root root = getRootForUri(mRootApiAddress);
        String version = root.getVersions().get(0).getId();
        Uri.Builder builder = new Uri.Builder();
        return builder.encodedPath(mRootApiAddress.toString())
            .appendEncodedPath(version)
            .appendEncodedPath(endpoint)
            .build();
    }

    public Uri getFullUri(String endpoint, String[] pathParams, Map<String, String> queryParams)
        throws IndigoException {
        Root root = getRootForUri(mRootApiAddress);
        String version = root.getVersions().get(0).getId();
        Uri.Builder builder = new Uri.Builder();
        builder.encodedPath(mRootApiAddress.toString())
            .appendEncodedPath(version)
            .appendEncodedPath(endpoint);
        if (pathParams != null && pathParams.length > 0) {
            for (String param : pathParams) {
                builder.appendPath(param);
            }
        }
        if (queryParams != null && !queryParams.isEmpty()) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    builder.appendQueryParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        return builder.build();
    }

    public Request.Builder prepareRequest() {
        return new Request.Builder();
    }

    public OkHttpClient getClient() {
        return mClient;
    }

    public Uri getRootApiAddress() {
        return mRootApiAddress;
    }

    public void setRootApiAddress(Uri rootApiAddress) {
        mRootApiAddress = rootApiAddress;
    }
}
