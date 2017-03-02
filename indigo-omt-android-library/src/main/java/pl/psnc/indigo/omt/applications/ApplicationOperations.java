package pl.psnc.indigo.omt.applications;

import java.util.List;
import pl.psnc.indigo.omt.api.model.Application;

/**
 * Created by michalu on 06.02.17.
 */

public interface ApplicationOperations {
    List<Application> getApplications();

    Application getApplication(int appId);

    Application getApplication(String name);
}
