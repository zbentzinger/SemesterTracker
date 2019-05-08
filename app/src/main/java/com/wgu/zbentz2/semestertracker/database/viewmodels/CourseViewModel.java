package com.wgu.zbentz2.semestertracker.database.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wgu.zbentz2.semestertracker.database.entities.Course;
import com.wgu.zbentz2.semestertracker.database.repositories.CourseRepository;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private CourseRepository courseRepository;
    private LiveData<List<Course>> allCourses;

    public CourseViewModel(Application application) {

        super(application);
        courseRepository = new CourseRepository(application);
        allCourses = courseRepository.getAllCourses();

    }

    public LiveData<List<Course>> getAllCourses() {

        return allCourses;

    }

    public LiveData<List<Course>> getAllCoursesForTerm(long term_id) {

        return courseRepository.getAllCoursesForTerm(term_id);

    }

}
