package com.tecnoia.matheus.financascosmeticos.revendedor;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendasRealizadas extends Fragment {
    private ListView listViewVendasRealizadas ;
    

    public static VendasRealizadas newInstance() {


        return new VendasRealizadas();
    }

    private ListView listViewVendas;
    private FloatingActionButton buttonNovaVenda;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vendas_realizadas, container, false);

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView) {
        listViewVendas = rootView.findViewById(R.id.list_view_vendas_realizadas);
        buttonNovaVenda = rootView.findViewById(R.id.floating_button_nova_venda);
        buttonNovaVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentUtils.replacePrincipalRetorno(getActivity(), NovaVendaFragment.newInstance());
            }
        });



    }


}
