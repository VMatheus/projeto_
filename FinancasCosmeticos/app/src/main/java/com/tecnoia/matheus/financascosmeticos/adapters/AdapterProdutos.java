package com.tecnoia.matheus.financascosmeticos.adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
 * Created by matheus on 25/08/17.
 */

public class AdapterProdutos extends ArrayAdapter {
    private String idSupervisor;

    private FragmentActivity activity;
    private List<Produto> produtoList;
    private ListView listView;

    private BottomSheetDialog dialog;

    public AdapterProdutos(FragmentActivity activity, List<Produto> produtoList, ListView listViewProdutos, String idSupervisor) {
        super(activity, R.layout.adapter_produtos, produtoList);
        this.activity = activity;
        this.produtoList = produtoList;
        this.listView = listViewProdutos;
        this.idSupervisor = idSupervisor;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.adapter_produtos, null, true);

        TextView nome, preco, quantidade;
        nome = view.findViewById(R.id.text_nome_produto_estoque);
        preco = view.findViewById(R.id.text_preco_produto_estoque);
        quantidade = view.findViewById(R.id.text_quantidade_produto_estoque);

        final Produto produto = produtoList.get(position);
        nome.setText(produto.getNome());
        preco.setText(String.format("%s R$", produto.getPreco()));
        quantidade.setText(String.format(String.format("Estoque: %s", produto.getQuantidade())));

        try {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Produto produto1 = produtoList.get(i);

                    init_modal_bottomsheet(produto1);
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

                bundle.putString("idProduto", produto1.getId());
                bundle.putString("nomeProduto", produto1.getNome());
                bundle.putString("precoProduto", String.valueOf(produto1.getPreco()));
                bundle.putString("quantidadeProduto", produto1.getQuantidade());

                Fragment fragment = CadastroProdutoFragment.newInstance();
                fragment.setArguments(bundle);
                FragmentUtils.replaceRetorno(activity, fragment);
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
