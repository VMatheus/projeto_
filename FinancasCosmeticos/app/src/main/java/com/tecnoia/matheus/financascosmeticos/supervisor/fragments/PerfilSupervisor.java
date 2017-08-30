package com.tecnoia.matheus.financascosmeticos.supervisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tecnoia.matheus.financascosmeticos.R;

import butterknife.ButterKnife;

import static android.R.attr.offset;

public class PerfilSupervisor extends Fragment{


    public static PerfilSupervisor newInstance() {

        return new PerfilSupervisor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_perfil_supervisor, container, false);
        setHasOptionsMenu(true);
      /*  ButterKnife.bind(getActivity());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        initViews(rootview);
        /*Toolbar toolbar = rootview.findViewById(R.id.perfil_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null

        toolbar.setTitle("");
/**/
        return rootview;

    }

    private void initViews(View rootview) {

    }



}
