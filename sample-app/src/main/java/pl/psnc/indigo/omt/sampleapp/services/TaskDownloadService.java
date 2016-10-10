package pl.psnc.indigo.omt.sampleapp.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;
import pl.psnc.indigo.omt.sampleapp.helpers.MessageEvent;

public class TaskDownloadService extends JobService {
    private static final String TAG = "SyncService";
    private JobParameters mParams;
    private DoItTask mTask;

    @Override public boolean onStartJob(JobParameters params) {
        // We don't do any real 'work' in this sample app. All we'll
        // do is track which jobs have landed on our service, and
        // update the UI accordingly.
        mParams = params;
        mTask = new DoItTask();
        mTask.execute();
        return true;
    }

    @Override public boolean onStopJob(JobParameters params) {
        // Stop tracking these job parameters, as we've 'finished' executing.
        if (mTask != null) mTask.cancel(true);
        Log.i(TAG, "on stop job: " + params.getJobId());
        return true;
    }

    private class DoItTask extends AsyncTask<Void, Void, Void> {
        @Override protected void onPostExecute(Void aVoid) {
            Log.d("DoItTask", "Clean up the task here and call jobFinished...");
            jobFinished(mParams, false);
            super.onPostExecute(aVoid);
        }

        @Override protected Void doInBackground(Void... params) {
            Log.d("DoItTask", "Working here...");
            EventBus.getDefault().post(new MessageEvent("Job started"));
            return null;
        }
    }
}