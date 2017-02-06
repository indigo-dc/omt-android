package pl.psnc.indigo.omt.api.model.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import pl.psnc.indigo.omt.api.model.Application;

/**
 * Created by michalu on 06.02.17.
 */

public class ApplicationsWrapper {
    @Expose @SerializedName("applications") private List<Application> mApplications;

    public List<Application> getApplications() {
        return this.mApplications;
    }
}
