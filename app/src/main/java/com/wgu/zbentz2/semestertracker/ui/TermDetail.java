package com.wgu.zbentz2.semestertracker.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Course;
import com.wgu.zbentz2.semestertracker.database.entities.Term;
import com.wgu.zbentz2.semestertracker.database.viewmodels.CourseViewModel;
import com.wgu.zbentz2.semestertracker.database.viewmodels.TermViewModel;
import com.wgu.zbentz2.semestertracker.utils.UserInterfaceUtils;

import java.util.Calendar;
import java.util.List;


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

                    UserInterfaceUtils.snackbarUser(
                        findViewById(R.id.term_detail_coordinator_view),
                        "You cannot delete a term that has associated courses."
                    );

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

            startCal.setTime(
                UserInterfaceUtils.parseDateString(term.getStart_date())
            );

            endCal.setTime(
                UserInterfaceUtils.parseDateString(term.getEnd_date())
            );

            populateCourseListView(term.getId());

            this.setTitle("Edit Term");

        } else {

            action = Intent.ACTION_INSERT;

            this.setTitle("New Term");

        }

        UserInterfaceUtils.setupCalendar(termStartDate, startCal, this);
        UserInterfaceUtils.setupCalendar(termEndDate, endCal, this);

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

    private void finishEditing() {

        String term_name = termName.getText().toString();
        String term_start_date  = termStartDate.getText().toString();
        String term_end_date = termEndDate.getText().toString();

        if (term_name.length() > 0 &&
            term_start_date.length() > 0 &&
            term_end_date.length() > 0) {

            String toastMessage = null;

            switch (action) {

                case Intent.ACTION_INSERT:

                    term = new Term(term_name, term_start_date, term_end_date);
                    termViewModel.insert(term);

                    toastMessage = term_name + " added successfully.";

                    break;


                case Intent.ACTION_EDIT:

                    term.setName(term_name);
                    term.setStart_date(term_start_date);
                    term.setEnd_date(term_end_date);

                    termViewModel.update(term);

                    toastMessage = term_name + " updated successfully.";

                    break;

                case Intent.ACTION_DELETE:

                    termViewModel.delete(term);

                    toastMessage = term_name + " deleted successfully.";

                    break;

            }

            UserInterfaceUtils.toastUser(this, toastMessage);

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

        UserInterfaceUtils.alertUser(
            this,
            "Are you sure you want to delete this term?",
            dialogListener
        );

    }

}
