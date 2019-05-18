package com.wgu.zbentz2.semestertracker.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Note;
import com.wgu.zbentz2.semestertracker.utils.listeners.NoteClickListener;

import java.util.List;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder> {

    private NoteClickListener adapterListener;
    private final LayoutInflater inflater;
    private List<Note> notes;

    public NoteRecyclerViewAdapter(Context context, NoteClickListener listener) {

        inflater = LayoutInflater.from(context);
        this.adapterListener = listener;

    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private NoteClickListener holderListener;
        private final TextView note_card_header;

        private NoteViewHolder(View itemView, NoteClickListener listener) {

            super(itemView);
            this.holderListener = listener;
            itemView.setOnClickListener(this);

            note_card_header = itemView.findViewById(R.id.note_card_header);

        }

        @Override public void onClick(View v) {

            if (holderListener != null) {

                holderListener.onNoteClick(getNote(getAdapterPosition()));

            }
        }
    }

    @NonNull
    @Override public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(
            R.layout.note_card,
            parent,
            false
        );

        return new NoteViewHolder(itemView, adapterListener);

    }

    @Override public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        if (notes != null) {

            final Note current = getNote(position);

            holder.note_card_header.setText(current.getNote_name());

        }
    }

    @Override public int getItemCount() {

        if (notes != null) {

            return this.notes.size();

        } else {

            return 0;

        }
    }

    private Note getNote(int pos) {

        return notes.get(pos);

    }

    public void setNotes(List<Note> notes) {

        this.notes = notes;
        notifyDataSetChanged();

    }
}
