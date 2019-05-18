package com.wgu.zbentz2.semestertracker.database.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wgu.zbentz2.semestertracker.database.entities.Assessment;
import com.wgu.zbentz2.semestertracker.database.repositories.AssessmentRepository;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private AssessmentRepository assessmentRepository;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentViewModel(Application application) {

        super(application);
        assessmentRepository = new AssessmentRepository(application);
        allAssessments = assessmentRepository.getAllAssessments();

    }

    public LiveData<List<Assessment>> getAllAssessments() {

        return allAssessments;

    }

    public LiveData<List<Assessment>> getAllAssessmentsForCourse(long course_id) {

        return assessmentRepository.getAllAssessmentsForCourse(course_id);

    }

    public void insert(Assessment assessment) {

        assessmentRepository.insert(assessment);

    }

    public void delete(Assessment assessment) {

        assessmentRepository.delete(assessment);

    }

    public void update(Assessment assessment) {

        assessmentRepository.update(assessment);

    }
}
