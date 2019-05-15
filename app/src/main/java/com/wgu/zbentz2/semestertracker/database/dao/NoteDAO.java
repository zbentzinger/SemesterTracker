package com.wgu.zbentz2.semestertracker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wgu.zbentz2.semestertracker.database.entities.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert
    long insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes ORDER BY id ASC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE course_id = :course_id ORDER BY id ASC")
    LiveData<List<Note>> getAllNotesForCourse(long course_id);

}
