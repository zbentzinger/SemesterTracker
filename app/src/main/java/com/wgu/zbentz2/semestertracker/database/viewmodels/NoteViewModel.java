package com.wgu.zbentz2.semestertracker.database.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wgu.zbentz2.semestertracker.database.entities.Note;
import com.wgu.zbentz2.semestertracker.database.repositories.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(Application application) {

        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();

    }

    public LiveData<List<Note>> getAllNotes() {

        return allNotes;

    }

    public LiveData<List<Note>> getAllNotesForCourse(long course_id) {

        return noteRepository.getAllNotesForCourse(course_id);

    }

    public void insert(Note note) {

        noteRepository.insert(note);

    }

    public void delete(Note note) {

        noteRepository.delete(note);

    }

    public void update(Note note) {

        noteRepository.update(note);

    }
}
