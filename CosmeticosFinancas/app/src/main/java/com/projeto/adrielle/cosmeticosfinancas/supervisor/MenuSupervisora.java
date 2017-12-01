package com.projeto.adrielle.cosmeticosfinancas.supervisor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments.ListaProdutosEstoque;
import com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments.ListaRevendedores;
import com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments.PerfilSupervisor;
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;
import com.projeto.adrielle.cosmeticosfinancas.R;

public class MenuSupervisora extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPrefSupervisor;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;


    public static MenuSupervisora newInstance() {
        return new MenuSupervisora();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_supervisor, container, false);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Status", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Status", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mAuth = FirebaseAuth.getInstance();

        initViews(rootView);
        if (container != null) {
            container.removeAllViews();
        }

        return rootView;


    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initViews(View rootView) {
        bottomNavigationView = rootView.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.item_revendedoras:
/*
                        fragment = RevendedoresContainer.newInstance();
*/
                        fragment = ListaRevendedores.newInstance();
                        break;
                    /*case R.id.item_estatisticas:

                        fragment = EstatisticasFragment.newInstance();
                        break;*/

                    case R.id.item_estoque:
                        fragment = ListaProdutosEstoque.newInstance();
                        break;

                    case R.id.item_perfil:
                        fragment = PerfilSupervisor.newInstance();

                        break;


                }
                FragmentUtils.replace(getActivity(), fragment);
                return true;
            }
        });

        try {
/**/
            FragmentUtils.replace(getActivity(), ListaRevendedores.newInstance());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
