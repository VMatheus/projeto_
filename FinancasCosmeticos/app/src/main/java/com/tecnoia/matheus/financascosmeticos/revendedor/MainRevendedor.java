package com.tecnoia.matheus.financascosmeticos.revendedor;

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
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

public class MainRevendedor extends Fragment {
    private BottomNavigationView bottomNavigationView;

    public static MainRevendedor newInstance() {
        return new MainRevendedor();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_revendedor, container, false);
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
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.item_vendas:
                        fragment = VendasRevendedor.newInstance();


                        break;

                    case R.id.item_perfil_revendedor:
                        fragment = VendasRevendedor.newInstance();
                        break;

                    case R.id.item_perfil_supervisor:
                        fragment = VendasRevendedor.newInstance();
                        break;
                }


                FragmentUtils.replaceRevendedor(getActivity(), fragment);

                return true;
            }
        });

        FragmentUtils.replaceRevendedor(getActivity(), VendasRevendedor.newInstance());

    }
}
