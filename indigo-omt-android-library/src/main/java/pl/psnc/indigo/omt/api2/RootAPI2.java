package pl.psnc.indigo.omt.api2;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.psnc.indigo.omt.api.model.Root;
import pl.psnc.indigo.omt.exceptions.IndigoException;

/**
 * Created by michalu on 14.07.16.
 */
public class RootAPI2 {
    public static final Uri DEFAULT_ADDRESS = Uri.parse("http://90.147.74.77:8888");
    public static final Uri LOCALHOST_ADDRESS = Uri.parse("http://10.0.3.2:8888");

    private OkHttpClient mClient;

    public RootAPI2(OkHttpClient mClient) {
        this.mClient = mClient;
    }

    public RootAPI2() {
        this.mClient = createClient();
    }

    protected Root getRootForURI(Uri baseUri) throws IndigoException {
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

    public Request.Builder prepareRequest() {
        return new Request.Builder();
    }

    public OkHttpClient getClient() {
        return mClient;
    }

    private OkHttpClient createClient() {
        return new OkHttpClient.Builder().build();
    }

    public Uri getFullAddress(Uri baseUri, String endpoint) throws IndigoException {
        Root root = getRootForURI(baseUri);
        String version = root.getVersions().get(0).getId();
        Uri.Builder builder = new Uri.Builder();
        return builder
                .appendPath(baseUri.toString())
                .appendPath(version)
                .appendPath(endpoint)
                .build();
    }

    public Uri getFullAddress(Uri baseUri, String endpoint, Map<String, String> parameters) throws IndigoException {
        Root root = getRootForURI(baseUri);
        String version = root.getVersions().get(0).getId();
        Uri.Builder builder = new Uri.Builder();
        builder.appendPath(baseUri.toString())
                .appendPath(version)
                .appendPath(endpoint);
        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }
}
