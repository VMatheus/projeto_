package com.tecnoia.matheus.financascosmeticos.model;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matheus on 25/08/17.
 */

public class Produto {
    private String id;
    private String nome;
    private String preco;

    public Produto() {
    }

    public Produto(String id, String nome, String preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
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

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public void salvarProduto(String idSupervisor) {
        try {


            DatabaseReference reference = ConfiguracoesFirebase.getFirebase();
            reference.child(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_ESTOQUE).child(String.valueOf(getId())).setValue(this);
        }catch (Exception e){
            Log.e(e +"", "Erro!");
        }
    }
    @Exclude
    public Map<String, Object> map(){
        HashMap<String, Object> hashMapProduto = new HashMap<>();
        hashMapProduto.put("id", getId());
        hashMapProduto.put("nome", getNome());
        hashMapProduto.put("preco", getPreco());

        return  hashMapProduto;


    }


}
