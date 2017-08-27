package com.tecnoia.matheus.financascosmeticos.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.tecnoia.matheus.financascosmeticos.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.utils.ConstantsUtils;
import com.tecnoia.matheus.financascosmeticos.utils.FragmentUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matheus on 17/08/17.
 */

public class Supervisor {
    private String id;
    private String nome;
    private String email;

    private String senha;

    public Supervisor(String id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Supervisor() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }



    public  void salvarSupervisor(){
        DatabaseReference reference = ConfiguracoesFirebase.getFirebase();
        reference.child(ConstantsUtils.BANCO_SUPERVISORES).child(String.valueOf(getId())).setValue(this);
        reference.child(String.valueOf(getId())).child("perfil").setValue(this);

    }

    @Exclude
    public Map<String, Object> map(){
        HashMap<String, Object> hashMapSupervisor = new HashMap<>();
        hashMapSupervisor.put("id", getId());
        hashMapSupervisor.put("nome", getNome());
        hashMapSupervisor.put("email", getEmail());
        hashMapSupervisor.put("senha", getSenha());
      return  hashMapSupervisor;


    }


}
