package pl.psnc.indigo.omt.callbacks;

import pl.psnc.indigo.omt.api.model.Application;

public interface ApplicationByNameCallback extends IndigoCallback {
    void onSuccess(Application result);
}