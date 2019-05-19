package com.wgu.zbentz2.semestertracker.ui;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;


public class TermDetail extends AppCompatActivity {

    private EditText termName;
    private EditText termStartDate;
    private EditText termEndDate;
    private ListView termCourseList;

    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;
    private Term term;

    private String action;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instantiate class variables
        termName = findViewById(R.id.edit_term_name);
        termStartDate = findViewById(R.id.edit_term_start_date);
        termEndDate = findViewById(R.id.edit_term_end_date);
        termCourseList = findViewById(R.id.edit_term_course_list);

        // Instantiate database view models
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

        // Populate the view with data if necessary
        initData();

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
        term = (Term) intent.getSerializableExtra("Term");

        if (term != null) {

            action = Intent.ACTION_EDIT;

            termName.setText(term.getName());
            termStartDate.setText(term.getStart_date());
            termEndDate.setText(term.getEnd_date());

            populateListView(term.getId());

            this.setTitle("Edit Term");

        } else {

            action = Intent.ACTION_INSERT;

            this.setTitle("New Term");

        }

    }

    private void populateListView(long term_id) {

        final ArrayAdapter<Course> listAdapter = new ArrayAdapter<>(
            TermDetail.this,
            android.R.layout.simple_list_item_1
        );

        courseViewModel.getAllCoursesForTerm(term_id).observe(
            this,
            new Observer<List<Course>>() {
                @Override public void onChanged(final List<Course> courses) {

                    listAdapter.clear();
                    listAdapter.addAll(courses);
                    termCourseList.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();

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

            }
        }

        finish();

    }
}
