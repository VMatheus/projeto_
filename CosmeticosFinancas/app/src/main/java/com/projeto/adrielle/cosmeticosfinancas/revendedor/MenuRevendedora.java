package com.projeto.adrielle.cosmeticosfinancas.revendedor;

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
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;
import com.projeto.adrielle.cosmeticosfinancas.R;

public class MenuRevendedora extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;


    public static MenuRevendedora newInstance() {
        return new MenuRevendedora();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_revendedor, container, false);
        if (container != null) {
            container.removeAllViews();
        }

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
                    case R.id.item__vendas:
                        fragment = VendasRealizadas.newInstance();

                        break;

                    case R.id.item_produtos:
                        fragment = ProdutosRevendedor.newInstance();
                        break;

                    case R.id.item_perfil_revendedor:
                        fragment = PerfilRevendedor.newInstance();
                        break;

                }


                FragmentUtils.replaceRevendedor(getActivity(), fragment);

                return true;
            }
        });
        try {

            FragmentUtils.replaceRevendedor(getActivity(), VendasRealizadas.newInstance());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
