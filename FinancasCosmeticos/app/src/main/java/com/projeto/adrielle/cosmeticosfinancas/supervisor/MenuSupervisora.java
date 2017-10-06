package com.projeto.adrielle.cosmeticosfinancas.supervisor;

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
import com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments.ListaRevendedores;
import com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments.PerfilSupervisor;
import com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments.ListaProdutosEstoque;
import com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments.RevendedoresContainer;
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;

public class MenuSupervisora extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPrefSupervisor;


    public static MenuSupervisora newInstance() {
        return new MenuSupervisora();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_supervisor, container, false);


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
                    /*case R.id.item_estatisticas:

                        fragment = EstatisticasFragment.newInstance();
                        break;*/

                    case R.id.item_estoque:
                        fragment = ListaProdutosEstoque.newInstance();
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
