package com.tecnoia.matheus.financascosmeticos.revendedor;


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
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.adapters.AdapterItensVenda;
import com.tecnoia.matheus.financascosmeticos.adapters.AdapterNovaVendaDialog;
import com.tecnoia.matheus.financascosmeticos.model.ItemVenda;
import com.tecnoia.matheus.financascosmeticos.model.Produto;
import com.tecnoia.matheus.financascosmeticos.utils.GetDataFromFirebase;
import com.tecnoia.matheus.financascosmeticos.utils.ValidaCamposConexao;

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

    private List<ItemVenda> itemVendaList = new ArrayList<>();


    private DatabaseReference databaseVendasRealizadas;
    private ArrayList<ItemVenda> vendasRealizadasList = new ArrayList<>();
    private BigDecimal updateSaldos;
    private BigDecimal precoProduto;


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

        produto = new Produto();
        popUpSelecionarPodutos();
        return rootView;
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

                dialog.show();

            }
        });

        buttonFinalizarVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaItens();
            }
        });


    }


    private void popUpSelecionarPodutos() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view1 = inflater.inflate(R.layout.adapter_dialog_lista_produtos, null);
        final ListView listViewProdutos = view1.findViewById(R.id.list_view_produtos);


        final Query referenceProdutos;
        produtoList = new ArrayList<>();


        referenceProdutos = ConfiguracoesFirebase.getListaProdutosVenda(idSupervisor, idRevendedor);
        referenceProdutos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    produtoList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Produto produto = snapshot.getValue(Produto.class);
                        if (Integer.parseInt(produto.getQuantidade()) != 0) {
                            produtoList.add(produto);
                        }

                    }

                    adapterNovaVendaDialog.atualiza(produtoList);

                } catch (Exception e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view1);
        builder.setCancelable(false);


        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        dialog = builder.create();


        adapterNovaVendaDialog = new AdapterNovaVendaDialog(getActivity(), produtoList, listViewProdutos, idSupervisor, dialog, textViewSelecionaProduto, produto);
        listViewProdutos.setAdapter(adapterNovaVendaDialog);


    }


    private void recuperaDados() {
        sharedPrefRevendedor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefRevendedor.getString("idSupervisor", "");
        idRevendedor = sharedPrefRevendedor.getString("idRevendedor", "");


    }

    private void verificaCampos() {
        try {

            quatidadeDisponivel = Integer.parseInt(produto.getQuantidade());
            quantidadeDesejada = Integer.parseInt(editTextQuantidade.getText().toString());


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


            adicionaProdutos();


        }


    }

    private void adicionaProdutos() {


        ItemVenda itemVenda = new ItemVenda(produto.getId(), produto.getNome(), String.valueOf(quantidadeDesejada), "0");

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
            ItemVenda updateItemLista = new ItemVenda(produto.getId(), produto.getNome(), String.valueOf(updateQuantidade), produto.getPreco());

            itemVendaList.set(position, updateItemLista);
            Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT).show();
        } else {
            itemVendaList.add(itemVenda);


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
                            Log.v(saldoItensNovo+"", "saldo");


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




