package com.wgu.zbentz2.semestertracker.database.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.wgu.zbentz2.semestertracker.database.SemesterTrackerRoomDB;
import com.wgu.zbentz2.semestertracker.database.dao.NoteDAO;
import com.wgu.zbentz2.semestertracker.database.entities.Note;

import java.util.List;

public class NoteRepository {

    private NoteDAO noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {

        SemesterTrackerRoomDB db = SemesterTrackerRoomDB.getDatabase(application);
        noteDao = db.noteDao();
        allNotes = noteDao.getAllNotes();

    }

    public LiveData<List<Note>> getAllNotes() {

        return allNotes;

    }

    public LiveData<List<Note>> getAllNotesForCourse(long course_id) {

        return noteDao.getAllNotesForCourse(course_id);

    }

    public void insert(Note note) {

        new insertAsyncTask(noteDao).execute(note);

    }

    public void delete(Note note) {

        new deleteAsyncTask(noteDao).execute(note);

    }

    public void update(Note note) {

        new updateAsyncTask(noteDao).execute(note);

    }

    private static class insertAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDAO asyncNoteDao;

        insertAsyncTask(NoteDAO noteDao) {

            asyncNoteDao = noteDao;

        }

        @Override protected Void doInBackground(Note... notes) {

            asyncNoteDao.insert(notes[0]);
            return null;

        }
    }

    private static class deleteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDAO asyncNoteDao;

        deleteAsyncTask(NoteDAO noteDao) {

            asyncNoteDao = noteDao;

        }

        @Override protected Void doInBackground(Note... notes) {

            asyncNoteDao.delete(notes[0]);
            return null;

        }
    }

    private static class updateAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDAO asyncNoteDao;

        updateAsyncTask(NoteDAO noteDao) {

            asyncNoteDao = noteDao;

        }

        @Override protected Void doInBackground(final Note... notes) {

            asyncNoteDao.update(notes[0]);
            return null;

        }
    }
}
