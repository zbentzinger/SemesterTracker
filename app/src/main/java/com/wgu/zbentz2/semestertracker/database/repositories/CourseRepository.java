package com.wgu.zbentz2.semestertracker.database.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.wgu.zbentz2.semestertracker.database.SemesterTrackerRoomDB;
import com.wgu.zbentz2.semestertracker.database.dao.CourseDAO;
import com.wgu.zbentz2.semestertracker.database.entities.Course;

import java.util.List;

public class CourseRepository {

    private CourseDAO courseDao;
    private LiveData<List<Course>> allCourses;

    public CourseRepository(Application application) {

        SemesterTrackerRoomDB db = SemesterTrackerRoomDB.getDatabase(application);
        courseDao = db.courseDao();
        allCourses = courseDao.getAllCourses();

    }

    public LiveData<List<Course>> getAllCourses() {

        return allCourses;

    }

    public LiveData<List<Course>> getAllCoursesForTerm(long term_id) {

        return courseDao.getAllCoursesForTerm(term_id);

    }

    public void insert(Course course) {

        new insertAsyncTask(courseDao).execute(course);

    }

    public void delete(Course course) {

        new deleteAsyncTask(courseDao).execute(course);

    }

    public void update(Course course) {

        new updateAsyncTask(courseDao).execute(course);

    }

    private static class insertAsyncTask extends AsyncTask<Course, Void, Void> {

        private CourseDAO asyncCourseDao;

        insertAsyncTask(CourseDAO courseDao) {

            asyncCourseDao = courseDao;

        }

        @Override protected Void doInBackground(Course... courses) {

            asyncCourseDao.insert(courses[0]);
            return null;

        }
    }

    private static class deleteAsyncTask extends AsyncTask<Course, Void, Void> {

        private CourseDAO asyncCourseDao;

        deleteAsyncTask(CourseDAO courseDao) {

            asyncCourseDao = courseDao;

        }

        @Override protected Void doInBackground(Course... courses) {

            asyncCourseDao.delete(courses[0]);
            return null;

        }
    }

    private static class updateAsyncTask extends AsyncTask<Course, Void, Void> {

        private CourseDAO asyncCourseDao;

        updateAsyncTask(CourseDAO courseDao) {

            asyncCourseDao = courseDao;

        }

        @Override protected Void doInBackground(final Course... courses) {

            asyncCourseDao.update(courses[0]);
            return null;

        }
    }
}
