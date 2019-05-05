package com.wgu.zbentz2.semestertracker.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgu.zbentz2.semestertracker.R;


public class CoursesList extends Fragment {


    public CoursesList() {}


    @Override public View onCreateView(LayoutInflater inflater,
                                       ViewGroup container,
                                       Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_courses_list, container, false);

    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Courses");

        FloatingActionButton fab = getView().findViewById(R.id.courses_fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CourseDetail.class));
            }
        });

    }

}
