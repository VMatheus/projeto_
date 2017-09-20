package com.tecnoia.matheus.financascosmeticos.revendedor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.utils.ValidaCamposConexao;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilRevendedor extends Fragment {
    private Toolbar toolbar;

    public static PerfilRevendedor newInstance() {
        return new PerfilRevendedor();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_perfil_revendedor, container, false);
        setHasOptionsMenu(true);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootview) {

        toolbar = rootview.findViewById(R.id.toolbar_perfil);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_perfil, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_desconectar:
                //Dialog confirmar
                ValidaCamposConexao.alertDialogDesconectar(getActivity());

                break;
            case R.id.action_editar:


                break;
        }
        return super.onOptionsItemSelected(item);


    }


}
