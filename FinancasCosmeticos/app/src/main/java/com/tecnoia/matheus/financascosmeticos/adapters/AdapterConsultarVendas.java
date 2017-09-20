package com.tecnoia.matheus.financascosmeticos.adapters;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.Produto;
import com.tecnoia.matheus.financascosmeticos.utils.ValidaCamposConexao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 31/08/17.
 */

public class AdapterConsultarVendas extends ArrayAdapter {

    private String idSupervisor;

    private Activity activity;
    private List<Produto> produtoListVendas, produtoListEstoque;
    private ListView listView;
    private BottomSheetDialog dialog;
    private String idRevendedor;
    private Integer quantidadeEstoque;


    public AdapterConsultarVendas(FragmentActivity activity, List<Produto> produtoListVendas, ListView listViewProdutos, String idSupervisor, List<Produto> produtosListEstoque, String idRevendedor) {
        super(activity, R.layout.adapter_vendas, produtoListVendas);
        this.activity = activity;
        this.produtoListVendas = produtoListVendas;
        this.listView = listViewProdutos;
        this.idSupervisor = idSupervisor;
        this.produtoListEstoque = produtosListEstoque;
        this.idRevendedor = idRevendedor;


    }

    @Override
    public int getCount() {
        return produtoListVendas.size();
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
        final TextView nome, preco, quantidade, status;
        nome = view.findViewById(R.id.text_nome_produto_venda);
        preco = view.findViewById(R.id.text_preco_produto_venda);
        quantidade = view.findViewById(R.id.text_disponiveis_avenda);


        status = view.findViewById(R.id.tex_status_vendidos);


        final Produto produto = produtoListVendas.get(position);

        nome.setText(produto.getNome());

        BigDecimal precoUnitario = ValidaCamposConexao.formatStringToBigDecimal(produto.getPreco());
        BigDecimal quantidadeVendida = ValidaCamposConexao.formatStringToBigDecimal(produto.getStatus());
        BigDecimal saldoItens = precoUnitario.multiply(quantidadeVendida);

        preco.setText("R$ " + ValidaCamposConexao.formataBigDecimalToString(saldoItens));


        quantidade.setText(activity.getString(R.string.itens_a_venda) + " " + produto.getQuantidade());
        status.setText(String.format("Vendidos: %s", produto.getStatus()));
        Integer statusV = Integer.parseInt(produto.getStatus());
        if (statusV.equals(0)) {
            sendNotification(view);
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Produto produtoVenda = produtoListVendas.get(i);
                initBottomsheet(produtoVenda);


                return true;
            }
        });
        return view;
    }

    public void atualiza(ArrayList<Produto> produtosList) {
        this.produtoListVendas = produtosList;
        this.notifyDataSetChanged();

    }

    public void initBottomsheet(final Produto produtoVenda) {
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


                popUpAtualizar(produtoVenda);
                dialog.dismiss();


            }
        });
        LinearLayout linearLayoutRemover = modalbottomsheet.findViewById(R.id.linear_remover);
        linearLayoutRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpRemoverProduto(produtoVenda);
               /*
*/
                dialog.dismiss();


            }
        });

    }

    private void popUpRemoverProduto(final Produto produtoVenda) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("Remover este produto das vendas?");
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {




                /*
*/

            }
        });


        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer quantidadeEmEstoque = null;
                Integer quantidadeAvenda = Integer.parseInt(produtoVenda.getQuantidade());

                Integer quantidadeUpdate;
                boolean existente = false;

                for (Produto produtoEstoque : produtoListEstoque) {

                    if (produtoEstoque.getId().equals(produtoVenda.getId())) {
                        quantidadeEmEstoque = Integer.parseInt(produtoEstoque.getQuantidade());


                        quantidadeUpdate = quantidadeAvenda + quantidadeEmEstoque;

                        //atualiza estoque
                        Produto produtoUpdateEstoque = new Produto(produtoEstoque.getId(), produtoEstoque.getNome(), produtoEstoque.getPreco(), String.valueOf(quantidadeUpdate), produtoEstoque.getStatus());
                        produtoUpdateEstoque.atualizarProduto(idSupervisor, produtoEstoque.getId());
                        produtoVenda.removeProdutoVenda(idSupervisor, produtoVenda.getId(), idRevendedor);
                        dialog.dismiss();
                        existente = true;
                        break;
                    }


                }
                if (!existente) {
                    produtoVenda.removeProdutoVenda(idSupervisor, produtoVenda.getId(), idRevendedor);
                    dialog.dismiss();

                }


            }
        });


    }

    private void popUpAtualizar(final Produto produtoVenda) {

        final Integer iniciofornecidos = Integer.parseInt(produtoVenda.getQuantidade());


        LayoutInflater inflater1 = activity.getLayoutInflater();
        final View view1 = inflater1.inflate(R.layout.adapter_dialog_vendas, null);

        final TextView textViewEstoque = view1.findViewById(R.id.unidades_disponiveis_dialog_vendas);
        final TextView textViewPreco = view1.findViewById(R.id.preco_dialog_vendas);

        final EditText editTextFornecidos = view1.findViewById(R.id.edit_fornecidos_vendas);

        editTextFornecidos.setError(null);

        int z;
        try {
            for (z = 0; z <= produtoListEstoque.size(); z++) {
                if (produtoVenda.getId().equals(produtoListEstoque.get(z).getId())) {
                    textViewEstoque.setText(String.format("%s uds.", produtoListEstoque.get(z).getQuantidade()));
                    quantidadeEstoque = Integer.parseInt(produtoListEstoque.get(z).getQuantidade());
                    editTextFornecidos.setText(produtoVenda.getQuantidade());

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        textViewPreco.setText(String.format("%s R$", produtoVenda.getPreco()));
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(produtoVenda.getNome());
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

        final AlertDialog dialog = builder.create();
        dialog.show();

        //validando e pegando referencia do banco pra atualizações

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextFornecidos.setError(null);
                String valorCampo = editTextFornecidos.getText().toString();


                View focusView = null;
                boolean cancel = false;
                if (TextUtils.isEmpty(valorCampo)) {
                    editTextFornecidos.setError(activity.getString(R.string.campo_vazio));
                    focusView = editTextFornecidos;
                    cancel = true;

                } else if (!ValidaCamposConexao.validaValorEstoque(Integer.parseInt(produtoVenda.getQuantidade()), quantidadeEstoque, Integer.parseInt(valorCampo))) {
                    editTextFornecidos.setError(activity.getString(R.string.estoque_insuficiente));
                    focusView = editTextFornecidos;
                    cancel = true;

                }
                if (cancel) {


                    focusView.requestFocus();

                } else {
                    Integer diferenca, novoValor = Integer.parseInt(editTextFornecidos.getText().toString());

                    if (novoValor > iniciofornecidos) {
                        diferenca = novoValor - iniciofornecidos;
                        Log.e(String.valueOf(diferenca), "lll");

                        // adicionar a diferença no estoque  e salva novo valor em produtos vendas e atualiza estoque;
                        int z;
                        try {
                            for (z = 0; z <= produtoListEstoque.size(); z++) {
                                if (produtoVenda.getId().equals(produtoListEstoque.get(z).getId())) {
                                    //UpdateProdutoVendas
                                    Produto produtoVendaUpdate = new Produto(produtoVenda.getId(), produtoVenda.getNome(), produtoVenda.getPreco(), editTextFornecidos.getText().toString(), produtoVenda.getStatus());
                                    produtoVendaUpdate.salvaProdutoVendas(idSupervisor, idRevendedor);


                                    //UpdateProdutoEstoque
                                    Produto produtoEstoque = produtoListEstoque.get(z);
                                    Integer updateEstoqueQuantidade = Integer.parseInt(produtoEstoque.getQuantidade()) - diferenca;


                                    Produto produtoUpdateEstoque = new Produto(produtoEstoque.getId(), produtoEstoque.getNome(), produtoEstoque.getPreco(), String.valueOf(updateEstoqueQuantidade), produtoEstoque.getStatus());
                                    produtoUpdateEstoque.atualizarProduto(idSupervisor, produtoEstoque.getId());


                                }

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else if (novoValor < iniciofornecidos) {
                        diferenca = iniciofornecidos - novoValor;

                        int z;
                        try {
                            for (z = 0; z <= produtoListEstoque.size(); z++) {
                                if (produtoVenda.getId().equals(produtoListEstoque.get(z).getId())) {
                                    //UpdateProdutoVendas
                                    Produto produtoVendaUpdate = new Produto(produtoVenda.getId(), produtoVenda.getNome(), produtoVenda.getPreco(), editTextFornecidos.getText().toString(), produtoVenda.getStatus());
                                    produtoVendaUpdate.salvaProdutoVendas(idSupervisor, idRevendedor);


                                    //UpdateProdutoEstoque
                                    Produto produtoEstoque = produtoListEstoque.get(z);
                                    Integer quantidadeEstoque = Integer.parseInt(produtoEstoque.getQuantidade()) + diferenca;


                                    Produto produtoUpdateEstoque = new Produto(produtoEstoque.getId(), produtoEstoque.getNome(), produtoEstoque.getPreco(), String.valueOf(quantidadeEstoque), produtoEstoque.getStatus());
                                    produtoUpdateEstoque.atualizarProduto(idSupervisor, produtoEstoque.getId());


                                }

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                    dialog.dismiss();
                }


            }
        });


    }

    public void atualizaEstoque(ArrayList<Produto> produtosListE) {
        this.produtoListEstoque = produtosListE;
        this.notifyDataSetChanged();

    }

    public void sendNotification(View view) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity);

//Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.androidauthority.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
        mBuilder.setContentTitle("My notification");
        mBuilder.setContentText("Hello World!");

        NotificationManager mNotificationManager =

                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }
}
