package com.tecnoia.matheus.financascosmeticos.revendedor;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tecnoia.matheus.financascosmeticos.R;

public class MainRevendedor extends Fragment {

    public static MainRevendedor newInstance() {
        MainRevendedor fragment = new MainRevendedor();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_revendedor, container, false);



        return rootView;


    }
}
