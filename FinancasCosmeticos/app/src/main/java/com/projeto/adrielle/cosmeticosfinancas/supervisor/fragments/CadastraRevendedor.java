package com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projeto.adrielle.cosmeticosfinancas.model.Revendedor;
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;
import com.projeto.adrielle.cosmeticosfinancas.utils.Utilitarios;
import com.tecnoia.matheus.financascosmeticos.R;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class CadastraRevendedor extends Fragment {
    private EditText editTextNome, editTextEmail, editTextSenha;
    private Button buttonSalvar;
    private View focusView = null;
    private boolean cancel = false;
    private ProgressDialog progressDialog;
    private String nome, email, senha;


    private String emailSupervisor, senhaSupervisor, idSupervisor;
    private FirebaseAuth.AuthStateListener authStateListener;


    private FirebaseAuth mAuth1, mAuth2;
    private SharedPreferences sharedPrefSupervisor;
    private static String NOME_APP = "FinancasCosmeticos";
    private FirebaseApp myApp;
    private String pathImage;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private String photoUrl;
    private static String TAG = "Adicionado ae banco";


    public static CadastraRevendedor newInstance() {

        return new CadastraRevendedor();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_cadastrar_revendedor, container, false);
        mAuth1 = FirebaseAuth.getInstance();


        try {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://fir-fcm-40651.firebaseio.com/")
                    .setApiKey("AIzaSyBvaCYgm4KanJ7JhvBlvYjsBGVQB3UVGOI")
                    .setApplicationId("1:855606521832:android:be1214118720ccaa").build();

            myApp = FirebaseApp.initializeApp(getActivity(), firebaseOptions, UUID.randomUUID() + "");

            mAuth2 = FirebaseAuth.getInstance(myApp);


        } catch (Exception e) {
            e.printStackTrace();
        /*  Toast.makeText(getActivity(),"Erro! " + e, Toast.LENGTH_SHORT).show();*/
            Log.e(e + "", "erro!");

        }


        initViews(rootview);

        if (container != null) {
            container.removeAllViews();
        }
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

        if (TextUtils.isEmpty(nome)) {
            editTextNome.setError(getString(R.string.campo_vazio));
            focusView = editTextNome;
            cancel = true;
        }


        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.campo_vazio));
            focusView = editTextEmail;
            cancel = true;

        } else if (!Utilitarios.validaEmail(email)) {
            editTextEmail.setError(getString(R.string.email_invalido));
            focusView = editTextEmail;
            cancel = true;

        }
        if (TextUtils.isEmpty(senha)) {
            editTextSenha.setError(getString(R.string.campo_vazio));
            focusView = editTextSenha;
            cancel = true;

        } else if (!Utilitarios.validaSenha(senha)) {
            editTextEmail.setError(getString(R.string.senha_invalida));
            focusView = editTextSenha;
            cancel = true;


        }

        if (cancel) {
            focusView.requestFocus();

        } else {

            showProgressDialog();
            idSupervisor = mAuth1.getCurrentUser().getUid();
            criaRevendedor(idSupervisor);


        }


    }


    private void criaRevendedor(final String idSupervisor) {


        mAuth2.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //uploadImagem
                    Bitmap bitmap = Utilitarios.drawableToBitmap(getActivity().getResources().getDrawable(R.drawable.ic_person));

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] data = outputStream.toByteArray();

                    //nomeando image
                    pathImage = UUID.randomUUID().toString();

                    StorageReference storageImagem = storage.getReference(pathImage);

                    StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata(TAG, nome + "perfil").build();

                    UploadTask uploadTask = storageImagem.putBytes(data, metadata);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri uri = taskSnapshot.getDownloadUrl();
                            String idRevendedor = task.getResult().getUser().getUid();

                            photoUrl = uri.toString();
                            Revendedor revendedor = new Revendedor(idRevendedor, idSupervisor, nome, email, senha, photoUrl, pathImage, "(**)*********", "0.00");
                            revendedor.salvarRevendedor(idSupervisor);

                            progressDialog.dismiss();
                            mAuth2.signOut();
                            FragmentUtils.replace(getActivity(), ListaRevendedores.newInstance());


                        }
                    });

                } else {

                }

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



