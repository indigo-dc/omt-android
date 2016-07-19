package pl.psnc.indigo.omt.callbacks;

import pl.psnc.indigo.omt.api.model.Task;

public interface TaskDetailsCallback extends IndigoCallback {
    void onSuccess(Task result);
}