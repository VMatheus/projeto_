package com.tecnoia.matheus.financascosmeticos.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by matheus on 19/08/17.
 */

public class Revendedor {
    private String id;
    private String idSupervisor;
    private String nome;
    private String email;
    private String senha;
    private String photoUrl;
    private Double saldoTotal;



    public Revendedor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSupervisor() {
        return idSupervisor;
    }

    public void setIdSupervisor(String idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Revendedor(String id, String idSupervisor, String nome, String email, String senha, String photoUrl, Double saldoTotal) {
        this.id = id;
        this.idSupervisor = idSupervisor;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.photoUrl = photoUrl;
        this.saldoTotal = saldoTotal;

    }


    public void salvarRevendedor(String idSupervisor) {
        DatabaseReference reference = ConfiguracoesFirebase.getFirebase();
        reference.child(idSupervisor +"/"+ConstantsUtils.BANCO_REVENDEDORES).child(String.valueOf(getId())).setValue(this);
        reference.child(String.valueOf(getId())).setValue(this);

    }
    @Exclude
    public Map<String, Object> map(){
        HashMap<String, Object> hashMapRevendedor = new HashMap<>();
        hashMapRevendedor.put("id", getId());
        hashMapRevendedor.put("nome", getNome());
        hashMapRevendedor.put("email", getEmail());
        hashMapRevendedor.put("senha", getSenha());
        hashMapRevendedor.put("photoUrl", getPhotoUrl());
        hashMapRevendedor.put("saldoTotal", getSaldoTotal());
        return  hashMapRevendedor;


    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(Double saldoTotal) {
        this.saldoTotal = saldoTotal;
    }
}
