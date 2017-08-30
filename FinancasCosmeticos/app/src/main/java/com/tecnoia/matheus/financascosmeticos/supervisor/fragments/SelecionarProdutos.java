package com.tecnoia.matheus.financascosmeticos.supervisor.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.adapters.AdapterProdutos;
import com.tecnoia.matheus.financascosmeticos.adapters.AdapterSelecionarProdutos;
import com.tecnoia.matheus.financascosmeticos.model.Produto;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;

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


    public static SelecionarProdutos newInstance() {

        return new SelecionarProdutos();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_selecionar_produtos, container, false);
        recuperaDados();
        initViews(rootview);
        preencheLista();
        adapterSelecionarProdutos = new AdapterSelecionarProdutos(getActivity(), produtosList, buttonSalvar, listViewSelecionarProdutos);
        listViewSelecionarProdutos.setAdapter(adapterSelecionarProdutos);

        return rootview;
    }

    private void initViews(View rootview) {
        buttonSalvar = rootview.findViewById(R.id.floating_button_selecionar_produtos);
        listViewSelecionarProdutos = rootview.findViewById(R.id.list_view_selecionar_produtos);






    }

    private void preencheLista() {
        produtosList = new ArrayList<>();

        databaseProdutosEstoque = FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_ESTOQUE).orderByChild("nome");


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


    }


}


