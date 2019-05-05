package com.wgu.zbentz2.semestertracker.database.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wgu.zbentz2.semestertracker.database.entities.Term;
import com.wgu.zbentz2.semestertracker.database.repositories.TermRepository;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private TermRepository termRepository;
    private LiveData<List<Term>> allTerms;

    public TermViewModel(Application application) {

        super(application);
        termRepository = new TermRepository(application);
        allTerms = termRepository.getAllTerms();

    }

    public LiveData<List<Term>> getAllTerms() {

        return allTerms;

    }

}
