package com.wgu.zbentz2.semestertracker.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Assessment;
import com.wgu.zbentz2.semestertracker.database.entities.Course;
import com.wgu.zbentz2.semestertracker.database.entities.Term;
import com.wgu.zbentz2.semestertracker.database.viewmodels.AssessmentViewModel;
import com.wgu.zbentz2.semestertracker.database.viewmodels.CourseViewModel;
import com.wgu.zbentz2.semestertracker.database.viewmodels.TermViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CourseDetail extends AppCompatActivity {

    private Calendar startCal;
    private Calendar endCal;
    private EditText courseName;
    private EditText courseStartDate;
    private EditText courseEndDate;
    private EditText courseMentorName;
    private EditText courseMentorPhone;
    private EditText courseMentorEmail;
    private Spinner courseStatus;
    private CheckBox courseNotifications;
    private Spinner termsDropdown;
    private ListView courseAssessmentList;

    private AssessmentViewModel assessmentViewModel;
    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;
    private Course course;
    private Term term;

    private String action;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instantiate class variables
        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();
        courseName = findViewById(R.id.edit_course_name);
        courseStartDate = findViewById(R.id.edit_course_start_date);
        courseEndDate = findViewById(R.id.edit_course_end_date);
        courseMentorName = findViewById(R.id.edit_course_mentor_name);
        courseMentorPhone = findViewById(R.id.edit_course_mentor_phone);
        courseMentorEmail = findViewById(R.id.edit_course_mentor_email);
        courseStatus = findViewById(R.id.edit_course_status_dropdown);
        courseNotifications = findViewById(R.id.edit_course_notifications);
        termsDropdown = findViewById(R.id.edit_course_term_dropdown);
        courseAssessmentList = findViewById(R.id.edit_course_assessments_list);
        courseAssessmentList.setEmptyView(findViewById(R.id.edit_course_empty_assessments_list));

        // Instantiate database view models
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);

        // Populate the view with data if necessary
        initData();

        // Set up and populate the spinner
        setupSpinner();

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {

        if (action.equals(Intent.ACTION_EDIT)) {

            getMenuInflater().inflate(R.menu.delete_only_options_menu, menu);

        }

        return true;

    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                finishEditing();
                break;

            case R.id.delete_only_menu_delete:
                deleteCourse();
                break;

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
            //courseStartDate.setText(course.getStart_date());
            //courseEndDate.setText(course.getEnd_date());
            courseMentorName.setText(course.getMentor_name());
            courseMentorPhone.setText(course.getMentor_phone());
            courseMentorEmail.setText(course.getMentor_email());
            courseNotifications.setChecked(course.getNotifications() > 0);

            switch (course.getStatus()) {

                case "In Progress":
                    courseStatus.setSelection(0, true);
                    break;

                case "Completed":
                    courseStatus.setSelection(1, true);
                    break;

                case "Dropped":
                    courseStatus.setSelection(2, true);
                    break;

                case "Plan to Take":
                    courseStatus.setSelection(3, true);
                    break;

            }

            try {

                startCal.setTime(dateFormat.parse(course.getStart_date()));
                endCal.setTime(dateFormat.parse(course.getEnd_date()));

            } catch (ParseException e) {

                e.printStackTrace();

            }

            populateListView(course.getId());

            this.setTitle("Edit Course");

        } else {

            action = Intent.ACTION_INSERT;

            this.setTitle("New Course");

        }

        setupCalendar(courseStartDate, startCal);
        setupCalendar(courseEndDate, endCal);

    }

    private void populateListView(long course_id) {

        final ArrayAdapter<Assessment> listAdapter = new ArrayAdapter<>(
            CourseDetail.this,
            R.layout.assessment_list_item
        );

        assessmentViewModel.getAllAssessmentsForCourse(course_id).observe(
            this,
            new Observer<List<Assessment>>() {
                @Override public void onChanged(final List<Assessment> assessments) {

                    listAdapter.clear();
                    listAdapter.addAll(assessments);
                    listAdapter.notifyDataSetChanged();
                    courseAssessmentList.setAdapter(listAdapter);

                }
            }
        );

        courseAssessmentList.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Assessment selectedAssessment = (Assessment) courseAssessmentList.getItemAtPosition(position);
                    Intent intent = new Intent(CourseDetail.this, AssessmentDetail.class);
                    intent.putExtra("Assessment", selectedAssessment);
                    startActivity(intent);

                }
            }
        );
    }

    private void setupCalendar(final EditText field, final Calendar calendar) {

        // Set the initial value.
        field.setText(dateFormat.format(calendar.getTime()));

        final DatePickerDialog.OnDateSetListener fieldDate = new DatePickerDialog.OnDateSetListener() {
            @Override public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // Update the field value on change.
                field.setText(dateFormat.format(calendar.getTime()));
            }
        };

        field.setOnClickListener(
            new View.OnClickListener() {
                @Override public void onClick(View v) {
                    new DatePickerDialog(
                        CourseDetail.this,
                        fieldDate,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show();
                }
            }
        );
    }

    private void finishEditing() {

        int course_notifications = courseNotifications.isChecked() ? 1 : 0;
        String course_name = courseName.getText().toString();
        String course_start_date  = courseStartDate.getText().toString();
        String course_end_date = courseEndDate.getText().toString();
        String course_mentor_name = courseMentorName.getText().toString();
        String course_mentor_phone  = courseMentorPhone.getText().toString();
        String course_mentor_email = courseMentorEmail.getText().toString();
        String course_status = courseStatus.getSelectedItem().toString();

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

                    courseViewModel.insert(course);

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

                case Intent.ACTION_DELETE:

                    courseViewModel.delete(course);

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

    private void deleteCourse() {

        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int button) {

                // Do nothing if they click NO
                if (button == DialogInterface.BUTTON_POSITIVE) {

                    action = Intent.ACTION_DELETE;
                    finishEditing();

                }

            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to delete this course and its associated assessments and notes?")
            .setPositiveButton(
                getString(android.R.string.yes),
                dialogListener
            )
            .setNegativeButton(
                getString(android.R.string.no),
                dialogListener
            )
            .show();

    }
}
