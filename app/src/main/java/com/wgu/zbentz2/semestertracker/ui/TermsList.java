package com.wgu.zbentz2.semestertracker.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Term;
import com.wgu.zbentz2.semestertracker.database.viewmodels.TermViewModel;
import com.wgu.zbentz2.semestertracker.utils.adapters.TermRecyclerViewAdapter;
import com.wgu.zbentz2.semestertracker.utils.listeners.TermClickListener;

import java.util.List;


public class TermsList extends Fragment {

    private TermViewModel termViewModel;

    public TermsList() {}

    @Override public View onCreateView(LayoutInflater inflater,
                                       ViewGroup container,
                                       Bundle savedInstanceState) {

        View terms_view = inflater.inflate(
            R.layout.fragment_terms_list,
            container,
            false
        );

        RecyclerView recyclerView = terms_view.findViewById(R.id.terms_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final TermRecyclerViewAdapter adapter = new TermRecyclerViewAdapter(
            getActivity(),
            addTermClickListener()
        );

        recyclerView.setAdapter(adapter);

        // Reload CardView with new/updated terms.
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);

        termViewModel.getAllTerms().observe(
            this,
            new Observer<List<Term>>() {
                @Override public void onChanged(final List<Term> terms) {
                    adapter.setTerms(terms);
                }
            }
        );

        return terms_view;

    }

    @Override public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Terms");

        FloatingActionButton fab = getView().findViewById(R.id.terms_fab);

        fab.setOnClickListener(
            new View.OnClickListener() {
                @Override public void onClick(View view) {
                    startActivity(
                        new Intent(getActivity(), TermDetail.class)
                    );
                }
            }
        );

    }

    private TermClickListener addTermClickListener() {

        return new TermClickListener() {
            @Override public void onTermClick(Term term) {
                Intent intent = new Intent(getActivity(), TermDetail.class);
                intent.putExtra("Term", term); // Pass the term object to the add/edit/details view.
                startActivity(intent);
            }
        };

    }

}
