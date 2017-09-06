package com.tecnoia.matheus.financascosmeticos.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.ItemVenda;
import com.tecnoia.matheus.financascosmeticos.model.Produto;

import java.util.List;

/**
 * Created by matheus on 05/09/17.
 */

public class AdapterItensVenda extends ArrayAdapter {
    private FragmentActivity activity;
    private List<ItemVenda> itemVendaList;
    private Integer quantidadeDesejada;


    public AdapterItensVenda(FragmentActivity activity, List<ItemVenda> itemVendaList, Integer quantidadeDesejada) {
        super(activity, R.layout.adapter_produtos_separacao);
        this.itemVendaList = itemVendaList;
        this.activity = activity;
        this.quantidadeDesejada = quantidadeDesejada;
    }

    @Override
    public int getCount() {
        return itemVendaList.size();

    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.adapter_produtos_separacao, null, true);
        TextView textViewNome, textViewUnidades;
        ImageView imageViewRemover;
        textViewNome = view.findViewById(R.id.text_nome_separacao);
        textViewUnidades = view.findViewById(R.id.text_unidades_separacao);

        imageViewRemover = view.findViewById(R.id.image_remover_separacao);

        final ItemVenda itemVenda = itemVendaList.get(position);
        textViewNome.setText(itemVenda.getNome());
        textViewUnidades.setText(String.format("%sUn", itemVenda.getQuantidade()));
        imageViewRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemVendaList.remove(itemVenda);
                atualiza(itemVendaList);

            }
        });

        return view;
    }

    public void atualiza(List<ItemVenda> itemVendas) {
        this.itemVendaList = itemVendas;
        this.notifyDataSetChanged();
    }
}
