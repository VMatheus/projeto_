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
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.adapters.AdapterProdutosVendas;
import com.tecnoia.matheus.financascosmeticos.model.Produto;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class VendasFragment extends Fragment {
    private FloatingActionButton buttonAdicionarProdutos;

    private Button buttonSalvar;
    private ArrayList<Produto> produtosList;
    private Query databaseProdutosEstoque;
    private SharedPreferences sharedPrefSupervisor;
    private String idSupervisor;
    private String idRevendedor;
    private ListView listViewProdutosVenda;
    private AdapterProdutosVendas adapterProdutos;
    private Toolbar toolbar;
    private String nomeRevendedor;


    public static VendasFragment newInstance() {

        return new VendasFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vendas, container, false);
        recuperaDados();
        setHasOptionsMenu(true);


        carrecarListaProdutosEstoque();

        initViews(rootView);
        toolbarVendas();
        adapterProdutos = new AdapterProdutosVendas(getActivity(), produtosList, listViewProdutosVenda, idSupervisor);
        listViewProdutosVenda.setAdapter(adapterProdutos);
        if (container != null) {
            container.removeAllViews();
        }


        return (rootView);

    }

    private void toolbarVendas() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Vendas");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(nomeRevendedor);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void carrecarListaProdutosEstoque() {
        produtosList = new ArrayList<>();


        databaseProdutosEstoque = FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_VENDAS + "/" + idRevendedor).orderByChild("nome");


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


                    adapterProdutos.atualiza(produtosList);


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

    private void initViews(View rootView) {
        toolbar = rootView.findViewById(R.id.toolbar_vendas);
        listViewProdutosVenda = rootView.findViewById(R.id.list_view_produtos_venda);
        buttonAdicionarProdutos = rootView.findViewById(R.id.floating_button_adicionar_produtos_venda);

      buttonAdicionarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("idRevendedor", idRevendedor);
                Fragment fragment = SelecionarProdutos.newInstance();
                fragment.setArguments(bundle);
                FragmentUtils.replaceRetorno(getActivity(), fragment);


            }
        });





    }


    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idRevendedor = bundle.getString("idRevendedor");
            nomeRevendedor = bundle.getString("nomeRevendedor");

        }


    }


}
