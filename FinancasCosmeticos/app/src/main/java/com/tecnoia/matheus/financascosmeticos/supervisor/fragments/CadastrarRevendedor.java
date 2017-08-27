package com.tecnoia.matheus.financascosmeticos.supervisor.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.Revendedor;
import com.tecnoia.matheus.financascosmeticos.model.Supervisor;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

import java.util.List;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class CadastrarRevendedor extends Fragment {
    private EditText editTextNome, editTextEmail, editTextSenha;
    private Button buttonSalvar;
    private View focusView = null;
    private boolean cancel = false;
    private ProgressDialog progressDialog;
    private String nome, email, senha;

    private List<Supervisor> supervisorList;
    private DatabaseReference databasePerfilSupervisor;
    private String emailSupervisor, senhaSupervisor, idSupervisor;
    private FirebaseAuth.AuthStateListener authStateListener;

    private ConfiguracoesFirebase configuracoesFirebase = new ConfiguracoesFirebase();
    private FirebaseAuth mAuth1, mAuth2;
    private SharedPreferences sharedPrefSupervisor;
    private static String NOME_APP = "FinancasCosmeticos";
    private FirebaseApp myApp;


    public static CadastrarRevendedor newInstance() {
        CadastrarRevendedor cadastrarRevendedor = new CadastrarRevendedor();


        return cadastrarRevendedor;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_cadastrar_revendedor, container, false);
        mAuth1 = FirebaseAuth.getInstance();
        recuperaDados();

        try {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://fir-fcm-40651.firebaseio.com/")
                    .setApiKey("AIzaSyBvaCYgm4KanJ7JhvBlvYjsBGVQB3UVGOI")
                    .setApplicationId("1:855606521832:android:be1214118720ccaa").build();

             myApp = FirebaseApp.initializeApp(getActivity(),firebaseOptions, UUID.randomUUID()+"");

            mAuth2 = FirebaseAuth.getInstance(myApp);


        } catch (Exception e) {
            e.printStackTrace();
        /*  Toast.makeText(getActivity(),"Erro! " + e, Toast.LENGTH_SHORT).show();*/
            Log.e(e+"", "erro!");

        }


        initViews(rootview);


        return rootview;

    }


    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");
        emailSupervisor = sharedPrefSupervisor.getString("emailSupervisor", "");
        senhaSupervisor = sharedPrefSupervisor.getString("senhaSupervisor", "");

    }


    private void initViews(View rootview) {


        buttonSalvar = rootview.findViewById(R.id.btn_register_rev);
        editTextNome = rootview.findViewById(R.id.edit_nome_rev);
        editTextEmail = rootview.findViewById(R.id.edit_email_rev);
        editTextSenha = rootview.findViewById(R.id.edit_senha_rev);
        progressDialog = new ProgressDialog(getActivity());
        editTextNome.setError(null);
        editTextEmail.setError(null);
        editTextSenha.setError(null);

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacaoCampos();


            }
        });


    }

    private void validacaoCampos() {


        nome = editTextNome.getText().toString();
        email = editTextEmail.getText().toString();
        senha = editTextSenha.getText().toString();
        showProgressDialog();
        criaRevendedor();


    }


    private void criaRevendedor() {


        mAuth2.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String idRevendedor = task.getResult().getUser().getUid();
                    Revendedor revendedor = new Revendedor(idRevendedor, idSupervisor, nome, email, senha, "...");
                    revendedor.salvarRevendedor(idSupervisor);


                    mAuth2.signOut();
                    FragmentUtils.replace(getActivity(), ListaRevendedores.newInstance());

                } else {

                }
                progressDialog.dismiss();
            }
        });


    }


    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setMessage(getString(R.string.aguarde));
        progressDialog.show();

    }
}



