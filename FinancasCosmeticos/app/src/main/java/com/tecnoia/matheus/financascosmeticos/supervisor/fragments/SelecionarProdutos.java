package com.tecnoia.matheus.financascosmeticos.supervisor.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.adapters.AdapterSelecionarProdutos;
import com.tecnoia.matheus.financascosmeticos.model.Produto;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class SelecionarProdutos extends Fragment {
    private ListView listViewSelecionarProdutos;
    private FloatingActionButton buttonSalvar;
    private ArrayList<Produto> produtosList;
    private SharedPreferences sharedPrefSupervisor;
    private String idSupervisor;
    private Query databaseProdutosEstoque;
    private AdapterSelecionarProdutos adapterSelecionarProdutos;
    private String idRevendedor;
    private Toolbar toolbar;


    public static SelecionarProdutos newInstance() {

        return new SelecionarProdutos();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_selecionar_produtos, container, false);
        if (container != null) {
            container.removeAllViews();
        }
        setHasOptionsMenu(true);
        recuperaDados();
        initViews(rootview);

        preencheLista();
        toolbarSelecionarProdutos();

        adapterSelecionarProdutos = new AdapterSelecionarProdutos(getActivity(), produtosList, buttonSalvar, listViewSelecionarProdutos, idSupervisor, idRevendedor);
        listViewSelecionarProdutos.setAdapter(adapterSelecionarProdutos);

        return rootview;
    }

    private void toolbarSelecionarProdutos() {

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.selecione_produtos));

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

    private void initViews(View rootview) {
        buttonSalvar = rootview.findViewById(R.id.floating_button_selecionar_produtos);
        listViewSelecionarProdutos = rootview.findViewById(R.id.list_view_selecionar_produtos);
        toolbar = rootview.findViewById(R.id.toolbar_selecionar_produtos);


    }

    private void preencheLista() {
        produtosList = new ArrayList<>();

        databaseProdutosEstoque = ConfiguracoesFirebase.getListaProdutosEstoque(idSupervisor);


        databaseProdutosEstoque.keepSynced(true);


        databaseProdutosEstoque.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    produtosList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Produto produto = snapshot.getValue(Produto.class);

                        produtosList.add(produto);
                    }

                    adapterSelecionarProdutos.atualiza(produtosList);


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(e + "", "Erro_database_produtos_estoque");

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idRevendedor = bundle.getString("idRevendedor");

        }


    }


}


