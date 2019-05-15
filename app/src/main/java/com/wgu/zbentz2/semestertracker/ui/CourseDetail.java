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
import com.wgu.zbentz2.semestertracker.database.entities.Course;
import com.wgu.zbentz2.semestertracker.database.entities.Term;
import com.wgu.zbentz2.semestertracker.database.viewmodels.CourseViewModel;
import com.wgu.zbentz2.semestertracker.database.viewmodels.TermViewModel;

import java.util.List;


public class CourseDetail extends AppCompatActivity {

    private EditText courseName;
    private EditText courseStartDate;
    private EditText courseEndDate;
    private EditText courseMentorName;
    private EditText courseMentorPhone;
    private EditText courseMentorEmail;
    private EditText courseStatus;
    private CheckBox courseNotifications;
    private Spinner termsDropdown;

    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;
    private Course course;
    private Term term;

    private String action;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instantiate class variables
        courseName = findViewById(R.id.edit_course_name);
        courseStartDate = findViewById(R.id.edit_course_start_date);
        courseEndDate = findViewById(R.id.edit_course_end_date);
        courseMentorName = findViewById(R.id.edit_course_mentor_name);
        courseMentorPhone = findViewById(R.id.edit_course_mentor_phone);
        courseMentorEmail = findViewById(R.id.edit_course_mentor_email);
        courseStatus = findViewById(R.id.edit_course_status);
        courseNotifications = findViewById(R.id.edit_course_notifications);
        termsDropdown = findViewById(R.id.edit_course_term_dropdown);

        // Instantiate database view models.
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);

        // Populate the view with data if necessary.
        initData();

        // Set up and populate the spinner.
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
        course = (Course) intent.getSerializableExtra("Course");

        if (course != null) {

            action = Intent.ACTION_EDIT;

            courseName.setText(course.getName());
            courseStartDate.setText(course.getStart_date());
            courseEndDate.setText(course.getEnd_date());
            courseMentorName.setText(course.getMentor_name());
            courseMentorPhone.setText(course.getMentor_phone());
            courseMentorEmail.setText(course.getMentor_email());
            courseStatus.setText(course.getStatus());
            courseNotifications.setChecked(course.getNotifications() > 0);

            this.setTitle("Edit Course");

        } else {

            action = Intent.ACTION_INSERT;

            this.setTitle("New Course");

        }

    }

    private void finishEditing() {

        int course_notifications = courseNotifications.isChecked() ? 1 : 0;
        String course_name = courseName.getText().toString();
        String course_start_date  = courseStartDate.getText().toString();
        String course_end_date = courseEndDate.getText().toString();
        String course_mentor_name = courseMentorName.getText().toString();
        String course_mentor_phone  = courseMentorPhone.getText().toString();
        String course_mentor_email = courseMentorEmail.getText().toString();
        String course_status = courseStatus.getText().toString();

        Term selectedTerm = (Term) termsDropdown.getSelectedItem();

        if (course_name.length() > 0 &&
            course_start_date.length() > 0 &&
            course_end_date.length() > 0 &&
            course_mentor_name.length() > 0 &&
            course_mentor_phone.length() > 0 &&
            course_mentor_email.length() > 0 &&
            course_status.length() > 0 &&
            selectedTerm != null) {

            switch (action) {

                case Intent.ACTION_INSERT:

                    course = new Course(
                        selectedTerm.getId(),
                        course_name,
                        course_start_date,
                        course_end_date,
                        course_mentor_name,
                        course_mentor_phone,
                        course_mentor_email,
                        course_status,
                        course_notifications
                    );

                    long temp = courseViewModel.insert(course);
                    break;


                case Intent.ACTION_EDIT:

                    course.setTerm_id(selectedTerm.getId());
                    course.setName(course_name);
                    course.setStart_date(course_start_date);
                    course.setEnd_date(course_end_date);
                    course.setMentor_name(course_mentor_name);
                    course.setMentor_phone(course_mentor_phone);
                    course.setMentor_email(course_mentor_email);
                    course.setStatus(course_status);
                    course.setNotifications(course_notifications);

                    courseViewModel.update(course);
                    break;


            }
        }

        finish();

    }

    private void setupSpinner() {

        final ArrayAdapter<Term> spinnerAdapter = new ArrayAdapter<>(
            CourseDetail.this,
            android.R.layout.simple_spinner_item);

        termViewModel.getAllTerms().observe(
            this,
            new Observer<List<Term>>() {
                @Override public void onChanged(List<Term> terms) {

                    spinnerAdapter.addAll(terms);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAdapter.notifyDataSetChanged();

                    termsDropdown.setAdapter(spinnerAdapter);

                    // populate the spinner dropdown in this scope.
                    if(course != null) {

                        term = termViewModel.getTerm(course.getTerm_id());
                        int pos = spinnerAdapter.getPosition(term);
                        termsDropdown.setSelection(pos, true);
                    }

                }
            }
        );

    }

}
