package com.wgu.zbentz2.semestertracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Term;
import com.wgu.zbentz2.semestertracker.database.repositories.TermRepository;

public class TermDetail extends AppCompatActivity {

    private EditText termName;
    private EditText termStartDate;
    private EditText termEndDate;

    private String action;

    private Term term;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        // Initialize field variables.
        termName = findViewById(R.id.edit_term_name);
        termStartDate = findViewById(R.id.edit_term_start_date);
        termEndDate = findViewById(R.id.edit_term_end_date);

        // Set up Action Bar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the intent from the previous view and set config based on it.
        Intent intent = getIntent();
        term = (Term) intent.getSerializableExtra("Term");

        if (term != null) {

            action = Intent.ACTION_EDIT;

            termName.setText(term.getName());
            termStartDate.setText(term.getStart_date());
            termEndDate.setText(term.getEnd_date());

            this.setTitle("Edit Term");

        } else {

            action = Intent.ACTION_INSERT;

            this.setTitle("New Term");

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

        TermRepository termRepository = new TermRepository(getApplication());

        String term_name = termName.getText().toString();
        String term_start_date  = termStartDate.getText().toString();
        String term_end_date = termEndDate.getText().toString();

        if (term_name.length() > 0 && term_start_date.length() > 0 && term_end_date.length() > 0) {

            switch (action) {

                case Intent.ACTION_INSERT:

                    term = new Term(term_name, term_start_date, term_end_date);

                    long temp = termRepository.insert(term);
                    break;


                case Intent.ACTION_EDIT:

                    term.setName(term_name);
                    term.setStart_date(term_start_date);
                    term.setEnd_date(term_end_date);

                    termRepository.update(term);
                    break;


            }
        }

        finish();

    }

}
