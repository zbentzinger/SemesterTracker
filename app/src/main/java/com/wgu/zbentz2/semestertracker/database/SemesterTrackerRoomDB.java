package com.wgu.zbentz2.semestertracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wgu.zbentz2.semestertracker.database.dao.AssessmentDAO;
import com.wgu.zbentz2.semestertracker.database.dao.CourseDAO;
import com.wgu.zbentz2.semestertracker.database.dao.NoteDAO;
import com.wgu.zbentz2.semestertracker.database.dao.TermDAO;
import com.wgu.zbentz2.semestertracker.database.entities.Assessment;
import com.wgu.zbentz2.semestertracker.database.entities.Course;
import com.wgu.zbentz2.semestertracker.database.entities.Note;
import com.wgu.zbentz2.semestertracker.database.entities.Term;

@Database(
    entities = {
        Term.class,
        Course.class,
        Assessment.class,
        Note.class
    },
    version = 1
)
public abstract class SemesterTrackerRoomDB extends RoomDatabase {

    public abstract TermDAO termDao();
    public abstract CourseDAO courseDao();
    public abstract AssessmentDAO assessmentDao();
    public abstract NoteDAO noteDao();

    private static volatile SemesterTrackerRoomDB INSTANCE;

    public static SemesterTrackerRoomDB getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (SemesterTrackerRoomDB.class) {

                INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    SemesterTrackerRoomDB.class,
                    "semester_tracker_database"
                ).build();
            }
        }

        return INSTANCE;

    }
}
