package com.projeto.adrielle.cosmeticosfinancas.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projeto.adrielle.cosmeticosfinancas.model.ItemVenda;
import com.projeto.adrielle.cosmeticosfinancas.model.Produto;
import com.projeto.adrielle.cosmeticosfinancas.R;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by matheus on 05/09/17.
 */

public class AdapterItensVenda extends ArrayAdapter {
    private FragmentActivity activity;
    private List<ItemVenda> itemVendaList;
    private List<Produto> produtoList;
    private Integer quantidadeDesejada;
    private TextView textViewTotal;


    public AdapterItensVenda(FragmentActivity activity, List<ItemVenda> itemVendaList, Integer quantidadeDesejada, List<Produto> listProdutos) {
        super(activity, R.layout.adapter_nova_venda);
        this.itemVendaList = itemVendaList;
        this.activity = activity;
        this.quantidadeDesejada = quantidadeDesejada;
        this.produtoList = listProdutos;


    }

    @Override
    public int getCount() {
        return itemVendaList.size();

    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.adapter_nova_venda, null, true);
        final TextView textViewNome, textViewUnidades, textViewSaldoItens;
        ImageView imageViewRemover;

        textViewNome = view.findViewById(R.id.text_nome_separacao);
        textViewUnidades = view.findViewById(R.id.text_unidades_separacao);

        imageViewRemover = view.findViewById(R.id.image_remover_separacao);
        textViewSaldoItens = view.findViewById(R.id.text_saldo_itens);

        final ItemVenda itemVenda = itemVendaList.get(position);
        textViewNome.setText(itemVenda.getNome());
        textViewSaldoItens.setText(itemVenda.getSaldoItens());
        textViewTotal = activity.findViewById(R.id.total);


        textViewUnidades.setText(String.format("%sUn", itemVenda.getQuantidade()));
        imageViewRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = 0;
                for (Produto produto : produtoList) {
                    if (produto.getId().equals(itemVenda.getId())) {
                        int quantidadeUpdate = Integer.parseInt(itemVenda.getQuantidade()) + Integer.parseInt(produto.getQuantidade());
                        Produto produto1 = new Produto(produto.getId(), produto.getNome(), produto.getPreco(), String.valueOf(quantidadeUpdate), produto.getStatus());
                        produtoList.set(pos, produto1);
                        BigDecimal total = new BigDecimal(textViewTotal.getText().toString()).subtract(new BigDecimal(itemVenda.getSaldoItens()));


                        textViewTotal.setText(String.valueOf(total));

                    }
                    pos++;

                }

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
