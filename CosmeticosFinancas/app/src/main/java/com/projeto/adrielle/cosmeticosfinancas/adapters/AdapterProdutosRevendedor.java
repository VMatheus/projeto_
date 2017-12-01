package com.projeto.adrielle.cosmeticosfinancas.adapters;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.projeto.adrielle.cosmeticosfinancas.R;
import com.projeto.adrielle.cosmeticosfinancas.model.Produto;

import java.util.List;

/**
 * Created by matheus on 03/09/17.
 */

public class AdapterProdutosRevendedor extends ArrayAdapter {
    private List<Produto> produtoList;
    private FragmentActivity activity;
    private String idSupervisor, idRevendedor;
    private ListView listViewVendas;


    public AdapterProdutosRevendedor(FragmentActivity activity, List<Produto> produtoList, String idRevendedor, String idSupervisor, ListView listViewVendas) {
        super(activity, R.layout.adapter_vendas);
        this.produtoList = produtoList;
        this.idRevendedor = idRevendedor;
        this.idSupervisor = idSupervisor;
        this.activity = activity;
        this.listViewVendas = listViewVendas;

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

        LinearLayout linearLayout;
        TextView textViewNome, textViewPreco, textViewQuantidade, textViewStatus;
        textViewNome = view.findViewById(R.id.text_nome_produto_venda);
        textViewPreco = view.findViewById(R.id.text_preco_produto_venda);
        linearLayout = view.findViewById(R.id.linear_adapter_vendas);

        textViewQuantidade = view.findViewById(R.id.text_disponiveis_avenda);
        textViewStatus = view.findViewById(R.id.tex_status_vendidos);

        final Produto produto = produtoList.get(position);
        textViewNome.setText(produto.getNome());
        textViewPreco.setText("R$ "+produto.getPreco().toString());
        textViewQuantidade.setText("Disponivel: " + produto.getQuantidade() );
        textViewStatus.setText(String.format("Vendidos: %s", produto.getStatus()));
        if (produto.getQuantidade().equals("0")) {
           textViewQuantidade.setTextColor(Color.RED);
        }
        listViewVendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Produto produto1 = produtoList.get(i);

              /*  alertDialog(produto1);*/


            }
        });


        return view;
    }

    private void alertDialog(Produto produto1) {
        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.adapter_dialog_revendedor, null);

        final TextView textViewDisponivel = view.findViewById(R.id.text_disponiveis);
        final TextView textViewPreco = view.findViewById(R.id.preco_dialog_vendas);
        final EditText editTextVendidos = view.findViewById(R.id.edit_vendidos);
        editTextVendidos.setError(null);

        textViewDisponivel.setText(produto1.getQuantidade());
        textViewPreco.setText(String.format("%s R$", produto1.getPreco()));
        editTextVendidos.setText(produto1.getStatus());


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(produto1.getNome());
        builder.setView(view);
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

        final AlertDialog dialog = builder.create();
        dialog.show();


    }
}
