package com.wgu.zbentz2.semestertracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Assessment;
import com.wgu.zbentz2.semestertracker.database.repositories.AssessmentRepository;

public class AssessmentDetail extends AppCompatActivity {

    private EditText assessmentName;
    private EditText assessmentType;
    private EditText assessmentDueDate;
    private CheckBox assessmentNotifications;

    private Assessment assessment;
    private String action;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assessmentName = findViewById(R.id.edit_assessment_name);
        assessmentType = findViewById(R.id.edit_assessment_type);
        assessmentDueDate = findViewById(R.id.edit_assessment_due_date);
        assessmentNotifications = findViewById(R.id.edit_assessment_notifications);


        // Get the intent from the previous view and set config based on it.
        Intent intent = getIntent();
        assessment = (Assessment) intent.getSerializableExtra("Assessment");

        if (assessment != null) {

            action = Intent.ACTION_EDIT;

            boolean is_checked = assessment.getNotifications() > 0;

            assessmentName.setText(assessment.getName());
            assessmentType.setText(assessment.getType());
            assessmentDueDate.setText(assessment.getDue_date());
            assessmentNotifications.setChecked(is_checked);

            this.setTitle("Edit Assessment");

        } else {

            action = Intent.ACTION_INSERT;

            this.setTitle("New Assessment");

        }

    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Save when the user uses the up button.

        if (item.getItemId() == android.R.id.home) {

            finishEditing();

        }

        return true;

    }

    @Override public void onBackPressed() {
        // Save when the user uses the devices back button.

        finishEditing();

    }

    private void finishEditing() {

        AssessmentRepository assessmentRepository = new AssessmentRepository(getApplication());

        int assessment_notifcations = assessmentNotifications.isChecked() ? 1 : 0;

        String assessment_name = assessmentName.getText().toString();
        String assessment_type = assessmentType.getText().toString();
        String assessment_due_date = assessmentDueDate.getText().toString();


        if (assessment_name.length() > 0 &&
            assessment_type.length() > 0 &&
            assessment_due_date.length() > 0) {

            switch (action) {

                case Intent.ACTION_INSERT:

                    assessment = new Assessment(
                        1,
                        assessment_name,
                        assessment_type,
                        assessment_due_date,
                        assessment_notifcations
                    );

                    long temp = assessmentRepository.insert(assessment);
                    break;


                case Intent.ACTION_EDIT:

                    assessment.setName(assessment_name);
                    assessment.setType(assessment_type);
                    assessment.setDue_date(assessment_due_date);
                    assessment.setNotifications(assessment_notifcations);

                    assessmentRepository.update(assessment);
                    break;


            }
        }

        finish();

    }

}
