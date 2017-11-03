package com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments;

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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projeto.adrielle.cosmeticosfinancas.EditarPerfilActivitySupervisor;
import com.projeto.adrielle.cosmeticosfinancas.model.Supervisor;
import com.projeto.adrielle.cosmeticosfinancas.utils.ConstantsUtils;
import com.projeto.adrielle.cosmeticosfinancas.utils.GetDataFromFirebase;
import com.projeto.adrielle.cosmeticosfinancas.utils.Utilitarios;
import com.tecnoia.matheus.financascosmeticos.R;

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
        recuperaDados();


        preencherPerfil();

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
                    textViewNome.setText(supervisor.getNome());
                    textViewEmail.setText(supervisor.getEmail());
                    textViewNumero.setText(supervisor.getNumero());



                    Glide.with(getActivity()).load(supervisor.getPhotoUrl()).into(imageView);

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
        imageView = rootview.findViewById(R.id.image);
        textViewNome = rootview.findViewById(R.id.text_nome);
        textViewEmail = rootview.findViewById(R.id.text_email);
        textViewNumero = rootview.findViewById(R.id.text_numero);

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
                Utilitarios.alertDialogDesconectar(getActivity());

                break;
            case R.id.action_editar:
                startActivity(new Intent(getActivity(), EditarPerfilActivitySupervisor.class));
                break;
        }
        return super.onOptionsItemSelected(item);


    }

    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");


    }


}
