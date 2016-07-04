package pl.psnc.indigo.omt.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.psnc.indigo.omt.api.model.Root;
import pl.psnc.indigo.omt.exceptions.WrongApiUrlException;

/**
 * Created by michalu on 21.03.16.
 */
public class RootApi extends Api {
    private static HashMap<String, RootApi> sRootApiMap;
    private Root mRoot;

    private RootApi(String httpAddress) throws WrongApiUrlException {
        super(httpAddress);
        try {
            mRoot = getRoot();
        } catch (IOException e) {
            throw new WrongApiUrlException();
        }
    }

    public static RootApi getRootForAddress(String httpAddress) throws WrongApiUrlException {
        if (sRootApiMap == null) {
            sRootApiMap = new HashMap<String, RootApi>();
        }
        if (sRootApiMap.containsKey(httpAddress)) {
            return sRootApiMap.get(httpAddress);
        } else {
            RootApi newRoot = new RootApi(httpAddress);
            sRootApiMap.put(httpAddress, newRoot);
            return newRoot;
        }
    }

    public Root getRoot() throws IOException {
        OkHttpClient okHttp = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(mHttpAddress).build();

        Response response = okHttp.newCall(request).execute();
        Type rootType = new TypeToken<Root>() {
        }.getType();
        Root root = new Gson().fromJson(response.body().string(), rootType);
        return root;
    }

    public String getURLAsString() {
        return mHttpAddress + "/" + mRoot.getVersions().get(0).getId() + "/";
    }
}
