package com.tecnoia.matheus.financascosmeticos;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.model.Supervisor;
import com.tecnoia.matheus.financascosmeticos.revendedor.MainRevendedor;
import com.tecnoia.matheus.financascosmeticos.supervisor.MainSupervisor;
import com.tecnoia.matheus.financascosmeticos.supervisor.fragments.TipoUsuarioFragment;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;
import com.tecnoia.matheus.financascosmeticos.utils.GetDataFromFirebase;


public class ContainerActivity extends AppCompatActivity {
    private FragmentUtils fragmentUtils = new FragmentUtils();


    private DatabaseReference databaseSupervisores;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth autenticacao;
    private String id;
    private SharedPreferences sharedPrefSupervisor;
    private Fragment fragment;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_container);
        autenticacao = ConfiguracoesFirebase.getFirebaseAutenticacao();


        verficaUsuarioPresente();


    }

    @Override
    public void onStart() {
        super.onStart();
        autenticacao.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            autenticacao.removeAuthStateListener(authStateListener);
        }
    }


    private void verficaUsuarioPresente() {
        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                try {


                    if (firebaseAuth.getCurrentUser() == null) {
                        fragment = TipoUsuarioFragment.newInstace();
                        FragmentUtils.replacePrincipal(ContainerActivity.this, fragment);


                    } else {


                        id = autenticacao.getCurrentUser().getUid();

                        transitaTela();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        };

    }



    private void transitaTela() {


        databaseSupervisores = FirebaseDatabase.getInstance().getReference(ConstantsUtils.BANCO_SUPERVISORES + "/" + id);
        databaseSupervisores.keepSynced(true);
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        databaseSupervisores.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        carregarDadosSupervisor();

                        FragmentUtils.replacePrincipal(ContainerActivity.this,MainSupervisor.newInstance());

                    } else {

                        FragmentUtils.replacePrincipal(ContainerActivity.this,MainRevendedor.newInstance());


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

