package com.tecnoia.matheus.financascosmeticos.revendedor;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.adapters.AdapterProdutosVendas;
import com.tecnoia.matheus.financascosmeticos.adapters.AdapterVendasRevendedor;
import com.tecnoia.matheus.financascosmeticos.model.Produto;
import com.tecnoia.matheus.financascosmeticos.utils.GetDataFromFirebase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendasRevendedor extends Fragment {


    private ListView listViewVendas;
    private Query databaseVendas;
    private List<Produto> produtoList;

    private SharedPreferences sharedPrefRevendedor;
    private String idSupervisor;
    private String idRevendedor;
    private AdapterVendasRevendedor adapterVendas;


    public static VendasRevendedor newInstance() {
        return new VendasRevendedor();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vendas_revendedor, container, false);
        if (container != null) {
            container.removeAllViews();
        }

        recuperaDados();
        initViews(rootView);
        preencheListaVendas();
        adapterVendas = new AdapterVendasRevendedor(getActivity(), produtoList, idRevendedor, idSupervisor, listViewVendas);
        listViewVendas.setAdapter(adapterVendas);
        return rootView;

    }

    private void preencheListaVendas() {
        produtoList = new ArrayList<>();

        databaseVendas = ConfiguracoesFirebase.getListaProdutosVenda(idSupervisor, idRevendedor);
        databaseVendas.keepSynced(true);
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        databaseVendas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    produtoList.clear();

                    for (DataSnapshot snapshotProdutos : dataSnapshot.getChildren()) {
                        Produto produto = snapshotProdutos.getValue(Produto.class);
                        produtoList.add(produto);


                    }
                    adapterVendas.atualiza(produtoList);


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
        listViewVendas = rootView.findViewById(R.id.list_view_vendas_revendedor);


    }

    private void recuperaDados() {
        sharedPrefRevendedor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefRevendedor.getString("idSupervisor", "");
        idRevendedor = sharedPrefRevendedor.getString("idRevendedor", "");


    }


}
