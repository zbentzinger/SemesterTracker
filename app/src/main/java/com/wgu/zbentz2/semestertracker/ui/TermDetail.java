package com.wgu.zbentz2.semestertracker.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Course;
import com.wgu.zbentz2.semestertracker.database.entities.Term;
import com.wgu.zbentz2.semestertracker.database.viewmodels.CourseViewModel;
import com.wgu.zbentz2.semestertracker.database.viewmodels.TermViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class TermDetail extends AppCompatActivity {

    private Calendar startCal;
    private Calendar endCal;
    private EditText termName;
    private EditText termStartDate;
    private EditText termEndDate;
    private ListView termCourseList;

    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;
    private Term term;

    private boolean canDelete = false;
    private String action;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instantiate class variables
        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();
        termName = findViewById(R.id.edit_term_name);
        termStartDate = findViewById(R.id.edit_term_start_date);
        termEndDate = findViewById(R.id.edit_term_end_date);
        termCourseList = findViewById(R.id.edit_term_course_list);
        termCourseList.setEmptyView(findViewById(R.id.edit_term_empty_text));

        // Instantiate database view models
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

        // Populate the view with data if necessary
        initData();

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

                if (canDelete) {

                    deleteTerm();

                } else {

                    View contextView = findViewById(R.id.term_detail_coordinator_view);
                    Snackbar.make(
                        contextView,
                        "You cannot delete a term that has associated courses.",
                        Snackbar.LENGTH_LONG)
                    .show();

                }

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
        term = (Term) intent.getSerializableExtra("Term");

        if (term != null) {

            action = Intent.ACTION_EDIT;

            termName.setText(term.getName());

            try {

                startCal.setTime(dateFormat.parse(term.getStart_date()));
                endCal.setTime(dateFormat.parse(term.getEnd_date()));

            } catch (ParseException e) {

                e.printStackTrace();

            }

            populateCourseListView(term.getId());

            this.setTitle("Edit Term");

        } else {

            action = Intent.ACTION_INSERT;

            this.setTitle("New Term");

        }

        setupCalendar(termStartDate, startCal);
        setupCalendar(termEndDate, endCal);

    }

    private void populateCourseListView(long term_id) {

        final ArrayAdapter<Course> listAdapter = new ArrayAdapter<>(
            TermDetail.this,
            R.layout.term_list_item
        );

        courseViewModel.getAllCoursesForTerm(term_id).observe(
            this,
            new Observer<List<Course>>() {
                @Override public void onChanged(final List<Course> courses) {

                    listAdapter.clear();
                    listAdapter.addAll(courses);
                    termCourseList.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();

                    // Rubric A3.
                    // So if the user wants to delete the term later on.
                    canDelete = courses.size() == 0;
                    
                }
            }
        );

        termCourseList.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Course selectedCourse = (Course) termCourseList.getItemAtPosition(position);
                    Intent intent = new Intent(TermDetail.this, CourseDetail.class);
                    intent.putExtra("Course", selectedCourse);
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
                        TermDetail.this,
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

        String term_name = termName.getText().toString();
        String term_start_date  = termStartDate.getText().toString();
        String term_end_date = termEndDate.getText().toString();

        if (term_name.length() > 0 &&
            term_start_date.length() > 0 &&
            term_end_date.length() > 0) {

            switch (action) {

                case Intent.ACTION_INSERT:

                    term = new Term(term_name, term_start_date, term_end_date);

                    termViewModel.insert(term);

                    break;


                case Intent.ACTION_EDIT:

                    term.setName(term_name);
                    term.setStart_date(term_start_date);
                    term.setEnd_date(term_end_date);

                    termViewModel.update(term);

                    break;

                case Intent.ACTION_DELETE:

                    termViewModel.delete(term);

                    break;

            }
        }

        finish();

    }

    private void deleteTerm() {

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

        builder.setMessage("Are you sure you want to delete this term?")
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
