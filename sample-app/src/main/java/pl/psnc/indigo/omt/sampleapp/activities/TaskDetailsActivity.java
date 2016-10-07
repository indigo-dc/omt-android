package pl.psnc.indigo.omt.sampleapp.activities;

import android.app.FragmentTransaction;
import android.app.job.JobParameters;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.widget.TextView;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.sampleapp.R;
import pl.psnc.indigo.omt.sampleapp.fragments.FragmentWithListAndCaption;

/**
 * Created by michalu on 13.09.16.
 */
public class TaskDetailsActivity extends IndigoActivity {
    private TextView mTaskTitle, mTaskDescription, mLastUpdate;
    private AppCompatButton mStatusButton;
    private Task mTask;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mTaskTitle = (TextView) findViewById(R.id.task_details_title);
        mStatusButton = (AppCompatButton) findViewById(R.id.task_details_status);
        mTaskDescription = (TextView) findViewById(R.id.task_details_description);

        Intent i = getIntent();
        if (i != null) {
            mTask = i.getParcelableExtra("task");
            if (mTask != null) {
                mTaskTitle.setText("Task #" + mTask.getId());
                mStatusButton.setText(mTask.getStatus());
                mTaskDescription.setText(
                    (mTask.getDescription() == null || mTask.getDescription().isEmpty())
                        ? "No description" : mTask.getDescription());
                FragmentWithListAndCaption f1 = FragmentWithListAndCaption.create("Input files", mTask.getInputFiles());
                FragmentWithListAndCaption f2 = FragmentWithListAndCaption.create("Output files", mTask.getOutputFiles());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.task_input_files_fragment, f1);
                ft.replace(R.id.task_output_files_fragment, f2);
                ft.commit();
            }
        }
    }
}
