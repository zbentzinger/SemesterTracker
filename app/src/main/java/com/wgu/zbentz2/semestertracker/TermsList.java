package com.wgu.zbentz2.semestertracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TermsList extends Fragment {


    public TermsList() {}


    @Override public View onCreateView(LayoutInflater inflater,
                                       ViewGroup container,
                                       Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_terms_list, container, false);

    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Term");

        FloatingActionButton fab = getView().findViewById(R.id.terms_fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TermDetail.class));
            }
        });

    }

}
