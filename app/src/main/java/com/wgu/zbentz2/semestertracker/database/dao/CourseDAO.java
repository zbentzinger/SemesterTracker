package com.wgu.zbentz2.semestertracker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wgu.zbentz2.semestertracker.database.entities.Course;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert
    long insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses ORDER BY end_date ASC")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM courses WHERE term_id = :term_id ORDER BY end_date ASC")
    LiveData<List<Course>> getAllCoursesForTerm(long term_id);

}
