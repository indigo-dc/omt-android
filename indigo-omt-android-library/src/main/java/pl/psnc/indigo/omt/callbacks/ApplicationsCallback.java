package pl.psnc.indigo.omt.callbacks;

import java.util.List;
import pl.psnc.indigo.omt.api.model.Application;

public interface ApplicationsCallback extends IndigoCallback {
    void onSuccess(List<Application> result);
}