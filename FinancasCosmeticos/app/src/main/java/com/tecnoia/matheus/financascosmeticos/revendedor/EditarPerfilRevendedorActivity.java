package com.tecnoia.matheus.financascosmeticos.revendedor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tecnoia.matheus.financascosmeticos.R;

public class EditarPerfilRevendedorActivity extends Fragment {
    private Toolbar toolbar;

    public static EditarPerfilRevendedorActivity newInstace() {
        return new EditarPerfilRevendedorActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editar_perfil, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_editar_perfil_revendedor, container, false);
        setHasOptionsMenu(true);
        initViews(rootView);
        return rootView;


    }

    private void initViews(View rootView) {
        toolbar = rootView.findViewById(R.id.toolbar_editar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Perfil");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();

                break;

        }


        return super.onOptionsItemSelected(item);
    }
}
