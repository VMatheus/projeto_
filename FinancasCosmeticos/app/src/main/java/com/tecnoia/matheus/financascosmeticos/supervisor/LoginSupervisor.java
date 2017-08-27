package com.tecnoia.matheus.financascosmeticos.supervisor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.tecnoia.matheus.financascosmeticos.model.Supervisor;
import com.tecnoia.matheus.financascosmeticos.utils.ValidaCamposConexao;

public class LoginSupervisor extends Fragment {
    private EditText editTextEmail, editTextSenha;
    private Button buttonLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth autenticacao;

    private boolean cancel = false;
    private View focusView = null;
    private Supervisor supervisor;

    private ValidaCamposConexao validaCamposConexao = new ValidaCamposConexao();
    private String email, senha;
    private SharedPreferences sharedPref;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_login_supervisor, container, false);


        initViews(rootView);


        return rootView;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initViews(View rootView) {

        editTextEmail = rootView.findViewById(R.id.edit_email_login_sup);
        editTextSenha = rootView.findViewById(R.id.edit_senha_login_sup);
        buttonLogin = rootView.findViewById(R.id.btn_login_sup);
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

        } else if (!validaCamposConexao.validaEmail(email)) {
            editTextEmail.setError(getString(R.string.email_invalido));
            focusView = editTextEmail;
            cancel = true;

        }
        if (TextUtils.isEmpty(senha)) {
            editTextSenha.setError(getString(R.string.campo_vazio));
            focusView = editTextSenha;
            cancel = true;

        } else if (!validaCamposConexao.validaSenha(senha)) {
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


    public static LoginSupervisor newInstance() {
        LoginSupervisor fragment = new LoginSupervisor();
        return fragment;
    }


    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.autenticando));
        progressDialog.setMessage(getString(R.string.aguarde));
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }
}
