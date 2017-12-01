package com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projeto.adrielle.cosmeticosfinancas.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilEditSupervisor extends Fragment {


    public static  PerfilEditSupervisor newInstace() {
        return new PerfilEditSupervisor();

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil_edit_supervisor, container, false);


    }

}
