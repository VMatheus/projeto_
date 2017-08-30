package com.tecnoia.matheus.financascosmeticos.supervisor.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.Produto;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class VendasFragment extends Fragment  {
    private FloatingActionButton buttonAdicionarProdutos;

    private Button buttonSalvar;
    private ArrayList<Produto> produtosList;
    private Query databaseProdutosEstoque;
    private SharedPreferences sharedPrefSupervisor;
    private String idSupervisor;



    public static VendasFragment newInstance() {

        return new VendasFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vendas, container, false);
        recuperaDados();
        carrecarListaProdutosEstoque();

        initViews(rootView);


        return (rootView);

    }

    private void carrecarListaProdutosEstoque() {
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

                     /*produtos_adapter = new ArrayAdapter<Produto>(getActivity(), android.R.layout.simple_spinner_dropdown_item, produtosList);
                    produtos_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

*//*
                    adapterProdutos.atualiza(produtosList);*/


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

    private void initViews(View rootView) {
        buttonAdicionarProdutos = rootView.findViewById(R.id.floating_button_adicionar_produtos_venda);

        buttonAdicionarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    FragmentUtils.replaceRetorno(getActivity(), SelecionarProdutos.newInstance());

            }
        });
    }




    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");


    }



}
