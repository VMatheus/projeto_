package com.tecnoia.matheus.financascosmeticos.supervisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

public class RevendedoresContainer extends Fragment {

    //Container principal de gerencia Revendedores

    public static RevendedoresContainer newInstance() {
        RevendedoresContainer revendedoresContainer = new RevendedoresContainer();
        return revendedoresContainer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_revendores_container, container, false);
        if (container != null) {
            container.removeAllViews();
        }
        initViews(rootview);

        return rootview;


    }

    private void initViews(View rootview) {

        FragmentUtils.replaceListaRevendedores(getActivity(), ListaRevendedores.newInstance());

    }


}