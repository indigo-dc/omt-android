package pl.psnc.indigo.omt.callbacks;

import pl.psnc.indigo.omt.api.model.Application;

public interface ApplicationByIdCallback extends IndigoCallback {
    void onSuccess(Application result);
}