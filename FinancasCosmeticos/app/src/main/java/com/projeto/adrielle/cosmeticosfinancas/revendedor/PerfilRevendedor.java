package com.projeto.adrielle.cosmeticosfinancas.revendedor;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projeto.adrielle.cosmeticosfinancas.EditarPerfilActivityRevendedor;
import com.projeto.adrielle.cosmeticosfinancas.model.Revendedor;
import com.projeto.adrielle.cosmeticosfinancas.model.Supervisor;
import com.projeto.adrielle.cosmeticosfinancas.utils.ConstantsUtils;
import com.projeto.adrielle.cosmeticosfinancas.utils.GetDataFromFirebase;
import com.tecnoia.matheus.financascosmeticos.R;
import com.projeto.adrielle.cosmeticosfinancas.utils.ValidaCamposConexao;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilRevendedor extends Fragment {
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationViewss;
    private FirebaseUser firebaseUser;
    private String idRevendedor;
    private DatabaseReference referencePerfil ;
    private TextView textViewNome, textViewEmail, textViewNumero;
    private ImageView imageView ;

    public static PerfilRevendedor newInstance() {

        return new PerfilRevendedor();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_perfil_revendedor, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        idRevendedor = firebaseUser.getUid();
        setHasOptionsMenu(true);
        initViews(rootView);
        preencherPerfil();

        return rootView;
    }
    private void preencherPerfil() {

        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        referencePerfil = FirebaseDatabase.getInstance().getReference(idRevendedor);
        referencePerfil.keepSynced(true);
        referencePerfil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Revendedor revendedor = dataSnapshot.getValue(Revendedor.class);
                    textViewNome.setText(revendedor.getNome());
                    textViewEmail.setText(revendedor.getEmail());
                   textViewNumero.setText(revendedor.getNumero());



                    Glide.with(getActivity()).load(revendedor.getPhotoUrl()).into(imageView);

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
        textViewNome = rootview.findViewById(R.id.text_nome);
        textViewEmail = rootview.findViewById(R.id.text_email);
        textViewNumero = rootview.findViewById(R.id.text_numero);



        toolbar = rootview.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                startActivity(new Intent(getActivity(), EditarPerfilActivityRevendedor.class));
              /*  FragmentUtils.replaceRevendedorRetorno(getActivity(), EditarPerfilRevendedorActivity.newInstace());*/

                break;
        }
        return super.onOptionsItemSelected(item);


    }


}
