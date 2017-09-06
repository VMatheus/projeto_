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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.adapters.AdapterConsultarVendas;
import com.tecnoia.matheus.financascosmeticos.model.Produto;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class ConsultarVendasFragment extends Fragment {
    private FloatingActionButton buttonAdicionarProdutos;

    private Button buttonSalvar;
    private ArrayList<Produto> produtosListVendas, produtosListEstoque;
    private Query databaseProdutoVenda, databaseProdutoEstoque;
    private SharedPreferences sharedPrefSupervisor;
    private String idSupervisor;
    private String idRevendedor;
    private ListView listViewProdutosVenda;
    private AdapterConsultarVendas adapterConsultarVendas;
    private Toolbar toolbar;
    private String nomeRevendedor;



    public static ConsultarVendasFragment newInstance() {

        return new ConsultarVendasFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_consultar_vendas, container, false);
        if (container != null) {
            container.removeAllViews();
        }
        recuperaDados();
        setHasOptionsMenu(true);


        carrecarListaProdutosVenda();
        carregarListeProdutosEstoque();


        initViews(rootView);
        toolbarVendas();
        adapterConsultarVendas = new AdapterConsultarVendas(getActivity(), produtosListVendas, listViewProdutosVenda, idSupervisor, produtosListEstoque, idRevendedor);
        listViewProdutosVenda.setAdapter(adapterConsultarVendas);


        return (rootView);

    }

    private void carregarListeProdutosEstoque() {
        produtosListEstoque = new ArrayList<>();


        databaseProdutoEstoque = ConfiguracoesFirebase.getListaProdutosEstoque(idSupervisor);

        databaseProdutoEstoque.keepSynced(true);
        databaseProdutoEstoque.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshotEstoque : dataSnapshot.getChildren()) {
                        Produto produto = snapshotEstoque.getValue(Produto.class);
                        produtosListEstoque.add(produto);

                    }
                    adapterConsultarVendas.atualizaEstoque(produtosListEstoque);

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void toolbarVendas() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Vendas");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(nomeRevendedor);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void carrecarListaProdutosVenda() {
        produtosListVendas = new ArrayList<>();

        databaseProdutoVenda = ConfiguracoesFirebase.getListaProdutosVenda(idSupervisor, idRevendedor);

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


                    adapterConsultarVendas.atualiza(produtosListVendas);


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
        toolbar = rootView.findViewById(R.id.toolbar_consultar_vendas);
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
