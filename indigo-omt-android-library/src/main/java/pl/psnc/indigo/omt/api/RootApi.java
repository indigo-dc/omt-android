package pl.psnc.indigo.omt.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import pl.psnc.indigo.omt.api.model.Root;
import pl.psnc.indigo.omt.exceptions.WrongApiUrlException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by michalu on 21.03.16.
 */
public class RootApi extends Api {
    private static HashMap<String, RootApi> rootMap;
    private Root wsRoot;

    private RootApi(String httpAddress) throws WrongApiUrlException {
        super(httpAddress);
        try {
            wsRoot = getRoot();
        } catch (Exception e) {
            throw new WrongApiUrlException();
        }
    }

    public static RootApi getRootForAddress(String httpAddress) throws WrongApiUrlException {
        if (rootMap == null) {
            rootMap = new HashMap<String, RootApi>();
        }
        if (rootMap.containsKey(httpAddress)) {
            return rootMap.get(httpAddress);
        } else {
            RootApi newRoot = new RootApi(httpAddress);
            rootMap.put(httpAddress, newRoot);
            return newRoot;
        }
    }

    public Root getRoot() throws Exception {
        OkHttpClient okHttp = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(httpAddress)
                .build();

        Response response = okHttp.newCall(request).execute();
        Type rootType = new TypeToken<Root>() {
        }.getType();
        Root root = new Gson().fromJson(response.body().string(), rootType);
        return root;
    }

    public String getURLAsString() {
        return httpAddress + "/" + wsRoot.getVersions().get(0).getId() + "/";
    }
}
