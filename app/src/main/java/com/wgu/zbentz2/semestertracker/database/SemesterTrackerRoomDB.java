package com.wgu.zbentz2.semestertracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wgu.zbentz2.semestertracker.database.dao.TermDao;
import com.wgu.zbentz2.semestertracker.database.entities.Term;

@Database(entities = {Term.class}, version = 1)
public abstract class SemesterTrackerRoomDB extends RoomDatabase {

    public abstract TermDao termDao();

    private static volatile SemesterTrackerRoomDB INSTANCE;

    public static SemesterTrackerRoomDB getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (SemesterTrackerRoomDB.class) {

                INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    SemesterTrackerRoomDB.class,
                    "semester_tracker_database"
                )
                .allowMainThreadQueries() // So we can return the last inserted PK.
                .build();

            }

        }

        return INSTANCE;

    }

}
