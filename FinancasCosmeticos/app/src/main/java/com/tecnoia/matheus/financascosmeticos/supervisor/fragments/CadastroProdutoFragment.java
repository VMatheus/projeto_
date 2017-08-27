package com.tecnoia.matheus.financascosmeticos.supervisor.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.Produto;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CadastroProdutoFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private EditText editTextNome, editTextPreco;
    private Button buttonAdicionar;
    private String nome;
    private String preco;
    private View focusView = null;
    private boolean cancel = false;
    private SharedPreferences sharedPrefSupervisor;
    private String idSupervisor;
    private List<String> listCategoria;
    private Spinner spinnerCategoria;
    private String categoria;


    public static CadastroProdutoFragment newInstance() {
        return new CadastroProdutoFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_cadastro_produto_fragment, container, false);
        recuperaDados();
        initViews(rootView);
        criaArrayListCategorias();

        return rootView;


    }

    private void criaArrayListCategorias() {
        listCategoria = new ArrayList<String>();

        listCategoria.add(getString(R.string.cat_1));

        listCategoria.add(getString(R.string.cat_2));
        listCategoria.add(getString(R.string.cat_3));
        listCategoria.add(getString(R.string.cat_4));
        listCategoria.add(getString(R.string.cat_5));

        ArrayAdapter<String> adapterCategoriAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listCategoria);
        spinnerCategoria.setAdapter(adapterCategoriAdapter);

    }

    private void initViews(View rootView) {
        editTextNome = rootView.findViewById(R.id.edit_nome_produto);
        editTextPreco = rootView.findViewById(R.id.edit_preco_produto);
        buttonAdicionar = rootView.findViewById(R.id.btn_adicionar_produto);
        spinnerCategoria = rootView.findViewById(R.id.spinner_categoria_produto);
        editTextNome.setError(null);
        editTextPreco.setError(null);
        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validacao();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == getActivity().findViewById(R.id.spinner_categoria_produto)) {
            categoria = parent.getItemAtPosition(position).toString();

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void validacao() {
        nome = editTextNome.getText().toString();
        preco = editTextPreco.getText().toString();


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

        if (cancel) {
            focusView.requestFocus();

        } else {
            cadastraProduto();
        }

    }

    private void cadastraProduto() {
        String idProduto = ConfiguracoesFirebase.getFirebase().push().getKey();
        Toast.makeText(getActivity(), idSupervisor, Toast.LENGTH_SHORT).show();
        Produto produto = new Produto(idProduto, nome, preco);
        produto.salvarProduto(idSupervisor);
        FragmentUtils.replace(getActivity(), ProdutosFragment.newInstance());

    }


    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");


    }


}

