package com.tecnoia.matheus.financascosmeticos.supervisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tecnoia.matheus.financascosmeticos.R;
import com.tecnoia.matheus.financascosmeticos.revendedor.LoginRevendedor;
import com.tecnoia.matheus.financascosmeticos.supervisor.LoginRegisterFragment;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

public class TipoUsuarioFragment extends Fragment {
    private Button buttonSupervisor, buttonRevendedor;

    public static TipoUsuarioFragment newInstace() {

        return new TipoUsuarioFragment();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tipo_usuario_fragment, container, false);
        initViews(rootView);
        return rootView;


    }

    private void initViews(View rootView) {
        buttonSupervisor = rootView.findViewById(R.id.buttonSupervisor);
        buttonRevendedor = rootView.findViewById(R.id.buttonRevendedor);
        buttonSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentUtils.replaceOpcaoUsuario(getActivity(), LoginRegisterFragment.newInstace());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        buttonRevendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FragmentUtils.replaceOpcaoUsuario(getActivity(), LoginRevendedor.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
