package pl.psnc.indigo.omt.root.remote;

import pl.psnc.indigo.omt.api.model.Root;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by michalu on 12.01.17.
 */

public interface RetrofitRootAPI {
    /*
        This resource return the versions available in the server with the link to access them.
     */
    @GET("/") Call<Root> getRoot();
}
