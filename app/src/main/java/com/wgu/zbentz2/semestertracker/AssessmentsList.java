package com.wgu.zbentz2.semestertracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AssessmentsList extends Fragment {


    public AssessmentsList() {}


    @Override public View onCreateView(LayoutInflater inflater,
                                       ViewGroup container,
                                       Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_assessments_list, container, false);

    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Assessments");

        FloatingActionButton fab = getView().findViewById(R.id.assessments_fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AssessmentDetail.class));
            }
        });

    }

}
