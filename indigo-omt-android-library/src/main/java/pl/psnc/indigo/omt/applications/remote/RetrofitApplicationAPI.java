package pl.psnc.indigo.omt.applications.remote;

import pl.psnc.indigo.omt.api.model.json.ApplicationsWrapper;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by michalu on 06.02.17.
 */

public interface RetrofitApplicationAPI {
    @GET("applications") Call<ApplicationsWrapper> getApplications();
}
