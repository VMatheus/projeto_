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
import com.tecnoia.matheus.financascosmeticos.model.ItemVenda;

import java.util.List;

/**
 * Created by matheus on 04/09/17.
 */

public class AdapterVendasRealizadas extends ArrayAdapter {
    private FragmentActivity activity;
    private List<ItemVenda> itemVendaList;


    public AdapterVendasRealizadas(FragmentActivity activity, List<ItemVenda> itemVendaList) {
        super(activity, R.layout.adapter_vendas_realizadas);
        this.activity = activity;
        this.itemVendaList = itemVendaList;

    }


    public void atualiza(List<ItemVenda> itemVendaList) {
        this.itemVendaList = itemVendaList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemVendaList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater1 = activity.getLayoutInflater();
        final View view1 = inflater1.inflate(R.layout.adapter_vendas_realizadas, null);

        TextView textViewNome, textViewVendidos, textViewSaldoItens;

        textViewVendidos = view1.findViewById(R.id.text_quantidade_vendidos);
        textViewNome = view1.findViewById(R.id.text_nome_vendas_realizadas);
        textViewSaldoItens = view1.findViewById(R.id.text_saldo_itens);
        final ItemVenda itemVenda = itemVendaList.get(position);


        textViewSaldoItens.setText("R$" + itemVenda.getSaldoItens());


        textViewNome.setText(itemVenda.getNome());
        textViewVendidos.setText("Vendidos: " + itemVenda.getQuantidade());

        return view1;

    }
}
