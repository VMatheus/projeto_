package com.projeto.adrielle.cosmeticosfinancas.revendedor;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projeto.adrielle.cosmeticosfinancas.DAO.ConfiguracoesFirebase;
import com.projeto.adrielle.cosmeticosfinancas.adapters.AdapterVendasRealizadas;
import com.projeto.adrielle.cosmeticosfinancas.model.ItemVenda;
import com.projeto.adrielle.cosmeticosfinancas.model.Produto;
import com.projeto.adrielle.cosmeticosfinancas.model.Revendedor;
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;
import com.projeto.adrielle.cosmeticosfinancas.utils.GetDataFromFirebase;
import com.tecnoia.matheus.financascosmeticos.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendasRealizadas extends Fragment {
    private Toolbar toolbar;
    private ListView listViewVendas;
    private FloatingActionButton buttonNovaVenda;
    private List<ItemVenda> itemVendaList;
    private SharedPreferences sharedPrefRevendedor;
    private TextView textViewSaldoTotal, textViewInf;
    private String idSupervisor, idRevendedor, saldoTotal;
    private DatabaseReference databaseVendasRealizadas;
    private AdapterVendasRealizadas adapterVendasRealizadas;
    private DatabaseReference databaseDadosRevendedor;
    private String nome, email, numero, senha, photoUrl, pathImg, saldoTotalEd;


    private List<Revendedor> dadosRevendedorList = new ArrayList<>();
    private ArrayList<Produto> listProdutos = new ArrayList<>();
    private Revendedor revendedor;
    private Revendedor revendedor1;


    public static VendasRealizadas newInstance() {


        return new VendasRealizadas();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vendas_realizadas, container, false);

        initViews(rootView);
        setHasOptionsMenu(true);


        recuperaDados();

        preencheLista();
        carregaProdutos();
        dadosRevendedor();
        toolbarVendas();




        return rootView;
    }

    private void toolbarVendas() {



        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.vendas));

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vendas, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void carregaProdutos() {

        Query referenceProdutos = ConfiguracoesFirebase.getListaProdutosVenda(idSupervisor, idRevendedor);
        referenceProdutos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    listProdutos.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Produto produto = snapshot.getValue(Produto.class);

                        listProdutos.add(produto);


                        //

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
                    revendedor = dataSnapshot.getValue(Revendedor.class);
                    saldoTotal = revendedor.getSaldoTotal();
                    textViewSaldoTotal.setText("Saldo de Vendas: R$ " + revendedor.getSaldoTotal());

                    dados(revendedor);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void dados(Revendedor revendedor) {
        revendedor1 = revendedor;
        adapterVendasRealizadas = new AdapterVendasRealizadas(getActivity(), itemVendaList, listViewVendas, listProdutos, idRevendedor, idSupervisor, revendedor1);
        listViewVendas.setAdapter(adapterVendasRealizadas);
        Log.v("REV", revendedor1.getNome());



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
                    if (!dataSnapshot.exists()) {
                        textViewInf.setVisibility(View.VISIBLE);
                    }
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
        toolbar = rootView.findViewById(R.id.toolbar_vendas);
        textViewSaldoTotal = rootView.findViewById(R.id.text_saldo_total);
        textViewInf = rootView.findViewById(R.id.text_info);
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
