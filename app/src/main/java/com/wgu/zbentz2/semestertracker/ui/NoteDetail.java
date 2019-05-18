package com.wgu.zbentz2.semestertracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Course;
import com.wgu.zbentz2.semestertracker.database.entities.Note;
import com.wgu.zbentz2.semestertracker.database.viewmodels.CourseViewModel;
import com.wgu.zbentz2.semestertracker.database.viewmodels.NoteViewModel;

import java.util.List;

public class NoteDetail extends AppCompatActivity {

    private EditText noteName;
    private EditText noteBody;
    private Spinner coursesDropdown;

    private CourseViewModel courseViewModel;
    private NoteViewModel noteViewModel;
    private Course course;
    private Note note;

    private String action;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instantiate class variables
        noteName = findViewById(R.id.edit_note_name);
        noteBody = findViewById(R.id.edit_note_body);
        coursesDropdown = findViewById(R.id.edit_note_course_dropdown);

        // Instantiate database view models
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

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
        note = (Note) intent.getSerializableExtra("Note");

        if (note != null) {

            action = Intent.ACTION_EDIT;

            noteName.setText(note.getNote_name());
            noteBody.setText(note.getNote_body());

            this.setTitle("Edit Note");

        } else {

            action = Intent.ACTION_INSERT;

            this.setTitle("New Note");

        }
    }

    private void finishEditing() {

        String note_name = noteName.getText().toString();
        String note_body = noteBody.getText().toString();

        Course selectedCourse = (Course) coursesDropdown.getSelectedItem();

        if (note_name.length() > 0 &&
            note_body.length() > 0 &&
            selectedCourse != null) {

            switch (action) {

                case Intent.ACTION_INSERT:

                    note = new Note(
                        selectedCourse.getId(),
                        note_name,
                        note_body
                    );

                    noteViewModel.insert(note);
                    break;


                case Intent.ACTION_EDIT:

                    note.setCourse_id(selectedCourse.getId());
                    note.setNote_name(note_name);
                    note.setNote_body(note_body);

                    noteViewModel.update(note);
                    break;

            }

        }

        finish();

    }

    private void setupSpinner() {

        final ArrayAdapter<Course> spinnerAdapter = new ArrayAdapter<>(
            NoteDetail.this,
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
                    if(note != null) {

                        course = courseViewModel.getCourse(note.getCourse_id());
                        int pos = spinnerAdapter.getPosition(course);
                        coursesDropdown.setSelection(pos, true);
                    }
                }
            }
        );
    }
}
