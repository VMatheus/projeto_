package com.tecnoia.matheus.financascosmeticos.revendedor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tecnoia.matheus.financascosmeticos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilRevendedor extends Fragment {

    public static PerfilRevendedor newInstance() {
        return new PerfilRevendedor();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_perfil_revendedor, container, false);
        return rootView;
    }

}
