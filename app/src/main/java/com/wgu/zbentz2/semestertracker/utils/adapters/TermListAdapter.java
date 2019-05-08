package com.wgu.zbentz2.semestertracker.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Term;
import com.wgu.zbentz2.semestertracker.utils.listeners.TermClickListener;

import java.util.List;

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.TermViewHolder> {

    private TermClickListener adapterListener;
    private final LayoutInflater inflater;
    private List<Term> terms;

    public TermListAdapter(Context context, TermClickListener listener) {

        inflater = LayoutInflater.from(context);
        this.adapterListener = listener;

    }

    class TermViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TermClickListener holderListener;
        private final TextView term_card_header;
        private final TextView term_card_footer;

        private TermViewHolder(View itemView, TermClickListener listener) {

            super(itemView);
            this.holderListener = listener;
            itemView.setOnClickListener(this);

            term_card_header = itemView.findViewById(R.id.term_card_header);
            term_card_footer = itemView.findViewById(R.id.term_card_footer);

        }

        @Override public void onClick(View v) {

            if (holderListener != null) {

                holderListener.onTermClick(getTerm(getAdapterPosition()));

            }

        }

    }

    @NonNull
    @Override public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(
            R.layout.term_card,
            parent,
            false
        );

        return new TermViewHolder(itemView, adapterListener);

    }

    @Override public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {

        if (terms != null) {

            final Term current = getTerm(position);

            holder.term_card_header.setText(current.getName());
            holder.term_card_footer.setText(current.getStart_date() + " - " + current.getEnd_date());

        }

    }

    public void setTerms(List<Term> terms) {

        this.terms = terms;
        notifyDataSetChanged();

    }

    @Override public int getItemCount() {

        if (terms != null) {

            return this.terms.size();

        } else {

            return 0;

        }

    }

    private Term getTerm(int pos) {

        return terms.get(pos);

    }

}
