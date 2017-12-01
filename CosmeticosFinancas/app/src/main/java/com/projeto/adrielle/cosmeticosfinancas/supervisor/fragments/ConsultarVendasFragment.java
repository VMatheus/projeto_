package com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projeto.adrielle.cosmeticosfinancas.DAO.ConfiguracoesFirebase;
import com.projeto.adrielle.cosmeticosfinancas.adapters.AdapterConsultarVendas;
import com.projeto.adrielle.cosmeticosfinancas.model.Produto;
import com.projeto.adrielle.cosmeticosfinancas.model.Revendedor;
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;
import com.projeto.adrielle.cosmeticosfinancas.utils.GetDataFromFirebase;
import com.projeto.adrielle.cosmeticosfinancas.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class ConsultarVendasFragment extends Fragment {
    private FloatingActionButton buttonAdicionarProdutos;

    private Button buttonSalvar;
    private ArrayList<Produto> produtosListVendas, produtosListEstoque;
    private Query databaseProdutoVenda, databaseProdutoEstoque;
    private SharedPreferences sharedPrefSupervisor;
    private String idSupervisor;
    private TextView textViewInf;
    private String idRevendedora;
    private ListView listViewProdutosVenda;
    private AdapterConsultarVendas adapterConsultarVendas;
    private Toolbar toolbar;
    private String nomeRevendedor, email, senha, photoUrl, pathImagem, numero, saldoTotal;

    private TextView textViewSaldoTotal;
    private DatabaseReference referencePerfil;


    public static ConsultarVendasFragment newInstance() {

        return new ConsultarVendasFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_consultar_vendas, container, false);
        if (container != null) {
            container.removeAllViews();
        }
        recuperaDados();
        setHasOptionsMenu(true);


        carrecarListaProdutosVenda();
        carregarListeProdutosEstoque();



        initViews(rootView);
        carregarPerfil();
        toolbarVendas();

        adapterConsultarVendas = new AdapterConsultarVendas(getActivity(), produtosListVendas, listViewProdutosVenda, idSupervisor, produtosListEstoque, idRevendedora);
        listViewProdutosVenda.setAdapter(adapterConsultarVendas);


        return (rootView);

    }

    private void carregarPerfil() {
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        referencePerfil = FirebaseDatabase.getInstance().getReference(idRevendedora);
        referencePerfil.keepSynced(true);
        referencePerfil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Revendedor revendedor = dataSnapshot.getValue(Revendedor.class);
                    nomeRevendedor = revendedor.getNome();
                    email= revendedor.getEmail();
                    senha = revendedor.getSenha();
                    photoUrl = revendedor.getPhotoUrl();
                    pathImagem  = revendedor.getPathImagem();
                    numero = revendedor.getNumero();
                    textViewSaldoTotal.setText("Saldo de Vendas: R$ " +revendedor.getSaldoTotal());
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(nomeRevendedor);





                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void carregarListeProdutosEstoque() {
        produtosListEstoque = new ArrayList<>();


        databaseProdutoEstoque = ConfiguracoesFirebase.getListaProdutosEstoque(idSupervisor);

        databaseProdutoEstoque.keepSynced(true);
        databaseProdutoEstoque.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    for (DataSnapshot snapshotEstoque : dataSnapshot.getChildren()) {
                        Produto produto = snapshotEstoque.getValue(Produto.class);
                        produtosListEstoque.add(produto);

                    }
                    adapterConsultarVendas.atualizaEstoque(produtosListEstoque);

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void toolbarVendas() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Vendas");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void carrecarListaProdutosVenda() {
        produtosListVendas = new ArrayList<>();

        databaseProdutoVenda = ConfiguracoesFirebase.getListaProdutosVenda(idSupervisor, idRevendedora);

        databaseProdutoVenda.keepSynced(true);


        databaseProdutoVenda.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if(!dataSnapshot.exists()) {
                        textViewInf.setVisibility(View.VISIBLE);

                    }
                    produtosListVendas.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Produto produto = snapshot.getValue(Produto.class);

                        produtosListVendas.add(produto);

                    }


                    adapterConsultarVendas.atualiza(produtosListVendas);


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(e + "", "Erro_database_produtos_estoque");

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();

                break;

            case R.id.action_limpar_estoque_rev:
                //fazer


                break;

            case R.id.action_zerar_saldo:
                zerarSaldo();

                break;


        }


        return super.onOptionsItemSelected(item);
    }

    private void zerarSaldo() {

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());

        builder.setMessage("Zerar saldo de vendas?");
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {





                /*
*/

            }
        });


        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Revendedor revendedorZerarSaldo = new Revendedor(idRevendedora, idSupervisor, nomeRevendedor, email,senha, photoUrl, pathImagem, numero,"0.00");
                revendedorZerarSaldo.atualizaRevendedor(idSupervisor, idRevendedora);
                dialog.dismiss();

            }


        });


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_consultar_vendas, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void initViews(View rootView) {
        textViewInf = rootView.findViewById(R.id.text_info);
        toolbar = rootView.findViewById(R.id.toolbar_consultar_vendas);
        listViewProdutosVenda = rootView.findViewById(R.id.list_view_produtos_venda);
        buttonAdicionarProdutos = rootView.findViewById(R.id.floating_button_adicionar_produtos_venda);
        textViewSaldoTotal = rootView.findViewById(R.id.text_saldo_total);
        buttonAdicionarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("idRevendedora", idRevendedora);
                Fragment fragment = SelecionarProdutos.newInstance();
                fragment.setArguments(bundle);
                FragmentUtils.replaceRetorno(getActivity(), fragment);


            }
        });


    }


    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idRevendedora = bundle.getString("idRevendedora");

        }


    }


}
