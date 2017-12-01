package com.projeto.adrielle.cosmeticosfinancas.adapters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.projeto.adrielle.cosmeticosfinancas.R;
import com.projeto.adrielle.cosmeticosfinancas.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 27/08/17.
 */

public class AdapterSelecionarProdutos extends ArrayAdapter {

    private FragmentActivity activity;
    private List<Produto> produtoListEstoque;
    private List<Produto> produtoListVendas;

    private ListView listViewProdutos;
    private String idRevendedor, idSupervisor;


    public AdapterSelecionarProdutos(FragmentActivity activity, List<Produto> produtosListEstoque, List<Produto> produtosListVendas, ListView listViewSelecionarProdutos, String idSupervisor, String idRevendedor) {
        super(activity, R.layout.adapter_produtos, produtosListEstoque);
        this.activity = activity;
        this.produtoListEstoque = produtosListEstoque;
        this.produtoListVendas = produtosListVendas;


        this.listViewProdutos = listViewSelecionarProdutos;
        this.idRevendedor = idRevendedor;
        this.idSupervisor = idSupervisor;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.adapter_produtos, null, true);

        final TextView textViewNome, textViewPreco, textViewDisponivel;


        textViewNome = view.findViewById(R.id.text_nome_produto_estoque);
        textViewPreco = view.findViewById(R.id.text_preco_produto_estoque);
        textViewDisponivel = view.findViewById(R.id.text_quantidade_produto_estoque);




        final Produto produto = produtoListEstoque.get(position);
        textViewNome.setText(produto.getNome());
        textViewPreco.setText(String.format("%s R$", produto.getPreco()));
        textViewDisponivel.setText(String.format("Estoque: %s", produto.getQuantidade()));


        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {


                Produto produto1 = produtoListEstoque.get(i);

                Integer quantidadeDispopnivel = Integer.parseInt(produto1.getQuantidade());
                if (quantidadeDispopnivel > 0) {
                    verificaItem(produto1);


                } else {


                    Snackbar.make(activity.findViewById(R.id.container_principal_supervisor), "Quantidade em estoque insuficiente.", Snackbar.LENGTH_LONG).show();

                }



            }
        });


        return view;
    }

    private void verificaItem(final Produto produto) {
        String id = produto.getId();

        if (produtoListVendas.isEmpty()) {
            //procedimento normal
            //...

            procedimentoNormal(produto);

        } else {
            boolean existente = false;

            for (Produto produtoVendas : produtoListVendas) {

                if (produtoVendas.getId().equals(id) ){

                    existente = true;
                    break;
                }


            }
            if (existente) {
                Snackbar.make(activity.findViewById(R.id.container_principal_supervisor), R.string.mensagem_produto_ja_adicionado, Snackbar.LENGTH_LONG).show();

            } else {

                procedimentoNormal(produto);

            }
        }
    }


    public void atualizaProdutosVenda(ArrayList<Produto> produtosListVendas) {
        this.produtoListVendas = produtosListVendas;
        this.notifyDataSetChanged();


    }

    public void atualizaEstoque(ArrayList<Produto> produtosListEstoque) {
        this.produtoListEstoque = produtosListEstoque;
        this.notifyDataSetChanged();

    }


    private void procedimentoNormal(final Produto produto1) {

        LayoutInflater inflater1 = activity.getLayoutInflater();
        final View view1 = inflater1.inflate(R.layout.adapter_dialog_selecionar, null);

        final TextView textViewUnidadesDisponiveis = view1.findViewById(R.id.unidades_disponiveis_dialog);
        final TextView textViewPreco = view1.findViewById(R.id.preco_dialog);
        final EditText editTextUniddades = view1.findViewById(R.id.edit_quantidade_dialog);


        textViewUnidadesDisponiveis.setText(String.format("%s uds.", produto1.getQuantidade()));
        textViewPreco.setText(String.format("%s R$", produto1.getPreco()));
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(produto1.getNome());
        builder.setView(view1);
        builder.setCancelable(false);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                //atualiza banco
                int valorInicial = Integer.parseInt(produto1.getQuantidade());
                int valorCampo = Integer.parseInt(editTextUniddades.getText().toString());
                int valorAtualizado = valorInicial - valorCampo;


                Produto produtoUpdate = new Produto(produto1.getId(), produto1.getNome(), produto1.getPreco(), String.valueOf(valorAtualizado), produto1.getStatus());
                produtoUpdate.atualizarProduto(idSupervisor, produto1.getId());

                Produto produtoRevendedor = new Produto(produto1.getId(), produto1.getNome(), produto1.getPreco(), String.valueOf(valorCampo), produto1.getStatus());

                produtoRevendedor.salvaProdutoVendas(idSupervisor, idRevendedor);

                //retorna tela dos produtos em venda
                FragmentManager fm = activity.getSupportFragmentManager();
                fm.popBackStack();


            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }


}
