package pl.psnc.indigo.omt.sampleapp.views;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import net.openid.appauth.AuthState;
import pl.psnc.indigo.omt.Indigo;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.callbacks.TaskDeleteCallback;
import pl.psnc.indigo.omt.callbacks.TaskDetailsCallback;
import pl.psnc.indigo.omt.iam.IAMHelper;
import pl.psnc.indigo.omt.sampleapp.R;
import pl.psnc.indigo.omt.sampleapp.fragments.FragmentWithListAndCaption;

/**
 * Created by michalu on 13.09.16.
 */
public class TaskDetailsActivity extends IndigoActivity {
    private TextView mTaskTitle, mTaskDescription, mLastUpdate;
    private AppCompatButton mStatusButton;
    private Button mDeleteTaskButton;
    private Task mTask;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mTaskTitle = (TextView) findViewById(R.id.task_details_title);
        mStatusButton = (AppCompatButton) findViewById(R.id.task_details_status);
        mTaskDescription = (TextView) findViewById(R.id.task_details_description);
        mDeleteTaskButton = (Button) findViewById(R.id.activity_details_remove_task);

        mDeleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                AuthState authState = IAMHelper.readAuthState(getApplicationContext());
                Indigo.deleteTask(mTask, authState, new TaskDeleteCallback() {
                    @Override public void onSuccess(boolean result) {
                        finish();
                    }

                    @Override public void onError(Exception exception) {
                        Toast.makeText(getApplicationContext(), "Cannot remove this task",
                            Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Intent i = getIntent();
        if (i != null) {
            mTask = i.getParcelableExtra("task");
            if (mTask != null) {
                AuthState authState = IAMHelper.readAuthState(getApplicationContext());
                Indigo.getTask(mTask, authState, new TaskDetailsCallback() {
                    @Override public void onSuccess(Task result) {
                        mTaskTitle.setText("Task #" + result.getId());
                        mStatusButton.setText(result.getStatus());
                        mTaskDescription.setText(
                            (result.getDescription() == null || result.getDescription().isEmpty())
                                ? "No description" : result.getDescription());
                        FragmentWithListAndCaption f1 =
                            FragmentWithListAndCaption.create("Input files",
                                result.getInputFiles());
                        FragmentWithListAndCaption f2 =
                            FragmentWithListAndCaption.create("Output files",
                                result.getOutputFiles());
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.task_input_files_fragment, f1);
                        ft.replace(R.id.task_output_files_fragment, f2);
                        ft.commit();
                    }

                    @Override public void onError(Exception exception) {
                        mTaskTitle.setText("Task #" + mTask.getId());
                        mStatusButton.setText(mTask.getStatus());
                        mTaskDescription.setText(
                            (mTask.getDescription() == null || mTask.getDescription().isEmpty())
                                ? "No description" : mTask.getDescription());
                        FragmentWithListAndCaption f1 =
                            FragmentWithListAndCaption.create("Input files", mTask.getInputFiles());
                        FragmentWithListAndCaption f2 =
                            FragmentWithListAndCaption.create("Output files",
                                mTask.getOutputFiles());
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.task_input_files_fragment, f1);
                        ft.replace(R.id.task_output_files_fragment, f2);
                        ft.commit();
                    }
                });
            }
        }
    }
}
