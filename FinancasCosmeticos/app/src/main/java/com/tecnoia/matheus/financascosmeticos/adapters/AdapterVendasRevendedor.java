package com.tecnoia.matheus.financascosmeticos.adapters;

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

import java.util.List;

/**
 * Created by matheus on 03/09/17.
 */

public class AdapterVendasRevendedor extends ArrayAdapter {
    private List<Produto> produtoList;
    private FragmentActivity activity;
    private String idSupervisor, idRevendedor;


    public AdapterVendasRevendedor(FragmentActivity activity, List<Produto> produtoList, String idRevendedor, String idSupervisor) {
        super(activity, R.layout.adapter_vendas);
        this.produtoList = produtoList;
        this.idRevendedor = idRevendedor;
        this.idSupervisor = idSupervisor;
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return produtoList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void atualiza(List<Produto> produtoList) {
        this.produtoList = produtoList;
        this.notifyDataSetChanged();


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.adapter_vendas, null, true);

        TextView nome, preco, quantidade;
        nome = view.findViewById(R.id.text_nome_produto_venda);
        preco = view.findViewById(R.id.text_preco_produto_venda);

        quantidade = view.findViewById(R.id.text_disponiveis_avenda);

        final Produto produto = produtoList.get(position);
        nome.setText(produto.getNome());
        preco.setText(String.format("%s R$", produto.getPreco()));
        quantidade.setText(activity.getResources().getString(R.string.disponiveis_a_venda)+ produto.getQuantidade());

        return view;
    }
}
