package com.tecnoia.matheus.financascosmeticos.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by matheus on 25/08/17.
 */

public class Produto {
    private String id;
    private String nome;
    private String preco;
    private String quantidade;
    private String status;


    public Produto() {
    }

    public Produto(String id, String nome, String preco, String quantidade, String status) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.status = status;
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



    public void removerProdutoEstoque(String idSupervisor, String idProduto) {
        try {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_ESTOQUE + "/" + idProduto);
            reference.removeValue();

        } catch (Exception e) {
            Log.e(e + "", "Erro remover produto estoque!");
        }

    }

    public void salvarProduto(String idSupervisor) {
        try {


            DatabaseReference reference = ConfiguracoesFirebase.getFirebase();
            reference.child(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_ESTOQUE).child(String.valueOf(getId())).setValue(this);
        } catch (Exception e) {
            Log.e(e + "", "Erro!");
        }
    }

    public void atualizarProduto(String idSupervisor, String idProduto) {
        DatabaseReference reference = ConfiguracoesFirebase.getFirebase();
        reference.child(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_ESTOQUE).child(String.valueOf(idProduto)).setValue(this);
    }


    @Exclude
    public Map<String, Object> map() {
        HashMap<String, Object> hashMapProduto = new HashMap<>();
        hashMapProduto.put("id", getId());
        hashMapProduto.put("nome", getNome());
        hashMapProduto.put("preco", getPreco());
        hashMapProduto.put("quantidade", getQuantidade());


        return hashMapProduto;


    }

    public void salvaProdutoVendas(String idSupervisor, String idRevendedor) {
        try {
            DatabaseReference reference = ConfiguracoesFirebase.getFirebase();
            reference.child(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_VENDAS + "/" + idRevendedor).child(String.valueOf(getId())).setValue(this);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e + "", "" + "Erro banco supervisora!");
        }


    }


    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public void removeProdutoVenda(String idSupervisor, String idProduto, String idRevendedor) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_VENDAS + "/" + idRevendedor+"/"+idProduto);
        reference.removeValue();

    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
