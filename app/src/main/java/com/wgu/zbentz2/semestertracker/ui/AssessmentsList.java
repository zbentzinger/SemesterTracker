package com.wgu.zbentz2.semestertracker.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgu.zbentz2.semestertracker.R;


public class AssessmentsList extends Fragment {


    public AssessmentsList() {}


    @Override public View onCreateView(@NonNull LayoutInflater inflater,
                                       ViewGroup container,
                                       Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_assessments_list, container, false);

    }

    @Override public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Assessments");

        FloatingActionButton fab = getView().findViewById(R.id.assessments_fab);
        fab.setOnClickListener(
            new View.OnClickListener() {
                @Override public void onClick(View view) {
                    startActivity(new Intent(getActivity(), AssessmentDetail.class));
                }
            }
        );

    }

}
