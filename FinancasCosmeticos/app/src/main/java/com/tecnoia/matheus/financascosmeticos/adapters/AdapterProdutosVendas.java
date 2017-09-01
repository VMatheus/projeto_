package com.tecnoia.matheus.financascosmeticos.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.Produto;
import com.tecnoia.matheus.financascosmeticos.supervisor.fragments.CadastroProdutoFragment;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 31/08/17.
 */

public class AdapterProdutosVendas extends ArrayAdapter {

    private String idSupervisor;

    private Activity activity;
    private List<Produto> produtoList;
    private ListView listView;
    private BottomSheetDialog dialog;


    public AdapterProdutosVendas(FragmentActivity activity, List<Produto> produtoList, ListView listViewProdutos, String idSupervisor) {
        super(activity, R.layout.adapter_vendas, produtoList);
        this.activity = activity;
        this.produtoList = produtoList;
        this.listView = listViewProdutos;
        this.idSupervisor = idSupervisor;


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

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.adapter_vendas, null, true);
        TextView nome, preco, quantidade;
        nome = view.findViewById(R.id.text_nome_produto_venda);
        preco = view.findViewById(R.id.text_preco_produto_venda);
        quantidade = view.findViewById(R.id.text_adquiridos_inicial);

        final Produto produto = produtoList.get(position);
        nome.setText(produto.getNome());
        preco.setText(String.format("%s R$", produto.getPreco()));
        quantidade.setText(String.format("Forneciodos: %s", produto.getQuantidade()));


        try {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    init_modal_bottomsheet(produto);
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public void atualiza(ArrayList<Produto> produtosList) {
        this.produtoList = produtosList;
        this.notifyDataSetChanged();

    }

    public void init_modal_bottomsheet(final Produto produto1) {
        View modalbottomsheet = activity.getLayoutInflater().inflate(R.layout.modal_bottomsheet, null);

        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        LinearLayout linearLayoutAtualizar = modalbottomsheet.findViewById(R.id.linear_atualizar);
        linearLayoutAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();


                dialog.dismiss();


            }
        });
        LinearLayout linearLayoutRemover = modalbottomsheet.findViewById(R.id.linear_remover);
        linearLayoutRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                produto1.removerProdutoEstoque(idSupervisor, produto1.getId());


                dialog.dismiss();


            }
        });

    }
}
