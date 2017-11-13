package com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments;

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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projeto.adrielle.cosmeticosfinancas.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.projeto.adrielle.cosmeticosfinancas.adapters.AdapterSelecionarProdutos;
import com.projeto.adrielle.cosmeticosfinancas.model.Produto;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class SelecionarProdutos extends Fragment {
    private ListView listViewSelecionarProdutos;

    private ArrayList<Produto> produtosListEstoque;
    private SharedPreferences sharedPrefSupervisor;
    private String idSupervisor;

    private AdapterSelecionarProdutos adapterSelecionarProdutos;
    private String idRevendedora;
    private Toolbar toolbar;
    private ArrayList<Produto> produtosListVendas;
    private Query databaseProdutoVenda, databaseProdutoEstoque;
    private TextView textViewInfo;


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
        carrecarListaProdutosVenda();
        carregarListeProdutosEstoque();

        toolbarSelecionarProdutos();

        adapterSelecionarProdutos = new AdapterSelecionarProdutos(getActivity(), produtosListEstoque, produtosListVendas, listViewSelecionarProdutos, idSupervisor, idRevendedora);
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
         textViewInfo = rootview.findViewById(R.id.text_info);

        listViewSelecionarProdutos = rootview.findViewById(R.id.list_view_selecionar_produtos);
        toolbar = rootview.findViewById(R.id.toolbar_selecionar_produtos);


    }


    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idRevendedora = bundle.getString("idRevendedora");

        }


    }

    private void carregarListeProdutosEstoque() {
        produtosListEstoque = new ArrayList<>();


        databaseProdutoEstoque = ConfiguracoesFirebase.getListaProdutosEstoque(idSupervisor);

        databaseProdutoEstoque.keepSynced(true);
        databaseProdutoEstoque.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    textViewInfo.setVisibility(View.VISIBLE);
                }
                try {
                    for (DataSnapshot snapshotEstoque : dataSnapshot.getChildren()) {
                        Produto produto = snapshotEstoque.getValue(Produto.class);
                        produtosListEstoque.add(produto);

                    }
                    adapterSelecionarProdutos.atualizaEstoque(produtosListEstoque);

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void carrecarListaProdutosVenda() {
        produtosListVendas = new ArrayList<>();

        databaseProdutoVenda = ConfiguracoesFirebase.getListaProdutosVenda(idSupervisor, idRevendedora);

        databaseProdutoVenda.keepSynced(true);


        databaseProdutoVenda.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    produtosListVendas.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Produto produto = snapshot.getValue(Produto.class);

                        produtosListVendas.add(produto);

                    }


                    adapterSelecionarProdutos.atualizaProdutosVenda(produtosListVendas);


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


}


