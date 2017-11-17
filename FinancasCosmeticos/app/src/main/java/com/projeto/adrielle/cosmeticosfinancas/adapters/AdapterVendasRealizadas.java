package com.projeto.adrielle.cosmeticosfinancas.adapters;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
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
import android.widget.Toast;

import com.projeto.adrielle.cosmeticosfinancas.model.ItemVenda;
import com.projeto.adrielle.cosmeticosfinancas.model.Produto;
import com.projeto.adrielle.cosmeticosfinancas.model.Revendedor;
import com.tecnoia.matheus.financascosmeticos.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 04/09/17.
 */

public class AdapterVendasRealizadas extends ArrayAdapter {
    private FragmentActivity activity;
    private List<ItemVenda> itemVendaList;
    private List<Produto> listProdutos;
    private ListView listViewVendas;
    private BottomSheetDialog dialog;
    private boolean existente = false;
    private String idRevendedor, idSupervisor;
    private Revendedor revendedor;
    private String nome, email, numero, senha, photoUrl, pathImg, saldoTotalEd;


    public AdapterVendasRealizadas(FragmentActivity activity, List<ItemVenda> itemVendaList, ListView listViewVendas, ArrayList<Produto> listProdutos, String idRevendedor, String idSupervisor, String nome, String email, String numero, String senha, String photoUrl, String pathImg, String saldoTotalEd) {
        super(activity, R.layout.adapter_vendas_realizadas);
        this.activity = activity;
        this.itemVendaList = itemVendaList;
        this.listViewVendas = listViewVendas;
        this.listProdutos = listProdutos;
        this.idRevendedor = idRevendedor;
        this.idSupervisor = idSupervisor;
        this.revendedor = revendedor;
        this.nome = nome;
        this.email = email;
        this.numero = numero;
        this.senha = senha;
        this.photoUrl = photoUrl;
        this.pathImg = pathImg;
        this.saldoTotalEd = saldoTotalEd;

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
        listViewVendas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ItemVenda itemVenda1 = itemVendaList.get(i);
               /* dialogCancelarVenda(itemVenda1);*/
                return true;
            }
        });
        return view1;

    }

    private void dialogCancelarVenda(final ItemVenda itemVenda1) {
        View modalbottomsheet = activity.getLayoutInflater().inflate(R.layout.modal_bottomsheet_vendas, null);

        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        dialog.show();
        LinearLayout linearLayoutAtualizar = modalbottomsheet.findViewById(R.id.linear_atualizar);
        linearLayoutAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //instruções


                dialogCancelar(itemVenda1);
                dialog.dismiss();


            }
        });
        LinearLayout linearLayoutRemover = modalbottomsheet.findViewById(R.id.linear_remover);
        linearLayoutRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //instruções

                dialog.dismiss();


            }
        });


    }

    private void dialogCancelar(final ItemVenda itemVenda1) {

        LayoutInflater inflater1 = activity.getLayoutInflater();
        final View view1 = inflater1.inflate(R.layout.adapter_dialog_cancelar_vendas, null);

        final TextView textViewUnidadesVendidas = view1.findViewById(R.id.unidades_vendidas);
        final TextView textViewSaldoItem = view1.findViewById(R.id.saldo_item);
        final EditText editTextQuantidad = view1.findViewById(R.id.edit_quantidade_dialog);


        textViewUnidadesVendidas.setText(itemVenda1.getQuantidade());
        textViewSaldoItem.setText(itemVenda1.getSaldoItens() + " R$");
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(itemVenda1.getNome());
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

                if (!editTextQuantidad.getText().toString().trim().isEmpty()) {
                    int valorCampo = Integer.parseInt(editTextQuantidad.getText().toString());
                    int quantidadeVendida = Integer.parseInt(itemVenda1.getQuantidade());
                    String idProduto;
                    BigDecimal saldoItems = new BigDecimal(itemVenda1.getSaldoItens());
                    BigDecimal saldoUnitario;
                    BigDecimal novoSaldo = new BigDecimal(0);
                    BigDecimal updateSaldo = new BigDecimal(0);
                    Produto produto2 = new Produto();

                    if (valorCampo <= quantidadeVendida) {
                        int novoValor = quantidadeVendida - valorCampo;
                        for (Produto produto : listProdutos) {
                            if (produto.getId().equals(itemVenda1.getId())) {
                                existente = true;
                                produto2 = produto;
                                saldoUnitario = new BigDecimal(produto.getPreco());
                                idProduto = produto.getId();
                                BigDecimal oper = new BigDecimal(produto.getPreco()).multiply(new BigDecimal(valorCampo));
                                novoSaldo = saldoItems.subtract(oper);


                                break;


                            }


                        }
                        if (existente) {
/*
                            //atualiza itens vendidos

                            ItemVenda itemVenda = new ItemVenda(itemVenda1.getId(), itemVenda1.getNome(), String.valueOf(novoValor), String.valueOf(novoSaldo));
                            itemVenda.novaVenda(idSupervisor, idRevendedor);


                            //atualiza produtos disponiveis para venda

                            int updateQuantidade = Integer.parseInt(produto2.getQuantidade()) + valorCampo;
                            int updateStatus = Integer.parseInt(produto2.getStatus()) - valorCampo;
                            Produto produto3 = new Produto(produto2.getId(), produto2.getNome(), produto2.getPreco(), String.valueOf(updateQuantidade), String.valueOf(updateStatus));
                            produto3.salvaProdutoVendas(idSupervisor, idRevendedor);
*/


                            //atualiza saldo
                            Toast.makeText(activity, ""+ saldoTotalEd, Toast.LENGTH_SHORT).show();
                          /*  BigDecimal updateSaldo2 = new BigDecimal(saldoTotalEd).subtract(new BigDecimal(String.valueOf(novoSaldo)));
                            Toast.makeText(activity, ""+ String.valueOf(updateSaldo2), Toast.LENGTH_SHORT).show();
                        /*    Revendedor revendedor = new Revendedor(idRevendedor, idSupervisor, nome, email, senha, photoUrl, pathImg, numero, String.valueOf(updateSaldo));
                            revendedor.atualizaRevendedor(idSupervisor, idRevendedor);
*/

                        }

                    }
                    if (valorCampo > quantidadeVendida) {
                        Toast.makeText(activity, "Valores insuficientes", Toast.LENGTH_SHORT).show();
                    }


                    /*else {

                    }*/
                   /* if (valorCampo == quantidadeVendida) {
                        //deletar

                    }*/


                }



               /* //atualiza banco
                int valorInicial = Integer.parseInt(produto1.getQuantidade());
                int valorCampo = Integer.parseInt(editTextUniddades.getText().toString());
                int valorAtualizado = valorInicial - valorCampo;


                Produto produtoUpdate = new Produto(produto1.getId(), produto1.getNome(), produto1.getPreco(), String.valueOf(valorAtualizado), produto1.getStatus());
                produtoUpdate.atualizarProduto(idSupervisor, produto1.getId());

                Produto produtoRevendedor = new Produto(produto1.getId(), produto1.getNome(), produto1.getPreco(), String.valueOf(valorCampo), produto1.getStatus());

                produtoRevendedor.salvaProdutoVendas(idSupervisor, idRevendedor);

                //retorna tela dos produtos em venda
                FragmentManager fm = activity.getSupportFragmentManager();
                fm.popBackStack();*/


            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}

