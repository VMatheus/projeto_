package com.tecnoia.matheus.financascosmeticos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.model.Revendedor;
import com.tecnoia.matheus.financascosmeticos.model.Supervisor;
import com.tecnoia.matheus.financascosmeticos.revendedor.MenuRevendedora;
import com.tecnoia.matheus.financascosmeticos.supervisor.MenuSupervisora;
import com.tecnoia.matheus.financascosmeticos.supervisor.fragments.TipoUsuarioFragment;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;
import com.tecnoia.matheus.financascosmeticos.utils.GetDataFromFirebase;


public class ContainerActivity extends AppCompatActivity {


    private DatabaseReference databaseSupervisores;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth autenticacao;
    private String id;
    private SharedPreferences sharedPrefRevendedor, sharedPrefSupervisor;
    private Fragment fragment = null;
    private DatabaseReference databaseRevendedor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_container);


        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {


                    id = firebaseAuth.getCurrentUser().getUid();
                    transitaTela();


                } else {
                    try {


                        FragmentUtils.replacePrincipal(ContainerActivity.this, TipoUsuarioFragment.newInstace());

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

                    } else {

                        carregarDadosRevendedor();
                        fragment = MenuRevendedora.newInstance();

                        FragmentUtils.replacePrincipal(ContainerActivity.this, fragment);


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
