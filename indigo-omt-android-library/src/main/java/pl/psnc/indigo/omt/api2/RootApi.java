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
public class RootApi {
    public static final Uri DEFAULT_ADDRESS = Uri.parse("http://90.147.74.77:8888");
    public static final Uri EMULATOR_LOCALHOST_ADDRESS = Uri.parse("http://10.0.2.2:8888");
    public static final Uri LOCALHOST_ADDRESS = Uri.parse("http://localhost:8888");

    private OkHttpClient mClient;
    private Uri mRootApiAddress;

    public RootApi(OkHttpClient client) {
        mClient = client;
        mRootApiAddress = DEFAULT_ADDRESS;
    }

    public RootApi(OkHttpClient client, Uri httpAddress) {
        mClient = client;
        mRootApiAddress = httpAddress;
    }

    public RootApi() {
        this.mClient = createClient();
        mRootApiAddress = DEFAULT_ADDRESS;
    }

    public RootApi(Uri httpAddress) {
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
        return new OkHttpClient.Builder().build();
    }

    public Uri getFullAddress(String endpoint) throws IndigoException {
        Root root = getRootForUri(mRootApiAddress);
        String version = root.getVersions().get(0).getId();
        Uri.Builder builder = new Uri.Builder();
        return builder.path(mRootApiAddress.toString())
            .appendPath(version)
            .appendPath(endpoint)
            .build();
    }

    public Uri getFullAddress(String endpoint, Map<String, String> parameters)
        throws IndigoException {
        Root root = getRootForUri(mRootApiAddress);
        String version = root.getVersions().get(0).getId();
        Uri.Builder builder = new Uri.Builder();
        builder.path(mRootApiAddress.toString()).appendPath(version).appendPath(endpoint);
        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
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
