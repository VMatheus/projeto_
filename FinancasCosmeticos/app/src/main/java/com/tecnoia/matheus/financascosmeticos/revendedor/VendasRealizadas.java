package com.tecnoia.matheus.financascosmeticos.revendedor;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.adapters.AdapterVendasRealizadas;
import com.tecnoia.matheus.financascosmeticos.model.ItemVenda;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;
import com.tecnoia.matheus.financascosmeticos.utils.GetDataFromFirebase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendasRealizadas extends Fragment {

    private ListView listViewVendas;
    private FloatingActionButton buttonNovaVenda;
    private List<ItemVenda> itemVendaList;
    private SharedPreferences sharedPrefRevendedor;
    private String idSupervisor, idRevendedor;
    private DatabaseReference databaseVendasRealizadas;
    private AdapterVendasRealizadas adapterVendasRealizadas;


    public static VendasRealizadas newInstance() {


        return new VendasRealizadas();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vendas_realizadas, container, false);

        initViews(rootView);
        recuperaDados();
        preencheLista();

        adapterVendasRealizadas = new AdapterVendasRealizadas(getActivity(), itemVendaList);
        listViewVendas.setAdapter(adapterVendasRealizadas);




        return rootView;
    }

    private void preencheLista() {
        itemVendaList = new ArrayList<>();


        databaseVendasRealizadas = ConfiguracoesFirebase.getVendasRealizadas(idSupervisor, idRevendedor);
        databaseVendasRealizadas.keepSynced(true);
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        databaseVendasRealizadas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    itemVendaList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ItemVenda itemVenda = snapshot.getValue(ItemVenda.class);

                        itemVendaList.add(itemVenda);


                    }
                    adapterVendasRealizadas.atualiza(itemVendaList);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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

    private void recuperaDados() {
        sharedPrefRevendedor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefRevendedor.getString("idSupervisor", "");
        idRevendedor = sharedPrefRevendedor.getString("idRevendedor", "");


    }


}
