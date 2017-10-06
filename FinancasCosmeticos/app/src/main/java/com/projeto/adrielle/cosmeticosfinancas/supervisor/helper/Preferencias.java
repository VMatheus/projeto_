package com.projeto.adrielle.cosmeticosfinancas.supervisor.helper;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

/**
 * Created by matheus on 22/08/17.
 */

public class Preferencias {
    private FragmentActivity context;
    private SharedPreferences sharedPreferences;
    private String NOME_ARQUIVO = "financasCosmeticos.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;
    private final String ID_SUPERVISOR = "idSupervisor";
    private final String NOME_SUPERVISOR = "nomeSupervisor";
    private final String EMAIL_SUPERVISOR = "emailSupervisor";
    private final String SENHA_SUPERVISOR = "senhaSupervisor";

    public Preferencias(FragmentActivity context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = sharedPreferences.edit();
    }


    public void salvarSupervisorPreferencias(String idSupervisor, String nome_supervisor, String emailSupervisor, String senhaSupervisor) {
        editor.putString(ID_SUPERVISOR, idSupervisor);
        editor.putString(NOME_SUPERVISOR, nome_supervisor);
        editor.putString(EMAIL_SUPERVISOR, emailSupervisor);
        editor.putString(SENHA_SUPERVISOR, senhaSupervisor);


        editor.commit();

    }

    public String getIdSupervisor() {
        return sharedPreferences.getString(ID_SUPERVISOR, null);

    }

    public String getNomeSpervisor() {
        return sharedPreferences.getString(NOME_SUPERVISOR, null);

    }

    public String getEmailSpervisor() {
        return sharedPreferences.getString(EMAIL_SUPERVISOR, null);

    }

    public String getSenhaSupervisor() {
        return sharedPreferences.getString(SENHA_SUPERVISOR, null);

    }
}
