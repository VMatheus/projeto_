package com.tecnoia.matheus.financascosmeticos.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;

/**
 * Created by matheus on 18/08/17.
 */

public class ValidaCamposConexao {

    //classe para validação de campos


    public boolean validaSenha(String s) {
        return s.length() > 4;

    }

    public boolean validaEmail(String e) {
        return e.contains("@");
    }

    public boolean validaNome(String n) {
        return n.length() >= 1;

    }


    public boolean verificaConexao(FragmentActivity activity) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        conectado = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return conectado;
    }

    public void progressDialog(Context activity, ProgressDialog progressDialog, String titulo, String mensagem) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(titulo);
        progressDialog.setMessage(mensagem);
        progressDialog.show();


    }


}
