package pl.psnc.indigo.omt.sampleapp.receivers;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import pl.psnc.indigo.omt.sampleapp.helpers.Actions;
import pl.psnc.indigo.omt.sampleapp.services.TaskDownloadAlarmManagerService;
import pl.psnc.indigo.omt.sampleapp.services.TaskDownloadService;

/**
 * Created by michalu on 07.10.16.
 */

public class TaskBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "TaskBroadcastReceiver";

    @Override public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case Actions.TASKS_UPDATE_LIST_ACTION:
                Toast.makeText(context, "Receiver is working", Toast.LENGTH_SHORT).show();
                ComponentName serviceComponent =
                    new ComponentName(context.getApplicationContext(), TaskDownloadService.class);
                JobInfo.Builder builder = new JobInfo.Builder(2, serviceComponent);
                builder.setRequiresCharging(false);
                builder.setRequiresDeviceIdle(false);
                builder.setPeriodic(10 * 1000);
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);

                JobScheduler tm = (JobScheduler) context.getApplicationContext()
                    .getSystemService(Context.JOB_SCHEDULER_SERVICE);
                int result = tm.schedule(builder.build());
                if (result == JobScheduler.RESULT_SUCCESS) {
                    Log.i(TAG, "Job scheduled successfully!");
                } else {
                    Log.i(TAG, "Job results: " + result);
                }
                break;
            case Actions.TASKS_UPDATE_LIST_ACTION_ALARM_MANAGER:
                Toast.makeText(context, "Intent received from AlarmManager", Toast.LENGTH_SHORT)
                    .show();
                Intent intentService = new Intent(context, TaskDownloadAlarmManagerService.class);
                context.startService(intentService);
                break;
        }
    }
}
