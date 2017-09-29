package com.tecnoia.matheus.financascosmeticos.supervisor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tecnoia.matheus.financascosmeticos.ContainerActivity;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.Supervisor;
import com.tecnoia.matheus.financascosmeticos.supervisor.helper.Preferencias;
import com.tecnoia.matheus.financascosmeticos.utils.ValidaCamposConexao;

public class CadastroSupervisor extends Fragment {
    private EditText editTextNome, editTextEmail, editTextSenha;
    private Button buttonRegistrar;
    private ProgressDialog progressDialog;
    private FirebaseAuth autenticacao;
    private String email, senha, nome, photoUrl;
    private boolean cancel = false;
    private View focusView = null;
    private ValidaCamposConexao camposConexao = new ValidaCamposConexao();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_cadastro_supervisor, container, false);
        initViews(rootView);
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registraSupervisor();

            }
        });
        return rootView;


    }

    private void initViews(View rootView) {


        editTextNome = rootView.findViewById(R.id.edit_nome_sup_register_sup);
        editTextEmail = rootView.findViewById(R.id.edit_email_register_sup);
        editTextSenha = rootView.findViewById(R.id.edit_senha_register_sup);
        buttonRegistrar = rootView.findViewById(R.id.btn_register_sup);


        editTextNome.setError(null);
        editTextEmail.setError(null);
        editTextSenha.setError(null);


    }

    private boolean validaSenha(String s) {
        return s.length() > 4;

    }

    private boolean validaEmail(String e) {
        return e.contains("@");
    }

    private boolean validaNome(String n) {
        return n.length() >= 1;

    }


    private void registraSupervisor() {
        nome = editTextNome.getText().toString();
        email = editTextEmail.getText().toString();
        senha = editTextSenha.getText().toString();


        //checando campos
        if (TextUtils.isEmpty(nome)) {
            editTextNome.setError(getString(R.string.campo_vazio));
            focusView = editTextNome;
            cancel = true;

        } else if (!validaNome(nome)) {
            editTextEmail.setError(getString(R.string.nome_invalido));
            focusView = editTextNome;
            cancel = true;


        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.campo_vazio));
            focusView = editTextEmail;
            cancel = true;

        } else if (!validaEmail(email)) {
            editTextEmail.setError(getString(R.string.email_invalido));
            focusView = editTextEmail;
            cancel = true;


        }
        if (TextUtils.isEmpty(senha)) {
            editTextSenha.setError(getString(R.string.campo_vazio));
            focusView = editTextSenha;
            cancel = true;

        } else if (!validaSenha(senha)) {
            editTextEmail.setError(getString(R.string.senha_invalida));
            focusView = editTextSenha;
            cancel = true;


        }


        if (cancel) {
            focusView.requestFocus();

        } else {
            showProgressDialog();

            validaCadastro();
        }
    }

    private void validaCadastro() {
        autenticacao = ConfiguracoesFirebase.getFirebaseAutenticacao();


        autenticacao.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String id = autenticacao.getCurrentUser().getUid();

                            //salvaprimeira imagem default perfil
                            salvaImagemDefault(id);

                            Supervisor supervisor = new Supervisor(id, nome, email, senha, photoUrl);

                            supervisor.salvarSupervisor();
                            startActivity(new Intent(getActivity(), ContainerActivity.class));

                            getActivity().finish();


                        } else {
                            Toast.makeText(getActivity(), "Erro!", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();


                        // ...
                    }
                });

    }

    public void salvaImagemDefault(String id) {
        photoUrl = "www.teste";





    }


    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage(getString(R.string.aguarde));
        progressDialog.show();

    }
}
