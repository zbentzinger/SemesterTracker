package com.wgu.zbentz2.semestertracker.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Term;
import com.wgu.zbentz2.semestertracker.database.viewmodels.TermViewModel;
import com.wgu.zbentz2.semestertracker.utils.UserInterfaceUtils;
import com.wgu.zbentz2.semestertracker.utils.adapters.TermRecyclerViewAdapter;
import com.wgu.zbentz2.semestertracker.utils.listeners.TermClickListener;

import java.util.ArrayList;
import java.util.List;


public class TermsList extends Fragment {

    private ConstraintLayout constraintLayout;
    private TextView emptyMessage;

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

        constraintLayout = terms_view.findViewById(R.id.terms_fragment);
        emptyMessage = terms_view.findViewById(R.id.terms_list_no_terms);

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
                    // Per requirement C.1.a.
                    adapter.setTerms((ArrayList<Term>) terms);

                    // Show a message if there are no items in the adapter.
                    if (adapter.getItemCount() == 0 ) {

                        emptyMessage.setVisibility(View.VISIBLE);

                    } else {

                        emptyMessage.setVisibility(View.GONE);

                    }
                }
            }
        );

        return terms_view;

    }

    @Override public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Terms");

        // Per Requirement C.1.k. Programmatic method for creating user interface.
        // Creating a FAB programmatically.
        if (constraintLayout != null) {

            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );

            FloatingActionButton fab = new FloatingActionButton(getContext());
            fab.setId(View.generateViewId());
            fab.setLayoutParams(lp);
            fab.setClickable(true);
            fab.setImageResource(R.drawable.ic_add);
            fab.setFocusable(true);
            fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        startActivity(
                            new Intent(getActivity(), TermDetail.class)
                        );
                    }
                }
            );
            fab.setImageTintList(getContext().getColorStateList(R.color.textColorAccent));

            constraintLayout.addView(fab);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(
                fab.getId(),
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                UserInterfaceUtils.dpToPixel(16)
            );
            constraintSet.connect(
                fab.getId(),
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END,
                UserInterfaceUtils.dpToPixel(16)
            );
            constraintSet.applyTo(constraintLayout);

        }

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
