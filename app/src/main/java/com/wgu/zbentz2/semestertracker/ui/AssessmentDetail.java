package com.wgu.zbentz2.semestertracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Assessment;
import com.wgu.zbentz2.semestertracker.database.entities.Course;
import com.wgu.zbentz2.semestertracker.database.viewmodels.AssessmentViewModel;
import com.wgu.zbentz2.semestertracker.database.viewmodels.CourseViewModel;

import java.util.List;

public class AssessmentDetail extends AppCompatActivity {

    private EditText assessmentName;
    private EditText assessmentType;
    private EditText assessmentDueDate;
    private CheckBox assessmentNotifications;
    private Spinner coursesDropdown;

    private AssessmentViewModel assessmentViewModel;
    private CourseViewModel courseViewModel;
    private Assessment assessment;
    private Course course;

    private String action;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instantiate class variables
        assessmentName = findViewById(R.id.edit_assessment_name);
        assessmentType = findViewById(R.id.edit_assessment_type);
        assessmentDueDate = findViewById(R.id.edit_assessment_due_date);
        assessmentNotifications = findViewById(R.id.edit_assessment_notifications);
        coursesDropdown = findViewById(R.id.edit_assessment_course_dropdown);

        // Instantiate database view models
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

        // Populate the view with data if necessary
        initData();

        // Set up and populate the spinner
        setupSpinner();

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

    private void initData() {

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

    private void finishEditing() {

        int assessment_notifications = assessmentNotifications.isChecked() ? 1 : 0;
        String assessment_name = assessmentName.getText().toString();
        String assessment_type = assessmentType.getText().toString();
        String assessment_due_date = assessmentDueDate.getText().toString();

        Course selectedCourse = (Course) coursesDropdown.getSelectedItem();

        if (assessment_name.length() > 0 &&
            assessment_type.length() > 0 &&
            assessment_due_date.length() > 0 &&
            selectedCourse != null) {

            switch (action) {

                case Intent.ACTION_INSERT:

                    assessment = new Assessment(
                        selectedCourse.getId(),
                        assessment_name,
                        assessment_type,
                        assessment_due_date,
                        assessment_notifications
                    );

                    assessmentViewModel.insert(assessment);
                    break;


                case Intent.ACTION_EDIT:

                    assessment.setCourse_id(selectedCourse.getId());
                    assessment.setName(assessment_name);
                    assessment.setType(assessment_type);
                    assessment.setDue_date(assessment_due_date);
                    assessment.setNotifications(assessment_notifications);

                    assessmentViewModel.update(assessment);
                    break;

            }

        }

        finish();

    }

    private void setupSpinner() {

        final ArrayAdapter<Course> spinnerAdapter = new ArrayAdapter<>(
            AssessmentDetail.this,
            android.R.layout.simple_spinner_item);

        courseViewModel.getAllCourses().observe(
            this,
            new Observer<List<Course>>() {
                @Override public void onChanged(List<Course> courses) {

                    spinnerAdapter.addAll(courses);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAdapter.notifyDataSetChanged();

                    coursesDropdown.setAdapter(spinnerAdapter);

                    // populate the spinner dropdown in this scope.
                    if(assessment != null) {

                        course = courseViewModel.getCourse(assessment.getCourse_id());
                        int pos = spinnerAdapter.getPosition(course);
                        coursesDropdown.setSelection(pos, true);
                    }
                }
            }
        );
    }
}
