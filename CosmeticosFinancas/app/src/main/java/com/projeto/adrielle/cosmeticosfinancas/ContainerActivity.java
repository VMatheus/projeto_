package com.projeto.adrielle.cosmeticosfinancas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projeto.adrielle.cosmeticosfinancas.DAO.ConfiguracoesFirebase;
import com.projeto.adrielle.cosmeticosfinancas.R;
import com.projeto.adrielle.cosmeticosfinancas.model.Revendedor;
import com.projeto.adrielle.cosmeticosfinancas.model.Supervisor;
import com.projeto.adrielle.cosmeticosfinancas.revendedor.MenuRevendedora;
import com.projeto.adrielle.cosmeticosfinancas.supervisor.MenuSupervisora;
import com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments.TipoUsuarioFragment;
import com.projeto.adrielle.cosmeticosfinancas.utils.ConstantsUtils;
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;
import com.projeto.adrielle.cosmeticosfinancas.utils.GetDataFromFirebase;


public class ContainerActivity extends AppCompatActivity {


    private DatabaseReference databaseSupervisores;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth autenticacao;
    private String id;
    private SharedPreferences sharedPrefRevendedor, sharedPrefSupervisor;
    private Fragment fragment = null;
    private DatabaseReference databaseRevendedor;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_container);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {


                    id = firebaseAuth.getCurrentUser().getUid();
                    transitaTela();


                } else {
                    try {


                        FragmentUtils.replacePrincipal(ContainerActivity.this, TipoUsuarioFragment.newInstace());
                        progressBar.setVisibility(View.GONE);

                    } catch (IllegalStateException e) {
                        e.printStackTrace();

                    }


                }
                // ...
            }
        });

    }

    public void transitaTela() {


        databaseSupervisores = FirebaseDatabase.getInstance().getReference(ConstantsUtils.BANCO_SUPERVISORES + "/" + id);
        databaseSupervisores.keepSynced(true);
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        databaseSupervisores.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {

                        carregarDadosSupervisor();
                        fragment = MenuSupervisora.newInstance();
                        FragmentUtils.replacePrincipal(ContainerActivity.this, fragment);
                        progressBar.setVisibility(View.GONE);

                    } else {

                        carregarDadosRevendedor();
                        fragment = MenuRevendedora.newInstance();

                        FragmentUtils.replacePrincipal(ContainerActivity.this, fragment);
                        progressBar.setVisibility(View.GONE);


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

    private void carregarDadosRevendedor() {
        databaseRevendedor = ConfiguracoesFirebase.getConsultaPerfilRevendor(id);
        databaseRevendedor.keepSynced(true);
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        databaseRevendedor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Revendedor revendedor = dataSnapshot.getValue(Revendedor.class);

                    sharedPrefRevendedor = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefRevendedor.edit();
                    editor.putString("idRevendedor", revendedor.getId());
                    editor.putString("idSupervisor", revendedor.getIdSupervisor());
                    editor.apply();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void carregarDadosSupervisor() {

        databaseSupervisores = ConfiguracoesFirebase.getListaConsultaListaSupervisores(id);
        databaseSupervisores.keepSynced(true);
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        databaseSupervisores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Supervisor supervisor = dataSnapshot.getValue(Supervisor.class);

                    sharedPrefSupervisor = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefSupervisor.edit();
                    editor.putString("idSupervisor", supervisor.getId());
                    editor.putString("emailSupervisor", supervisor.getEmail());
                    editor.putString("senhaSupervisor", supervisor.getSenha());
                    editor.apply();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }


}
