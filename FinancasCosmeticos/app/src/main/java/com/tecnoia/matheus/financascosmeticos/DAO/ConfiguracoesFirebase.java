package com.tecnoia.matheus.financascosmeticos.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    public static DatabaseReference getListaRevendedor(String idSupervisor) {

        return FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_REVENDEDORES);

    }

    public static Query getListaProdutosEstoque(String idSupervisor) {
        return FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_ESTOQUE).orderByChild("nome");

    }

    public static Query getListaProdutosVenda(String idSupervisor, String idRevendedor) {
        return FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_VENDAS + "/" + idRevendedor).orderByChild("nome");
    }

    public static DatabaseReference getListaConsultaListaSupervisores(String id) {
        return FirebaseDatabase.getInstance().getReference(ConstantsUtils.BANCO_SUPERVISORES + "/" + id);

    }


    public static DatabaseReference getConsultaPerfilRevendor(String id) {

        return FirebaseDatabase.getInstance().getReference(id);

    }

    public static DatabaseReference getVendasRealizadas(String idSupervisor, String idRevendedor) {
        return FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.VENDAS_REALIZADAS + "/" + idRevendedor);

    }

    public static DatabaseReference getVendasRealizadasUpdate(String idSupervisor, String idRevendedor, String idProduto) {
        return FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.VENDAS_REALIZADAS + "/" + idRevendedor + "/" + idProduto);

    }
}
