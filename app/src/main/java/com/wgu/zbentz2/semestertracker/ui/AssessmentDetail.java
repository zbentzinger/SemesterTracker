package com.wgu.zbentz2.semestertracker.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
import com.wgu.zbentz2.semestertracker.database.entities.Note;
import com.wgu.zbentz2.semestertracker.database.viewmodels.AssessmentViewModel;
import com.wgu.zbentz2.semestertracker.database.viewmodels.CourseViewModel;
import com.wgu.zbentz2.semestertracker.database.viewmodels.NoteViewModel;
import com.wgu.zbentz2.semestertracker.utils.UserInterfaceUtils;

import java.util.Calendar;
import java.util.List;

public class AssessmentDetail extends AppCompatActivity {

    private Calendar dueDateCal;
    private EditText assessmentName;
    private Spinner assessmentType;
    private EditText assessmentDueDate;
    private CheckBox assessmentNotifications;
    private Spinner coursesDropdown;
    private ListView notesList;

    private AssessmentViewModel assessmentViewModel;
    private CourseViewModel courseViewModel;
    private NoteViewModel noteViewModel;
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
        dueDateCal = Calendar.getInstance();
        assessmentName = findViewById(R.id.edit_assessment_name);
        assessmentType = findViewById(R.id.edit_assessment_type);
        assessmentDueDate = findViewById(R.id.edit_assessment_due_date);
        assessmentNotifications = findViewById(R.id.edit_assessment_notifications);
        coursesDropdown = findViewById(R.id.edit_assessment_course_dropdown);
        notesList = findViewById(R.id.edit_assessment_notes_list);
        notesList.setEmptyView(findViewById(R.id.edit_assessment_empty_notes));

        // Instantiate database view models
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

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
                deleteAssessment();
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
        assessment = (Assessment) intent.getSerializableExtra("Assessment");

        if (assessment != null) {

            action = Intent.ACTION_EDIT;

            boolean is_checked = assessment.getNotifications() > 0;

            assessmentName.setText(assessment.getName());
            assessmentNotifications.setChecked(is_checked);

            switch (assessment.getType()) {

                case "Objective":
                    assessmentType.setSelection(0, true);
                    break;

                case "Performance":
                    assessmentType.setSelection(1, true);
                    break;

            }

            dueDateCal.setTime(
                UserInterfaceUtils.parseDateString(assessment.getDue_date())
            );


            populateListView(assessment.getCourse_id());

            this.setTitle("Edit Assessment");

        } else {

            action = Intent.ACTION_INSERT;

            this.setTitle("New Assessment");

        }

        UserInterfaceUtils.setupCalendar(assessmentDueDate, dueDateCal, AssessmentDetail.this);

    }

    private void populateListView(long course_id) {

        final ArrayAdapter<Note> listAdapter = new ArrayAdapter<>(
            AssessmentDetail.this,
            R.layout.note_list_item
        );

        noteViewModel.getAllNotesForCourse(course_id).observe(
            this,
            new Observer<List<Note>>() {
                @Override public void onChanged(final List<Note> notes) {

                    listAdapter.clear();
                    listAdapter.addAll(notes);
                    listAdapter.notifyDataSetChanged();
                    notesList.setAdapter(listAdapter);

                }
            }
        );

        notesList.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Note selectedNote = (Note) notesList.getItemAtPosition(position);
                    Intent intent = new Intent(AssessmentDetail.this, NoteDetail.class);
                    intent.putExtra("Note", selectedNote);
                    startActivity(intent);

                }
            }
        );
    }

    private void finishEditing() {

        int assessment_notifications = assessmentNotifications.isChecked() ? 1 : 0;
        String assessment_name = assessmentName.getText().toString();
        String assessment_type = assessmentType.getSelectedItem().toString();
        String assessment_due_date = assessmentDueDate.getText().toString();

        Course selectedCourse = (Course) coursesDropdown.getSelectedItem();

        if (assessment_name.length() > 0 &&
            assessment_type.length() > 0 &&
            assessment_due_date.length() > 0 &&
            selectedCourse != null) {

            String toastMessage = null;

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

                    toastMessage = assessment_name + " added successfully.";

                    break;


                case Intent.ACTION_EDIT:

                    assessment.setCourse_id(selectedCourse.getId());
                    assessment.setName(assessment_name);
                    assessment.setType(assessment_type);
                    assessment.setDue_date(assessment_due_date);
                    assessment.setNotifications(assessment_notifications);

                    assessmentViewModel.update(assessment);

                    toastMessage = assessment_name + " updated successfully.";

                    break;

                case Intent.ACTION_DELETE:

                    assessmentViewModel.delete(assessment);

                    toastMessage = assessment_name + " deleted successfully.";

                    break;

            }

            UserInterfaceUtils.toastUser(this, toastMessage);

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

    private void deleteAssessment() {

        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int button) {

                // Do nothing if they click NO
                if (button == DialogInterface.BUTTON_POSITIVE) {

                    action = Intent.ACTION_DELETE;
                    finishEditing();

                }

            }
        };

        UserInterfaceUtils.alertUser(
            this,
            "Are you sure you want to delete this assessment?",
            dialogListener
        );

    }
}
