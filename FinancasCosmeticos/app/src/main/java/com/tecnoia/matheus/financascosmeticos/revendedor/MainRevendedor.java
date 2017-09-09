package com.tecnoia.matheus.financascosmeticos.revendedor;

import android.content.Intent;
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
    private int saveState;

    public static MainRevendedor newInstance() {
        return new MainRevendedor();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_revendedor, container, false);
        if (container != null) {
            container.removeAllViews();
        }



        initViews(rootView);


        return rootView;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bottomNavigationView.setSelectedItemId(saveState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState = bottomNavigationView.getSelectedItemId();
    }

    private void initViews(View rootView) {
        bottomNavigationView = rootView.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.item__vendas:
                        fragment = VendasRealizadas.newInstance();


                        break;

                    case R.id.item_perfil_revendedor:
                        fragment = PerfilRevendedor.newInstance();
                        break;

                    case R.id.item_produtos:
                        fragment = ProdutosRevendedor.newInstance();
                        break;
                }


                FragmentUtils.replaceRevendedor(getActivity(), fragment);

                return true;
            }
        });

        FragmentUtils.replaceRevendedor(getActivity(), VendasRealizadas.newInstance());

    }
}
