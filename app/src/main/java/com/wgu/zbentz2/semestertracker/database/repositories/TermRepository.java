package com.wgu.zbentz2.semestertracker.database.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.wgu.zbentz2.semestertracker.database.SemesterTrackerRoomDB;
import com.wgu.zbentz2.semestertracker.database.dao.TermDAO;
import com.wgu.zbentz2.semestertracker.database.entities.Term;

import java.util.List;

public class TermRepository {

    private TermDAO termDao;
    private LiveData<List<Term>> allTerms;

    public TermRepository(Application application) {

        SemesterTrackerRoomDB db = SemesterTrackerRoomDB.getDatabase(application);
        termDao = db.termDao();
        allTerms = termDao.getAllTerms();

    }

    public LiveData<List<Term>> getAllTerms() {

        return allTerms;

    }

    public long insert(Term term) {

        return termDao.insert(term);

    }

    public void update(Term term) {

        new updateAsyncTask(termDao).execute(term);

    }

    private static class updateAsyncTask extends AsyncTask<Term, Void, Void> {

        private TermDAO asyncTermDao;

        updateAsyncTask(TermDAO dao) {

            asyncTermDao = dao;

        }


        @Override protected Void doInBackground(final Term... params) {

            asyncTermDao.update(params[0]);
            return null;

        }

    }

}
