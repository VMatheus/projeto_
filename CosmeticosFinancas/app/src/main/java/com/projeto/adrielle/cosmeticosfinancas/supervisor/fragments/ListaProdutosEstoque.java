package com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments;

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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projeto.adrielle.cosmeticosfinancas.DAO.ConfiguracoesFirebase;
import com.projeto.adrielle.cosmeticosfinancas.adapters.AdapterProdutos;
import com.projeto.adrielle.cosmeticosfinancas.model.Produto;
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;
import com.projeto.adrielle.cosmeticosfinancas.R;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class ListaProdutosEstoque extends Fragment {
    private FloatingActionButton floatingActionButton;
    private ListView listViewProdutos;
    private Query databaseProdutosEstoque;
    private SharedPreferences sharedPrefSupervisor;
    private String idSupervisor;
    private ArrayList<Produto> produtosList;
    private AdapterProdutos adapterProdutos;
    private TextView textViewInfo;

    //BottomSheetDialog


    public static ListaProdutosEstoque newInstance() {

        return new ListaProdutosEstoque();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_lista_produtos_estoque, container, false);
        initViews(rootview);
        recuperaDados();

        preencheLista();
        adapterProdutos = new AdapterProdutos(getActivity(), produtosList, listViewProdutos, idSupervisor);
        listViewProdutos.setAdapter(adapterProdutos);
        if (container != null) {
            container.removeAllViews();
        }
        return rootview;

    }


    private void preencheLista() {
        produtosList = new ArrayList<>();

        databaseProdutosEstoque = ConfiguracoesFirebase.getListaProdutosEstoque(idSupervisor);

        databaseProdutosEstoque.keepSynced(true);


        databaseProdutosEstoque.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (!dataSnapshot.exists()) {
                        textViewInfo.setVisibility(View.VISIBLE);
                    }
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

    private void initViews(View rootview) {


        listViewProdutos = rootview.findViewById(R.id.list_view_produtos_estoque);
        floatingActionButton = rootview.findViewById(R.id.floating_button_adicionar_produtos);
        textViewInfo = rootview.findViewById(R.id.text_info);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentUtils.replaceRetorno(getActivity(), CadastroProdutoFragment.newInstance());

            }
        });


    }

    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");


    }
}
