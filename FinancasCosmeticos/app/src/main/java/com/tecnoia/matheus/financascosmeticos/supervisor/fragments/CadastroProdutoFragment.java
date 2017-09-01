package com.tecnoia.matheus.financascosmeticos.supervisor.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.Produto;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CadastroProdutoFragment extends Fragment {
    private EditText editTextNome, editTextPreco;
    private Button buttonAdicionar;
    private String nome;
    private String preco;
    private String quantidade;
    private View focusView = null;
    private boolean cancel = false;
    private SharedPreferences sharedPrefSupervisor;
    private String idSupervisor;
    private List<String> listCategoria;
    private Spinner spinnerCategoria;
    private String categoria;
    private EditText editTextQuantidade;
    private String idProdutoEdit, nomeProdutoEdit, precoProdutoEdit, quantidadeProdutoEdit;
    private String idProduto;


    public static CadastroProdutoFragment newInstance() {
        return new CadastroProdutoFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_cadastro_produto_fragment, container, false);
        initViews(rootView);
        recuperaDados();

        if (container != null) {
            container.removeAllViews();
        }


        return rootView;


    }

    private void initViews(View rootView) {
        editTextNome = rootView.findViewById(R.id.edit_nome_produto);
        editTextPreco = rootView.findViewById(R.id.edit_preco_produto);
        buttonAdicionar = rootView.findViewById(R.id.btn_adicionar_produto);
        spinnerCategoria = rootView.findViewById(R.id.spinner_categoria_produto);
        editTextQuantidade = rootView.findViewById(R.id.edit_quantidade_produto);
        editTextNome.setError(null);
        editTextPreco.setError(null);
        editTextQuantidade.setError(null);
        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validacao();
            }
        });


    }


    private void validacao() {

        nome = editTextNome.getText().toString();
        preco = editTextPreco.getText().toString();
        quantidade = editTextQuantidade.getText().toString();


        if (TextUtils.isEmpty(nome)) {
            editTextNome.setError(getString(R.string.campo_vazio));
            focusView = editTextNome;
            cancel = true;

        }
        if (TextUtils.isEmpty(preco)) {
            editTextPreco.setError(getString(R.string.campo_vazio));
            focusView = editTextPreco;
            cancel = true;

        }
        if (TextUtils.isEmpty(quantidade)) {
            editTextQuantidade.setError(getString(R.string.campo_vazio));
            focusView = editTextQuantidade;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();

        } else {
            cadastraProduto();
        }

    }

    private void cadastraProduto() {


        Produto produto = new Produto(idProduto, nome, preco, quantidade);
        produto.salvarProduto(idSupervisor);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();


    }


    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");
        Bundle bundle = this.getArguments();
        if (bundle != null) {


            //preparadadosEditar
            idProdutoEdit = bundle.getString("idProduto");
            editTextNome.setText(nomeProdutoEdit = bundle.getString("nomeProduto"));
            editTextPreco.setText(precoProdutoEdit = bundle.getString("precoProduto"));
            editTextQuantidade.setText(quantidadeProdutoEdit = bundle.getString("quantidadeProduto"));

            idProduto = idProdutoEdit;
            buttonAdicionar.setText(R.string.atualizar);
        } else {
            idProduto = ConfiguracoesFirebase.getFirebase().push().getKey();
        }


    }


}

