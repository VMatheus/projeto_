package com.tecnoia.matheus.financascosmeticos.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 27/08/17.
 */

public class AdapterSelecionarProdutos extends ArrayAdapter {

    private Activity activity;
    private List<Produto> produtoList;
    private FloatingActionButton floatingActionButton;
    private ListView listViewProdutos;

    public AdapterSelecionarProdutos(FragmentActivity activity, List<Produto> produtoList, FloatingActionButton buttonSalvar, ListView listViewSelecionarProdutos) {
        super(activity, R.layout.adapter_produtos, produtoList);
        this.activity = activity;
        this.produtoList = produtoList;
        this.floatingActionButton = buttonSalvar;

        this.listViewProdutos = listViewSelecionarProdutos;


    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.adapter_produtos, null, true);

        TextView textViewNome, textViewPreco;



        textViewNome = view.findViewById(R.id.text_nome_produto_estoque);
        textViewPreco = view.findViewById(R.id.text_preco_produto_estoque);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "floatingActionButton", Toast.LENGTH_SHORT).show();
            }
        });
        final Produto produto = produtoList.get(position);
        textViewNome.setText(produto.getNome());
        textViewPreco.setText(produto.getPreco());
        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Produto produto1 = produtoList.get(i);

                LayoutInflater inflater1 = activity.getLayoutInflater();
                View view1 = inflater1.inflate(R.layout.adapter_dialog_selecionar, null);

                final  TextView textViewUnidadesDisponiveis = view1.findViewById(R.id.unidades_disponiveis_dialog);
                textViewUnidadesDisponiveis.setText(produto1.getQuantidade()+" uds.");
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

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });



        return view;
    }

    public void atualiza(ArrayList<Produto> produtosList) {
        this.produtoList = produtosList;
        this.notifyDataSetChanged();

    }
}
