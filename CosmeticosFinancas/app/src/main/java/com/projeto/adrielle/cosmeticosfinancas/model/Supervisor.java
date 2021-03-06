package com.projeto.adrielle.cosmeticosfinancas.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.projeto.adrielle.cosmeticosfinancas.DAO.ConfiguracoesFirebase;
import com.projeto.adrielle.cosmeticosfinancas.utils.ConstantsUtils;

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
    private String photoUrl;
    private String pathImagem;
    private String numero ;

    public String getPathImagem() {
        return pathImagem;
    }

    public void setPathImagem(String pathImagem) {
        this.pathImagem = pathImagem;
    }



    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }



    public Supervisor(String id, String nome, String email, String senha, String photoUrl, String pathImagem, String numero) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.photoUrl = photoUrl;
        this.pathImagem = pathImagem;
        this.numero = numero;
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

    //salvando dados no banco
    //salvando na tabela de supervisores e na tabela de fluxo
    public void salvarSupervisor() {
        DatabaseReference reference = ConfiguracoesFirebase.getFirebase();

        //tabela utilizada para controle de usuarios
        reference.child(ConstantsUtils.BANCO_SUPERVISORES).child(String.valueOf(getId())).setValue(this);

        //tabela para controle de fluxo de dados
        reference.child(String.valueOf(getId())).child("perfil").setValue(this);

    }

    @Exclude
    public Map<String, Object> map() {
        HashMap<String, Object> hashMapSupervisor = new HashMap<>();
        hashMapSupervisor.put("id", getId());
        hashMapSupervisor.put("nome", getNome());
        hashMapSupervisor.put("email", getEmail());
        hashMapSupervisor.put("senha", getSenha());
        hashMapSupervisor.put("pathImagem", getPathImagem());
        hashMapSupervisor.put("numero", getNumero());

        return hashMapSupervisor;


    }


    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void atualizaPerfil(String idSupervisor) {
        DatabaseReference reference = ConfiguracoesFirebase.getFirebase();

        //tabela utilizada para controle de usuarios
        reference.child(ConstantsUtils.BANCO_SUPERVISORES).child(String.valueOf(getId())).setValue(this);

        //tabela para controle de fluxo de dados
        reference.child(idSupervisor).child("perfil").setValue(this);


    }
}
