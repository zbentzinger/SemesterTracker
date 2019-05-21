package com.wgu.zbentz2.semestertracker.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

    @Override public boolean onCreateOptionsMenu(Menu menu) {

        if (action.equals(Intent.ACTION_EDIT)) {

            getMenuInflater().inflate(R.menu.share_and_delete_options_menu, menu);

        }

        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                finishEditing();
                break;

            case R.id.delete_and_sharing_menu_delete:
                deleteNote();
                break;

            case R.id.delete_and_sharing_menu_share:
                shareNote();
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

            String toastMessage = null;

            switch (action) {

                case Intent.ACTION_INSERT:

                    note = new Note(
                        selectedCourse.getId(),
                        note_name,
                        note_body
                    );

                    noteViewModel.insert(note);

                    toastMessage = note_name + " added successfully.";

                    break;


                case Intent.ACTION_EDIT:

                    note.setCourse_id(selectedCourse.getId());
                    note.setNote_name(note_name);
                    note.setNote_body(note_body);

                    noteViewModel.update(note);

                    toastMessage = note_name + " updated successfully.";

                    break;

                case Intent.ACTION_DELETE:

                    noteViewModel.delete(note);

                    toastMessage = note_name + " deleted successfully.";

                    break;

            }

            // Let the user know all is well.
            Toast.makeText(
                NoteDetail.this,
                toastMessage,
                Toast.LENGTH_SHORT)
            .show();

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

    private void shareNote() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            noteName.getText().toString()
        );
        intent.putExtra(
            Intent.EXTRA_TEXT,
            noteBody.getText().toString()
        );

        startActivity(Intent.createChooser(intent, "Share via"));

    }

    private void deleteNote() {

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

        builder.setMessage("Are you sure you want to delete this note?")
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
