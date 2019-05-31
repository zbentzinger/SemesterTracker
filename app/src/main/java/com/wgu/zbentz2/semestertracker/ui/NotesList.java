package com.wgu.zbentz2.semestertracker.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Note;
import com.wgu.zbentz2.semestertracker.database.viewmodels.NoteViewModel;
import com.wgu.zbentz2.semestertracker.utils.adapters.NoteRecyclerViewAdapter;
import com.wgu.zbentz2.semestertracker.utils.listeners.NoteClickListener;

import java.util.ArrayList;
import java.util.List;


public class NotesList extends Fragment {

    private TextView emptyMessage;

    private NoteViewModel noteViewModel;

    public NotesList() {}

    @Override public View onCreateView(LayoutInflater inflater,
                                       ViewGroup container,
                                       Bundle savedInstanceState) {

        View notes_view = inflater.inflate(
            R.layout.fragment_notes_list,
            container,
            false
        );

        emptyMessage = notes_view.findViewById(R.id.notes_list_empty_text);

        RecyclerView recyclerView = notes_view.findViewById(R.id.notes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final NoteRecyclerViewAdapter adapter = new NoteRecyclerViewAdapter(
            getActivity(),
            addNoteClickListener()
        );

        recyclerView.setAdapter(adapter);

        // Reload CardView with new/updated notes.
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(
            this,
            new Observer<List<Note>>() {
                @Override public void onChanged(final List<Note> notes) {
                    // Per requirement C.1.a.
                    adapter.setNotes((ArrayList<Note>) notes);

                    // Show a message if there are no items in the adapter.
                    if (adapter.getItemCount() == 0 ) {

                        emptyMessage.setVisibility(View.VISIBLE);

                    } else {

                        emptyMessage.setVisibility(View.GONE);

                    }
                }
            }
        );

        return notes_view;

    }

    @Override public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Notes");

        FloatingActionButton fab = getView().findViewById(R.id.notes_fab);

        fab.setOnClickListener(
            new View.OnClickListener() {
                @Override public void onClick(View view) {
                    startActivity(
                        new Intent(getActivity(), NoteDetail.class)
                    );
                }
            }
        );
    }

    private NoteClickListener addNoteClickListener() {

        return new NoteClickListener() {
            @Override public void onNoteClick(Note note) {
                Intent intent = new Intent(getActivity(), NoteDetail.class);
                intent.putExtra("Note", note); // Pass the note object to the add/edit/details view.
                startActivity(intent);
            }
        };
    }
}
