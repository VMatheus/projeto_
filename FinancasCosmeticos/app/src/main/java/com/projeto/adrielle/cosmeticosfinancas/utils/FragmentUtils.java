package com.projeto.adrielle.cosmeticosfinancas.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tecnoia.matheus.financascosmeticos.R;

/**
 * Created by matheus on 18/08/17.
 */

public class FragmentUtils {

    public static void replace(FragmentActivity activity, Fragment fragment) {
        replace(activity, fragment, R.id.container_principal_supervisor);
    }

    public static void replace(FragmentActivity activity, Fragment fragment, int id) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void replaceRetorno(FragmentActivity activity, Fragment fragment) {
        replaceRetorno(activity, fragment, R.id.container_principal_supervisor);
    }

    public static void replaceRetorno(FragmentActivity activity, Fragment fragment, int id) {
        String s = activity.getClass().getName();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment).addToBackStack(s);
        transaction.commit();
    }


    public static void replacePrincipal(AppCompatActivity activity, Fragment fragment) {
        replacePrincipal(activity, fragment, R.id.container_main);
    }

    public static void replacePrincipal(AppCompatActivity activity, Fragment fragment, int id) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.commitAllowingStateLoss();
    }

    //ReplacePrincipalRetorno
    public static void replacePrincipalRetorno(FragmentActivity activity, Fragment fragment) {
        replacePrincipalRetorno(activity, fragment, R.id.container_main);
    }

    public static void replacePrincipalRetorno(FragmentActivity activity, Fragment fragment, int id) {
        String s = activity.getClass().getName();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment).addToBackStack(s);
        transaction.commit();
    }
 //ReplacePrincipalRetorno
    public static void replacePrincipalRetornoFra(FragmentActivity activity, Fragment fragment) {
        replacePrincipalRetornoFra(activity, fragment, R.id.container_main);
    }

    public static void replacePrincipalRetornoFra(FragmentActivity activity, Fragment fragment, int id) {
        String s = activity.getClass().getName();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.commit();
    }

    public static void replaceOpcaoUsuario(FragmentActivity activity, Fragment fragment) {
        replaceOpcaoUsuario(activity, fragment, R.id.container_main);
    }

    public static void replaceOpcaoUsuario(FragmentActivity activity, Fragment fragment, int id) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment).addToBackStack(null);

        transaction.commit();

    }

    public static void replaceSemRetorno(FragmentActivity activity, Fragment fragment) {
        replaceSemRetorno(activity, fragment, R.id.container_main);
    }

    public static void replaceSemRetorno(FragmentActivity activity, Fragment fragment, int id) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);

        transaction.commit();

    }


    //Fragments Utils Revendedores


    public static void replaceRevendedor(FragmentActivity activity, Fragment fragment) {
        replaceRevendedor(activity, fragment, R.id.container_principal_revendedor);
    }

    public static void replaceRevendedor(FragmentActivity activity, Fragment fragment, int id) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.commit();
    }


    public static void replaceRevendedorRetorno(FragmentActivity activity, Fragment fragment) {
        replaceRevendedorRetorno(activity, fragment, R.id.container_principal_revendedor);
    }

    public static void replaceRevendedorRetorno(FragmentActivity activity, Fragment fragment, int id) {
        String s = activity.getClass().getName();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment).addToBackStack(s);
        transaction.commit();
    }


}
