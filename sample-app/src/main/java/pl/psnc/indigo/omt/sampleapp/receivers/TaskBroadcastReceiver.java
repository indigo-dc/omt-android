package pl.psnc.indigo.omt.sampleapp.receivers;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import pl.psnc.indigo.omt.sampleapp.helpers.Actions;
import pl.psnc.indigo.omt.sampleapp.services.TaskDownloadService;

/**
 * Created by michalu on 07.10.16.
 */

public class TaskBroadcastReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case Actions.TASKS_UPDATE_LIST_ACTION:
                Toast.makeText(context, "Receiver is working", Toast.LENGTH_SHORT).show();
                ComponentName serviceComponent =
                    new ComponentName(context, TaskDownloadService.class);
                JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
                builder.setPeriodic(5000);
                builder.setRequiredNetworkType(
                    JobInfo.NETWORK_TYPE_ANY); // require unmetered network
                JobScheduler tm =
                    (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                tm.schedule(builder.build());
                break;
        }
    }
}
