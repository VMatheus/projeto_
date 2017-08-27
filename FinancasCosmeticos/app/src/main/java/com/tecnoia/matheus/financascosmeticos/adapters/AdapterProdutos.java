package com.tecnoia.matheus.financascosmeticos.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 25/08/17.
 */

public class AdapterProdutos extends ArrayAdapter {

    private Activity activity;
    private List<Produto> produtoList;

    public AdapterProdutos(FragmentActivity activity, List<Produto> produtoList) {
        super(activity, R.layout.adapter_produtos, produtoList);
        this.activity = activity;
        this.produtoList = produtoList;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.adapter_produtos, null, true);

        TextView nome, preco;
        nome = view.findViewById(R.id.text_nome_produto_estoque);
        preco = view.findViewById(R.id.text_preco_produto_estoque);

        Produto produto = produtoList.get(position);
        nome.setText(produto.getNome());
        preco.setText(produto.getPreco());


        return view;
    }

    public void atualiza(ArrayList<Produto> produtosList) {
        this.produtoList = produtosList;
        this.notifyDataSetChanged();

    }
}
