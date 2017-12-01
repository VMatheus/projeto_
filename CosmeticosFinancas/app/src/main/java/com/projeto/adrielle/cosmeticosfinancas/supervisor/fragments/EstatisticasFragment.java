package com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projeto.adrielle.cosmeticosfinancas.R;

public class EstatisticasFragment extends Fragment {

    public static EstatisticasFragment newInstance() {
        return new EstatisticasFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_estatisticas_fragment, container, false);
        return rootview;
    }
}