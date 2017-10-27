package com.projeto.adrielle.cosmeticosfinancas.adapters;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.projeto.adrielle.cosmeticosfinancas.model.Produto;
import com.tecnoia.matheus.financascosmeticos.R;

import java.util.List;

/**
 * Created by matheus on 04/09/17.
 */

public class AdapterNovaVendaDialog extends ArrayAdapter {
    private ListView listViewProdutos;
    private FragmentActivity activity;
    private List<Produto> produtoList;
    private String idRevendedor;
    private String idSupervisor;
    private int produtoPosition;
    private Dialog dialog;
    private TextView textViewProduto;
    private Produto produtoSelecionado;

    public AdapterNovaVendaDialog(FragmentActivity activity, List<Produto> produtoArrayList, ListView listViewProdutos, String idSupervisor, Dialog dialog, TextView textViewSelecionaProduto, Produto produto, int positionUpdate) {
        super(activity, R.layout.adapter_dialog_vendas);
        this.activity = activity;
        this.idSupervisor = idSupervisor;
        this.produtoList = produtoArrayList;
        this.listViewProdutos = listViewProdutos;
        this.dialog = dialog;
        this.produtoSelecionado = produto;
        this.textViewProduto = textViewSelecionaProduto;
        this.produtoPosition = positionUpdate;


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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.adapter_produtos_dialog_venda, null, true);

        TextView textViewNomeProduto, textViewDisponivel;
        textViewNomeProduto = view.findViewById(R.id.text_nome_produto);
        textViewDisponivel = view.findViewById(R.id.text_disponiveis);
        final Produto produto = produtoList.get(position);
        textViewNomeProduto.setText(produto.getNome());
        textViewDisponivel.setText("Estoque: " + produto.getQuantidade());

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Produto produto1 = produtoList.get(i);
                String nome = produto1.getNome();
                textViewProduto.setText(nome);
                produtoPosition = i ;
                produtoSelecionado.setId(produto1.getId());
                produtoSelecionado.setNome(produto1.getNome());
                produtoSelecionado.setPreco(produto1.getPreco());
                produtoSelecionado.setQuantidade(produto1.getQuantidade());
                produtoSelecionado.setStatus(produto1.getStatus());
                dialog.dismiss();


            }
        });


        return view;
    }

}
