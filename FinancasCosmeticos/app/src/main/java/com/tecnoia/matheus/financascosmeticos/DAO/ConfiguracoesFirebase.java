package com.tecnoia.matheus.financascosmeticos.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;

/**
 * Created by matheus on 21/08/17.
 */

public class ConfiguracoesFirebase {


    private static DatabaseReference referenceFirebase;
    private static FirebaseAuth autenticacao;


    public static DatabaseReference getFirebase() {
        if (referenceFirebase == null) {
            referenceFirebase = FirebaseDatabase.getInstance().getReference();

        }
        return referenceFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao() {
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();

        }
        return autenticacao;
    }

    public static  DatabaseReference getListaRevendedor(String idSupervisor){

        referenceFirebase = FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_REVENDEDORES);

    return referenceFirebase;
    }

}
