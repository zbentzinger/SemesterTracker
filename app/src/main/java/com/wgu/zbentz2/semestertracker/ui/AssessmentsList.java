package com.wgu.zbentz2.semestertracker.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Assessment;
import com.wgu.zbentz2.semestertracker.database.viewmodels.AssessmentViewModel;
import com.wgu.zbentz2.semestertracker.utils.adapters.AssessmentRecyclerViewAdapter;
import com.wgu.zbentz2.semestertracker.utils.listeners.AssessmentClickListener;

import java.util.ArrayList;
import java.util.List;


public class AssessmentsList extends Fragment {

    private AssessmentViewModel assessmentViewModel;

    public AssessmentsList() {}


    @Override public View onCreateView(@NonNull LayoutInflater inflater,
                                       ViewGroup container,
                                       Bundle savedInstanceState) {

        View assessments_view = inflater.inflate(
            R.layout.fragment_assessments_list,
            container,
            false
        );

        RecyclerView recyclerView = assessments_view.findViewById(R.id.assessments_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final AssessmentRecyclerViewAdapter adapter = new AssessmentRecyclerViewAdapter(
            getActivity(),
            addAssessmentClickListener()
        );

        recyclerView.setAdapter(adapter);

        // Reload CardView with new/updated assessments.
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);

        assessmentViewModel.getAllAssessments().observe(
            this,
            new Observer<List<Assessment>>() {
                @Override public void onChanged(final List<Assessment> assessments) {
                    // Per requirement C.1.a.
                    adapter.setAssessments((ArrayList<Assessment>) assessments);
                }
            }
        );

        return assessments_view;

    }

    @Override public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Assessments");

        FloatingActionButton fab = getView().findViewById(R.id.assessments_fab);
        fab.setOnClickListener(
            new View.OnClickListener() {
                @Override public void onClick(View view) {
                    startActivity(
                        new Intent(getActivity(), AssessmentDetail.class)
                    );
                }
            }
        );
    }

    private AssessmentClickListener addAssessmentClickListener() {

        return new AssessmentClickListener() {
            @Override public void onAssessmentClick(Assessment assessment) {
                Intent intent = new Intent(getActivity(), AssessmentDetail.class);
                intent.putExtra("Assessment", assessment); // Pass the assessment object to the add/edit/details view.
                startActivity(intent);
            }
        };
    }
}
