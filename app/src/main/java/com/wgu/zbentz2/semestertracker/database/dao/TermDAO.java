package com.wgu.zbentz2.semestertracker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wgu.zbentz2.semestertracker.database.entities.Term;

import java.util.List;

@Dao
public interface TermDAO {

    @Insert
    long insert(Term term);

    @Update
    void update(Term term);

    @Query("SELECT * FROM terms ORDER BY end_date ASC")
    LiveData<List<Term>> getAllTerms();

}
