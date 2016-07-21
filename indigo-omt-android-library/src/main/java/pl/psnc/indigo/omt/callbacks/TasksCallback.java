package pl.psnc.indigo.omt.callbacks;

import java.util.List;
import pl.psnc.indigo.omt.api.model.Task;

public interface TasksCallback extends IndigoCallback {
    void onSuccess(List<Task> result);
}