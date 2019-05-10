package com.wgu.zbentz2.semestertracker.database.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.wgu.zbentz2.semestertracker.database.SemesterTrackerRoomDB;
import com.wgu.zbentz2.semestertracker.database.dao.AssessmentDAO;
import com.wgu.zbentz2.semestertracker.database.entities.Assessment;

import java.util.List;

public class AssessmentRepository {

    private AssessmentDAO assessmentDao;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentRepository(Application application) {

        SemesterTrackerRoomDB db = SemesterTrackerRoomDB.getDatabase(application);
        assessmentDao = db.assessmentDao();
        allAssessments = assessmentDao.getAllAssessments();

    }

    public LiveData<List<Assessment>> getAllAssessments() {

        return allAssessments;

    }

    public LiveData<List<Assessment>> getAllAssessmentsForCourse(long course_id) {

        return assessmentDao.getAllAssessmentsForCourse(course_id);

    }

    public long insert(Assessment assessment) {

        return assessmentDao.insert(assessment);

    }

    public void delete(Assessment assessment) {

        new deleteAsyncTask(assessmentDao).execute(assessment);

    }

    public void update(Assessment assessment) {

        new updateAsyncTask(assessmentDao).execute(assessment);

    }


    private static class updateAsyncTask extends AsyncTask<Assessment, Void, Void> {

        private AssessmentDAO asyncAssessmentDao;

        updateAsyncTask(AssessmentDAO assessmentDao) {

            asyncAssessmentDao = assessmentDao;

        }

        @Override protected Void doInBackground(final Assessment... assessments) {

            asyncAssessmentDao.update(assessments[0]);
            return null;

        }

    }

    private static class deleteAsyncTask extends AsyncTask<Assessment, Void, Void> {

        private AssessmentDAO asyncAssessmentDao;

        deleteAsyncTask(AssessmentDAO assessmentDao) {

            asyncAssessmentDao = assessmentDao;

        }

        @Override protected Void doInBackground(Assessment... assessments) {

            asyncAssessmentDao.delete(assessments[0]);
            return null;

        }

    }

}
