package com.wgu.zbentz2.semestertracker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wgu.zbentz2.semestertracker.database.entities.Assessment;

import java.util.List;

@Dao
public interface AssessmentDAO {

    @Insert
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM assessments ORDER BY id DESC")
    LiveData<List<Assessment>> getAllAssessments();

    @Query("SELECT * FROM assessments WHERE course_id = :course_id ORDER BY id DESC")
    LiveData<List<Assessment>> getAllAssessmentsForCourse(long course_id);

}
