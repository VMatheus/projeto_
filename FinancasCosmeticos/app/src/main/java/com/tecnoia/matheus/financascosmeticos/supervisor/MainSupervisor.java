package com.tecnoia.matheus.financascosmeticos.supervisor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.supervisor.fragments.EstatisticasFragment;
import com.tecnoia.matheus.financascosmeticos.supervisor.fragments.ListaRevendedores;
import com.tecnoia.matheus.financascosmeticos.supervisor.fragments.PerfilSupervisor;
import com.tecnoia.matheus.financascosmeticos.supervisor.fragments.ListaProdutos;
import com.tecnoia.matheus.financascosmeticos.supervisor.fragments.RevendedoresContainer;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

public class MainSupervisor extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPrefSupervisor;


    public static MainSupervisor newInstance() {
        return new MainSupervisor();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.supervisor_main, container, false);


        initViews(rootView);
        if (container != null) {
            container.removeAllViews();
        }

        return rootView;


    }

    private void initViews(View rootView) {
        bottomNavigationView = rootView.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.item_revendedoras:
/*
                        fragment = RevendedoresContainer.newInstance();
*/
                        fragment = ListaRevendedores.newInstance();
                        break;
                    case R.id.item_estatisticas:

                        fragment = EstatisticasFragment.newInstance();
                        break;

                    case R.id.item_estoque:
                        fragment = ListaProdutos.newInstance();
                        break;

                    case R.id.item_perfil:
                        fragment = PerfilSupervisor.newInstance();

                        break;


                }
                FragmentUtils.replace(getActivity(), fragment);
                return true;
            }
        });


        FragmentUtils.replace(getActivity(), RevendedoresContainer.newInstance());


    }
}
