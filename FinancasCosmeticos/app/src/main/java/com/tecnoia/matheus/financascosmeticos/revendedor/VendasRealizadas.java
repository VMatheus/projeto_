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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.adapters.AdapterVendasRealizadas;
import com.tecnoia.matheus.financascosmeticos.model.ItemVenda;
import com.tecnoia.matheus.financascosmeticos.model.Produto;
import com.tecnoia.matheus.financascosmeticos.model.Revendedor;
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
    private TextView textViewSaldoTotal;
    private String idSupervisor, idRevendedor, saldoTotal;
    private DatabaseReference databaseVendasRealizadas;
    private AdapterVendasRealizadas adapterVendasRealizadas;
    private DatabaseReference databaseDadosRevendedor;

    private List<Revendedor> dadosRevendedorList = new ArrayList<>();


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
        dadosRevendedor();


        adapterVendasRealizadas = new AdapterVendasRealizadas(getActivity(), itemVendaList);
        listViewVendas.setAdapter(adapterVendasRealizadas);


        return rootView;
    }

    private void dadosRevendedor() {

/*

        databaseDadosRevendedor = ConfiguracoesFirebase.getConsultaDadosRevendedor(idSupervisor, idRevendedor).child("saldoTotal");
        databaseDadosRevendedor.keepSynced(true);
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        databaseDadosRevendedor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                textViewSaldoTotal.setText(String.valueOf(dataSnapshot.getValue(Double.class)));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

*/


        databaseDadosRevendedor = ConfiguracoesFirebase.getConsultaDadosRevendedor(idSupervisor, idRevendedor);
        databaseDadosRevendedor.keepSynced(true);
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        databaseDadosRevendedor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Revendedor revendedor = dataSnapshot.getValue(Revendedor.class);
                    saldoTotal = revendedor.getSaldoTotal();
                    String saldoNew = saldoTotal;
                    textViewSaldoTotal.setText(saldoNew +" R$");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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

                        ItemVenda itemVenda7 = snapshot.getValue(ItemVenda.class);

                        itemVendaList.add(itemVenda7);


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
        textViewSaldoTotal = rootView.findViewById(R.id.text_saldo_total);
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
