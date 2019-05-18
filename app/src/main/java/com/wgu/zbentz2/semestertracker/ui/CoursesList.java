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
import com.wgu.zbentz2.semestertracker.database.entities.Course;
import com.wgu.zbentz2.semestertracker.database.viewmodels.CourseViewModel;
import com.wgu.zbentz2.semestertracker.utils.adapters.CourseRecyclerViewAdapter;
import com.wgu.zbentz2.semestertracker.utils.listeners.CourseClickListener;

import java.util.List;


public class CoursesList extends Fragment {

    private CourseViewModel courseViewModel;

    public CoursesList() {}


    @Override public View onCreateView(@NonNull LayoutInflater inflater,
                                       ViewGroup container,
                                       Bundle savedInstanceState) {

        View courses_view = inflater.inflate(
            R.layout.fragment_courses_list,
            container,
            false
        );

        RecyclerView recyclerView = courses_view.findViewById(R.id.courses_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final CourseRecyclerViewAdapter adapter = new CourseRecyclerViewAdapter(
            getActivity(),
            addCourseClickListener()
        );

        recyclerView.setAdapter(adapter);

        // Reload CardView with new/updated courses.
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

        courseViewModel.getAllCourses().observe(
            this,
            new Observer<List<Course>>() {
                @Override public void onChanged(final List<Course> courses) {
                    adapter.setCourses(courses);
                }
            }
        );

        return courses_view;

    }

    @Override public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Courses");

        FloatingActionButton fab = getView().findViewById(R.id.courses_fab);
        fab.setOnClickListener(
            new View.OnClickListener() {
                @Override public void onClick(View view) {
                    startActivity(
                        new Intent(getActivity(), CourseDetail.class)
                    );
                }
            }
        );
    }

    private CourseClickListener addCourseClickListener() {

        return new CourseClickListener() {
            @Override public void onCourseClick(Course course) {
                Intent intent = new Intent(getActivity(), CourseDetail.class);
                intent.putExtra("Course", course); // Pass the course object to the add/edit/details view.
                startActivity(intent);
            }
        };
    }
}
