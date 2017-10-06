package com.projeto.adrielle.cosmeticosfinancas.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.projeto.adrielle.cosmeticosfinancas.utils.ConstantsUtils;

import java.text.DecimalFormat;

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

    //metodos de leituras de dados
    //consulta lista revendedoras
    public static DatabaseReference getListaRevendedor(String idSupervisor) {

        return FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_REVENDEDORES);

    }
    //consulta lista de produtos em estoque
    public static Query getListaProdutosEstoque(String idSupervisor) {
        return FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_ESTOQUE).orderByChild("nome");

    }
    //consulta lista de produtos destinados a venda
    public static Query getListaProdutosVenda(String idSupervisor, String idRevendedor) {
        return FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_VENDAS + "/" + idRevendedor).orderByChild("nome");
    }

    //consulta lista de supervisores no login, para de terminar qual activity inflar...
    public static DatabaseReference getListaConsultaListaSupervisores(String id) {
        return FirebaseDatabase.getInstance().getReference(ConstantsUtils.BANCO_SUPERVISORES + "/" + id);

    }

    //consulta dados do perfil
    public static DatabaseReference getConsultaPerfilRevendor(String id) {

        return FirebaseDatabase.getInstance().getReference(id);

    }
    //consulta de vendas realizadas de cada revendedora
    public static DatabaseReference getVendasRealizadas(String idSupervisor, String idRevendedor) {
        return FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.VENDAS_REALIZADAS + "/" + idRevendedor);

    }
    /**
     public static DatabaseReference getVendasRealizadasUpdate(String idSupervisor, String idRevendedor, String idProduto) {
     return FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.VENDAS_REALIZADAS + "/" + idRevendedor + "/" + idProduto);

     }
     */

    //consulta dados do revendedor
    public static DatabaseReference getConsultaDadosRevendedor(String idSupervisor, String idRevendedor) {
        return FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_REVENDEDORES + "/" + idRevendedor + "/");


    }


}
