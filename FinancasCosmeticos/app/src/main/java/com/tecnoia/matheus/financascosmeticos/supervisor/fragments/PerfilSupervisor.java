package com.tecnoia.matheus.financascosmeticos.supervisor.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tecnoia.matheus.financascosmeticos.PerfilActivity;
import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.model.Supervisor;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;
import com.tecnoia.matheus.financascosmeticos.utils.GetDataFromFirebase;
import com.tecnoia.matheus.financascosmeticos.utils.ValidaCamposConexao;

import static android.content.Context.MODE_PRIVATE;

public class PerfilSupervisor extends Fragment {
    private TextView textViewNome, textViewNumero, textViewEmail;
    private ImageView imageView;


    private Toolbar toolbar;
    private DatabaseReference referencePerfil;
    private SharedPreferences sharedPrefSupervisor;
    private String idSupervisor;
    String nome;


    public static PerfilSupervisor newInstance() {

        return new PerfilSupervisor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_perfil_supervisor, container, false);
        setHasOptionsMenu(true);


        initViews(rootview);
       /* recuperaDados();
        preencherPerfil();*/
        return rootview;

    }

    private void preencherPerfil() {

        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        referencePerfil = FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.PERFIL);
        referencePerfil.keepSynced(true);
        referencePerfil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Supervisor supervisor = dataSnapshot.getValue(Supervisor.class);

                   
                    textViewEmail.setText(supervisor.getEmail());
                    Glide.with(getActivity()).load(supervisor.getPhotoUrl()).into(imageView);

                    toolbar.setTitle(supervisor.getNome());

                } catch (Exception e) {
                    e.printStackTrace();
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initViews(View rootview) {

        toolbar = rootview.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        /*((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        /*imageView = rootview.findViewById(R.id.image);
        textViewEmail = rootview.findViewById(R.id.text_email);*/

      /*  textViewNumero = rootview.*/


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_perfil, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_desconectar:
                //Dialog confirmar
                ValidaCamposConexao.alertDialogDesconectar(getActivity());

                break;
            case R.id.action_editar:
                startActivity(new Intent(getActivity(), PerfilActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);


    }

    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");


    }


}
