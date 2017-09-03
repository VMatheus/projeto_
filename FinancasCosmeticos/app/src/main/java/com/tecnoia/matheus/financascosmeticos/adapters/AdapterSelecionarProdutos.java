package com.tecnoia.matheus.financascosmeticos.adapters;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.Produto;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 27/08/17.
 */

public class AdapterSelecionarProdutos extends ArrayAdapter {

    private FragmentActivity activity;
    private List<Produto> produtoList;
    private FloatingActionButton floatingActionButton;
    private ListView listViewProdutos;
    private String idRevendedor, idSupervisor;

    public AdapterSelecionarProdutos(FragmentActivity activity, List<Produto> produtoList, FloatingActionButton buttonSalvar, ListView listViewSelecionarProdutos, String idSupervisor, String idRevendedor) {
        super(activity, R.layout.adapter_produtos, produtoList);
        this.activity = activity;
        this.produtoList = produtoList;
        this.floatingActionButton = buttonSalvar;

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



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "floatingActionButton", Toast.LENGTH_SHORT).show();
            }
        });
        final Produto produto = produtoList.get(position);
        textViewNome.setText(produto.getNome());
        textViewPreco.setText(String.format("%s R$", produto.getPreco()));
        textViewDisponivel.setText(String.format("Estoque: %s", produto.getQuantidade()));
        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {


                final Produto produto1 = produtoList.get(i);
                final String idProduto, nome, preco, quantidade,status;
                idProduto = produto1.getId();
                nome = produto1.getNome();
                preco = produto1.getPreco();
                quantidade = produto1.getQuantidade();
                status = produto1.getStatus();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(idSupervisor+"/"+ConstantsUtils.BANCO_PRODUTOS_VENDAS+"/"+idRevendedor +"/"+ idProduto);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                            if(dataSnapshot.exists()){

                                Snackbar.make(activity.findViewById(R.id.container_principal_supervisor), R.string.mensagem_produto_ja_adicionado, Snackbar.LENGTH_LONG).show();

                            }else {

                                procedimentoNormal();




                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }





                    }




                    private void procedimentoNormal() {

                        LayoutInflater inflater1 = activity.getLayoutInflater();
                        final View view1 = inflater1.inflate(R.layout.adapter_dialog_selecionar, null);

                        final TextView textViewUnidadesDisponiveis = view1.findViewById(R.id.unidades_disponiveis_dialog);
                        final TextView textViewPreco = view1.findViewById(R.id.preco_dialog);
                        final EditText editTextUniddades = view1.findViewById(R.id.edit_quantidade_dialog);


                        textViewUnidadesDisponiveis.setText(String.format("%s uds.", quantidade));
                        textViewPreco.setText(String.format("%s R$", preco));
                        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(nome);
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
                                int valorInicial = Integer.parseInt(quantidade);
                                int valorCampo = Integer.parseInt(editTextUniddades.getText().toString());
                                int valorAtualizado = valorInicial - valorCampo;


                                Produto produtoUpdate = new Produto(idProduto, nome, preco, String.valueOf(valorAtualizado),status);
                                produtoUpdate.atualizarProduto(idSupervisor, idProduto);

                                Produto produtoRevendedor = new Produto(idProduto, nome, preco, String.valueOf(valorCampo),status);

                                produtoRevendedor.salvaProdutoVendas(idSupervisor, idRevendedor);

                                //retorna tela dos produtos em venda
                                FragmentManager fm = activity.getSupportFragmentManager();
                                fm.popBackStack();


                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });


        return view;
    }

    public void atualiza(ArrayList<Produto> produtosList) {
        this.produtoList = produtosList;
        this.notifyDataSetChanged();

    }







}
