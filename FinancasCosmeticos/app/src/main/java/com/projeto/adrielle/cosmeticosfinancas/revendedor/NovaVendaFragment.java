package com.projeto.adrielle.cosmeticosfinancas.revendedor;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projeto.adrielle.cosmeticosfinancas.DAO.ConfiguracoesFirebase;
import com.projeto.adrielle.cosmeticosfinancas.adapters.AdapterItensVenda;
import com.projeto.adrielle.cosmeticosfinancas.adapters.AdapterNovaVendaDialog;
import com.projeto.adrielle.cosmeticosfinancas.model.ItemVenda;
import com.projeto.adrielle.cosmeticosfinancas.model.Produto;
import com.projeto.adrielle.cosmeticosfinancas.utils.GetDataFromFirebase;
import com.tecnoia.matheus.financascosmeticos.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NovaVendaFragment extends Fragment {
    private Toolbar toolbar;
    private TextView textViewSelecionaProduto;
    private SharedPreferences sharedPrefRevendedor;
    private String idSupervisor, idRevendedor;
    private AdapterNovaVendaDialog adapterNovaVendaDialog;
    private AlertDialog dialog;
    private Produto produto;
    private Button buttonAdicionarProdutos, buttonFinalizarVenda;
    private EditText editTextQuantidade;
    private Integer quatidadeDisponivel, quantidadeDesejada;
    private ListView listViewProdutosEmSeparação;

    private BigDecimal saldoItemAtual;
    private List<Produto> produtoList = new ArrayList<>();
    private List<Produto> produtoListBanco = new ArrayList<>();

    private List<Produto> listProdutos = new ArrayList<>();


    private List<ItemVenda> itemVendaList = new ArrayList<>();

    private DatabaseReference databaseVendasRealizadas;
    private ArrayList<ItemVenda> vendasRealizadasList = new ArrayList<>();
    private BigDecimal updateSaldos;
    private BigDecimal precoProduto;
    private Query referenceProdutos;
    private int positionUpdate;


    public static NovaVendaFragment newInstance() {
        return new NovaVendaFragment();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nova_venda, container, false);
        setHasOptionsMenu(true);
        initViews(rootView);
        recuperaDados();
        vendasRealizadas();

        toolbarNovaVenda();


        carregaProdutos();
        produto = new Produto();


        return rootView;
    }

    private void carregaProdutos() {

        referenceProdutos = ConfiguracoesFirebase.getListaProdutosVenda(idSupervisor, idRevendedor);
        referenceProdutos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    produtoListBanco.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Produto produto = snapshot.getValue(Produto.class);
                        if (Integer.parseInt(produto.getQuantidade()) != 0) {
                            produtoListBanco.add(produto);


                            //

                        }

                    }
                    for (int i = 0; i < produtoListBanco.size(); i++) {


                        listProdutos.add(produtoListBanco.get(i));

                    }

                    Log.v(listProdutos.size() + "", "list_produtos");


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void toolbarNovaVenda() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Nova Venda");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();

                break;

        }


        return super.onOptionsItemSelected(item);
    }

    private void initViews(View rootView) {
        toolbar = rootView.findViewById(R.id.toolbar_nova_venda);
        buttonFinalizarVenda = rootView.findViewById(R.id.buttomFinalizarVenda);
        listViewProdutosEmSeparação = rootView.findViewById(R.id.list_view_produtos_separacao);
        textViewSelecionaProduto = rootView.findViewById(R.id.text_seleciona_produto);
        buttonAdicionarProdutos = rootView.findViewById(R.id.buttonAdicionarProdutos);
        editTextQuantidade = rootView.findViewById(R.id.edit_quantidade);
        buttonAdicionarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verificaCampos();
            }
        });


        textViewSelecionaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpSelecionarPodutos();
                dialog.show();

            }
        });

        buttonFinalizarVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  verificaItens();*/
            }
        });


    }


    private void popUpSelecionarPodutos() {


        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view1 = inflater.inflate(R.layout.adapter_dialog_lista_produtos, null);
        final ListView listViewProdutosPopUp = view1.findViewById(R.id.list_view_produtos);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view1);
        builder.setCancelable(false);


        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        dialog = builder.create();


        adapterNovaVendaDialog = new AdapterNovaVendaDialog(getActivity(), listProdutos, listViewProdutosPopUp, idSupervisor, dialog, textViewSelecionaProduto, produto, positionUpdate);
        listViewProdutosPopUp.setAdapter(adapterNovaVendaDialog);
        adapterNovaVendaDialog.atualiza(listProdutos);


    }


    private void recuperaDados() {
        sharedPrefRevendedor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefRevendedor.getString("idSupervisor", "");
        idRevendedor = sharedPrefRevendedor.getString("idRevendedor", "");


    }

    private void verificaCampos() {
        try {

            quatidadeDisponivel = Integer.parseInt(listProdutos.get(positionUpdate).getQuantidade());
            quantidadeDesejada = Integer.parseInt(editTextQuantidade.getText().toString());
            Toast.makeText(getActivity(), quatidadeDisponivel, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean cancela = false;
        editTextQuantidade.setError(null);
        textViewSelecionaProduto.setError(null);
        if (textViewSelecionaProduto.getText().toString().equals(getString(R.string.selecione_o_produto))) {
            textViewSelecionaProduto.setError(getString(R.string.selecione_o_produto_error));
            textViewSelecionaProduto.requestFocus();
            cancela = true;

        }
        if (editTextQuantidade.getText().toString().trim().isEmpty()) {
            editTextQuantidade.setError(getString(R.string.quantidade_erro));
            editTextQuantidade.requestFocus();
            cancela = true;

        }
        if ((quantidadeDesejada != null) && (quatidadeDisponivel != null)) {
            if (quantidadeDesejada > quatidadeDisponivel) {
                editTextQuantidade.setError(getString(R.string.quantidade_indisponivel));
                editTextQuantidade.requestFocus();
                cancela = true;

            }

        } else {
            Toast.makeText(getActivity(), "Selecione um produto e indique a quantidade desejada!", Toast.LENGTH_SHORT).show();
            cancela = true;
        }

        if (!cancela) {
            int pos = 0;
            for (Produto produtoPesquisa : listProdutos) {

                if (produtoPesquisa.getId().equals(produto.getId())) {
                 /* adicionaProdutos(pos);*/


                }
                pos++;
            }





            /*
            adicionaProdutos();*/


        }


    }

    private void adicionaProdutos(int pos) {
        /*BigDecimal precoUnitario = new BigDecimal(produto.getPreco());

        BigDecimal saldoItensNovo = precoUnitario.multiply(new BigDecimal(quantidadeDesejada));
        Toast.makeText(getActivity(), saldoItensNovo + "preco", Toast.LENGTH_SHORT).show();*/
        ItemVenda itemVenda = new ItemVenda(produto.getId(), produto.getNome(), String.valueOf(quantidadeDesejada), "0.00");


        boolean existete = false;
        int position = 0;
        int quantidadeItem = 0;

        //verifica se o item ja esta presente na lista
        for (ItemVenda itemVendaLista : itemVendaList) {
            if (itemVendaLista.getId().equals(produto.getId())) {
                existete = true;
                quantidadeItem = Integer.parseInt(itemVendaLista.getQuantidade());
                break;


            }
            position++;

        }
        if (existete) {

            int updateQuantidade = quantidadeDesejada + quantidadeItem;
            ItemVenda updateItemLista = new ItemVenda(produto.getId(), produto.getNome(), String.valueOf(updateQuantidade), "0.00");

            itemVendaList.set(position, updateItemLista);


            //atualizar list_produtos

            int position2 = 0;
            for (Produto produtoUpdate : listProdutos) {
                if (produtoUpdate.getId().equals(produto.getId())) {
                    int updateQuantidadeLis = Integer.parseInt(produtoUpdate.getQuantidade()) - Integer.parseInt(quantidadeDesejada.toString());
                    Produto produtoUp = new Produto(produtoUpdate.getId(), produtoUpdate.getNome(), produtoUpdate.getPreco(), String.valueOf(updateQuantidadeLis), produtoUpdate.getStatus());
                    listProdutos.set(position2, produtoUp);
                    adapterNovaVendaDialog.atualiza(listProdutos);
                   /* Toast.makeText(getActivity(), position2 + "", Toast.LENGTH_SHORT).show();*/
                }
                position2++;
            }

        } else {
            int position3 = 0;
            itemVendaList.add(itemVenda);
            for (Produto produtoUpdate : listProdutos) {
                if (produtoUpdate.getId().equals(produto.getId())) {
                    int updateQuantidadeLis = Integer.parseInt(produtoUpdate.getQuantidade()) - Integer.parseInt(quantidadeDesejada.toString());
                    Produto produtoUp = new Produto(produtoUpdate.getId(), produtoUpdate.getNome(), produtoUpdate.getPreco(), String.valueOf(updateQuantidadeLis), produtoUpdate.getStatus());
                    listProdutos.set(position3, produtoUp);
                    adapterNovaVendaDialog.atualiza(listProdutos);
                   /* Toast.makeText(getActivity(), position3 + "", Toast.LENGTH_SHORT).show();
*/

                }
                position3++;
            }

        }


        AdapterItensVenda adapterItensVenda = new AdapterItensVenda(getActivity(), itemVendaList, quantidadeDesejada);
        listViewProdutosEmSeparação.setAdapter(adapterItensVenda);
        adapterItensVenda.notifyDataSetChanged();


    }


    public void verificaItens() {

        if (itemVendaList.isEmpty()) {
            Toast.makeText(getActivity(), "0 Itens", Toast.LENGTH_SHORT).show();
        } else {

            for (ItemVenda itemVenda : itemVendaList) {
                for (Produto produto : produtoList) {

                    if (itemVenda.getId().equals(produto.getId())) {
                        BigDecimal precoUnitario = new BigDecimal(produto.getPreco());
                        int quantidadeVendido = 0;

                        String saldoItens = "";


                        boolean exitente = false;
                        // verifica se o item ja foi vendido antes
                        for (ItemVenda vendasRealizadas : vendasRealizadasList) {
                            try {
                                if (vendasRealizadas.getId().equals(produto.getId())) {
                                    exitente = true;
                                    quantidadeVendido = Integer.parseInt(vendasRealizadas.getQuantidade());

                                    saldoItens = vendasRealizadas.getSaldoItens();

                                    break;


                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        if (exitente) {


                            //prepara itens para atualizar
                            String updateQuantidade = String.valueOf(quantidadeVendido + Integer.parseInt(itemVenda.getQuantidade()));
                            BigDecimal saldoItensNovo = precoUnitario.multiply(new BigDecimal(itemVenda.getQuantidade())).add(new BigDecimal(saldoItens));
                            String saldoItemFinal;
                           /* Log.v("sal", saldoItensNovo + "");*/


                            //atualiza quantidade de itens vendidos e saldo total
                            ItemVenda novoItemVenda = new ItemVenda(itemVenda.getId(), itemVenda.getNome(), updateQuantidade, String.valueOf(saldoItensNovo));
                            novoItemVenda.novaVenda(idSupervisor, idRevendedor);


                            //atualiza produtos disponiveis
                            Integer disponivelUpdate = quatidadeDisponivel - Integer.parseInt(itemVenda.getQuantidade());
                            Integer quatidadeVendida = Integer.parseInt(produto.getStatus()) + Integer.parseInt(itemVenda.getQuantidade());

                            Produto produtoUpdate = new Produto(produto.getId(), produto.getNome(), produto.getPreco(), String.valueOf(disponivelUpdate), String.valueOf(quatidadeVendida));
                            produtoUpdate.salvaProdutoVendas(idSupervisor, idRevendedor);


                        } else {
                            //se este produto ainda não foi vendido efetua a primeira venda

                            BigDecimal saldoItensNovo = precoUnitario.multiply(new BigDecimal(itemVenda.getQuantidade()));
                            Log.v(saldoItensNovo + "", "saldo");


                            ItemVenda novoItemVenda = new ItemVenda(itemVenda.getId(), itemVenda.getNome(), itemVenda.getQuantidade(), String.valueOf(saldoItensNovo));
                            novoItemVenda.novaVenda(idSupervisor, idRevendedor);

                            //atualiza produtos disponiveis
                            Integer disponivelUpdate = quatidadeDisponivel - Integer.parseInt(itemVenda.getQuantidade());
                            Integer quatidadeVendida = Integer.parseInt(produto.getStatus()) + Integer.parseInt(itemVenda.getQuantidade());

                            Produto produtoUpdate = new Produto(produto.getId(), produto.getNome(), produto.getPreco(), String.valueOf(disponivelUpdate), String.valueOf(quatidadeVendida));
                            produtoUpdate.salvaProdutoVendas(idSupervisor, idRevendedor);


                        }


                    }

                }


            }

            itemVendaList.clear();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        }

    }


    public void vendasRealizadas() {


        databaseVendasRealizadas = ConfiguracoesFirebase.getVendasRealizadas(idSupervisor, idRevendedor);
        databaseVendasRealizadas.keepSynced(true);
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        databaseVendasRealizadas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    vendasRealizadasList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ItemVenda itemVenda = snapshot.getValue(ItemVenda.class);


                        produtoList.add(produto);

                        vendasRealizadasList.add(itemVenda);


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public String convertStringFinal(String str) {
        str = str.replace(".", "");
        str = str.replace(",", ".");
        str = str.trim();
        return str;
    }

    public String recuperaValorBigDecimal(BigDecimal bigDecimal) {
        DecimalFormat decFormat = new DecimalFormat("#,###,##0.00");
        return decFormat.format(bigDecimal);
    }


}




