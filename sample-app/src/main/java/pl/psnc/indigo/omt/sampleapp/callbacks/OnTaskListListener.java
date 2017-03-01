package pl.psnc.indigo.omt.sampleapp.callbacks;

import java.util.List;
import pl.psnc.indigo.omt.api.model.Task;

/**
 * Created by michalu on 07.10.16.
 */

public interface OnTaskListListener {
    void onUpdate(List<Task> list);
}
