package com.projeto.adrielle.cosmeticosfinancas.revendedor;

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
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;

public class MenuRevendedora extends Fragment {
    private BottomNavigationView bottomNavigationView;
    Fragment fragment = null;


    public static MenuRevendedora newInstance() {
        return new MenuRevendedora();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_revendedor, container, false);
        if (container != null) {
            container.removeAllViews();
        }


        initViews(rootView);


        return rootView;


    }


    private void initViews(View rootView) {
        bottomNavigationView = rootView.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.item__vendas:
                        fragment = VendasRealizadas.newInstance();

                        break;

                    case R.id.item_produtos:
                        fragment = ProdutosRevendedor.newInstance();
                        break;

                    case R.id.item_perfil_revendedor:
                        fragment = PerfilRevendedor.newInstance();
                        break;

                }


                FragmentUtils.replaceRevendedor(getActivity(), fragment);

                return true;
            }
        });

        FragmentUtils.replaceRevendedor(getActivity(), VendasRealizadas.newInstance());


    }


}
