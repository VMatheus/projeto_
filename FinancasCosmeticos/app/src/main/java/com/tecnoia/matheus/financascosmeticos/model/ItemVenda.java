package com.tecnoia.matheus.financascosmeticos.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;

/**
 * Created by matheus on 05/09/17.
 */

public class ItemVenda {
    private String id;
    private String nome;
    private String quantidade;
    private String saldoItens;


    public ItemVenda(String id, String nome, String quantidade, String saldoItens) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.saldoItens = saldoItens;
    }

    public ItemVenda() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }


    //salva vendas realizadas das revendedoras , tendo como parametro o id da supervisora e o id da revendedora
    public void novaVenda(String idSupervisor, String idRevendedor) {
        try {
            DatabaseReference reference = ConfiguracoesFirebase.getFirebase();

            reference.child(idSupervisor + "/" + ConstantsUtils.VENDAS_REALIZADAS + "/" + idRevendedor)
                    .child(String.valueOf(getId())).setValue(this);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e + "", "" + "Erro banco supervisora!");
        }


    }

    public String getSaldoItens() {
        return saldoItens;
    }

    public void setSaldoItens(String saldoItens) {
        this.saldoItens = saldoItens;
    }
}
