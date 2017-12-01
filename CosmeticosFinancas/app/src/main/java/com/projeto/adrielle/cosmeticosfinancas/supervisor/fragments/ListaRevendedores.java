package com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.projeto.adrielle.cosmeticosfinancas.DAO.ConfiguracoesFirebase;
import com.projeto.adrielle.cosmeticosfinancas.R;
import com.projeto.adrielle.cosmeticosfinancas.adapters.AdapterRevendedores;
import com.projeto.adrielle.cosmeticosfinancas.model.Revendedor;
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;
import com.projeto.adrielle.cosmeticosfinancas.utils.GetDataFromFirebase;
import com.projeto.adrielle.cosmeticosfinancas.utils.GridSpacingItemDecoration;
import com.projeto.adrielle.cosmeticosfinancas.utils.Utilitarios;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ListaRevendedores extends Fragment {


    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerViewRevendedores;
    private SharedPreferences sharedPrefSupervisor;
    private String idSupervisor;
    private String emailSupervisor;
    private String senhaSupervisor;
    private DatabaseReference databaseRevendedores;
    private ArrayList<Revendedor> revendedoresList;
    private TextView textViewInfo;



    private AdapterRevendedores adapterRevendedores;

    public static ListaRevendedores newInstance() {
        return new ListaRevendedores();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_lista_revendedores, container, false);
        recuperaDados();
        initViews(rootview);
        preencherLista();
        adapterRevendedores = new AdapterRevendedores(getActivity(), revendedoresList, databaseRevendedores);
        recyclerViewRevendedores.setAdapter(adapterRevendedores);
        if (container != null) {
            container.removeAllViews();
        }
        return rootview;

    }

    private void preencherLista() {

        revendedoresList = new ArrayList<>();

        databaseRevendedores = ConfiguracoesFirebase.getListaRevendedor(idSupervisor);
        databaseRevendedores.keepSynced(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerViewRevendedores.setLayoutManager(mLayoutManager);
        recyclerViewRevendedores.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerViewRevendedores.setItemAnimator(new DefaultItemAnimator());


        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        databaseRevendedores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if(!dataSnapshot.exists()){
                        textViewInfo.setVisibility(View.VISIBLE);

                    }else {
                        textViewInfo.setVisibility(View.GONE);
                    }


                    revendedoresList.clear();
                    for (DataSnapshot snapshotRevendedores : dataSnapshot.getChildren()) {
                        Revendedor revendedor = snapshotRevendedores.getValue(Revendedor.class);
                        revendedoresList.add(revendedor);

                    }
                    adapterRevendedores.atualiza(revendedoresList);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("", "Erro Lista de revendedores!");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initViews(View rootview) {


        floatingActionButton = rootview.findViewById(R.id.floating_button_adicionar_revendedores);
        recyclerViewRevendedores = rootview.findViewById(R.id.list_view_revendedores);
        textViewInfo = rootview.findViewById(R.id.text_info);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Utilitarios.verificaConexao(getActivity())) {


                    Toast.makeText(getActivity(), "Verifique sua conexão!", Toast.LENGTH_SHORT).show();
                } else {


                    FragmentUtils.replaceRetorno(getActivity(), CadastraRevendedor.newInstance());


                }


            }
        });

    }

    private void recuperaDados() {
        sharedPrefSupervisor = getActivity().getPreferences(MODE_PRIVATE);
        idSupervisor = sharedPrefSupervisor.getString("idSupervisor", "");
        emailSupervisor = sharedPrefSupervisor.getString("emailSupervisor", "");
        senhaSupervisor = sharedPrefSupervisor.getString("senhaSupervisor", "");

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
