package com.tecnoia.matheus.financascosmeticos.revendedor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tecnoia.matheus.financascosmeticos.ContainerActivity;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.utils.ValidaCamposConexao;

public class LoginRevendedor extends Fragment {
    private EditText editTextEmail, editTextSenha;
    private Button buttonLogin;
    private String email, senha;
    private View focusView = null;
    private boolean cancel = false;
    private ValidaCamposConexao  camposConexao = new ValidaCamposConexao();
    private ProgressDialog progressDialog;
    private FirebaseAuth autenticacao;


    public static LoginRevendedor newInstance() {
        return new LoginRevendedor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_login_revendedor, container, false);


        initViews(rootView);


        return rootView;


    }

    private void initViews(View rootView) {
        editTextEmail = rootView.findViewById(R.id.edit_email_login_rev);
        editTextSenha = rootView.findViewById(R.id.edit_senha_login_rev);
        buttonLogin = rootView.findViewById(R.id.btn_login_rev);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacao();
            }
        });


        editTextEmail.setError(null);
        editTextSenha.setError(null);

    }

    private void validacao() {

        email = editTextEmail.getText().toString();
        senha = editTextSenha.getText().toString();


        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.campo_vazio));
            focusView = editTextEmail;
            cancel = true;

        } else if (!camposConexao.validaEmail(email)) {
            editTextEmail.setError(getString(R.string.email_invalido));
            focusView = editTextEmail;
            cancel = true;

        }
        if (TextUtils.isEmpty(senha)) {
            editTextSenha.setError(getString(R.string.campo_vazio));
            focusView = editTextSenha;
            cancel = true;

        } else if (!camposConexao.validaSenha(senha)) {
            editTextEmail.setError(getString(R.string.senha_invalida));
            focusView = editTextSenha;
            cancel = true;


        }

        if (cancel) {
            focusView.requestFocus();

        } else {

            showProgressDialog();
            validaLogin();

        }


    }

    private void validaLogin() {
        autenticacao = ConfiguracoesFirebase.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {


                    getActivity().finish();
                    startActivity(new Intent(getActivity(), ContainerActivity.class));


                } else {

                    Snackbar.make(getView(), getString(R.string.usuario_senha_invalidos), Snackbar.LENGTH_LONG).show();
                }
                progressDialog.dismiss();


            }
        });

    }


    private void showProgressDialog() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getString(R.string.autenticando));
            progressDialog.setMessage(getString(R.string.aguarde));
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }




}
