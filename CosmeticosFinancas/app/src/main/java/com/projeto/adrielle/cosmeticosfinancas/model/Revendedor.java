package com.projeto.adrielle.cosmeticosfinancas.model;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.projeto.adrielle.cosmeticosfinancas.DAO.ConfiguracoesFirebase;
import com.projeto.adrielle.cosmeticosfinancas.utils.ConstantsUtils;

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
    private String pathImagem;
    private String numero;
    private String saldoTotal;


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

    public Revendedor(String id, String idSupervisor, String nome, String email, String senha, String photoUrl, String pathImagem, String numero, String saldoTotal) {
        this.id = id;
        this.idSupervisor = idSupervisor;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.photoUrl = photoUrl;
        this.pathImagem = pathImagem;
        this.numero = numero;
        this.saldoTotal = saldoTotal;

    }
    //salvando dados no banco
    //salva revendedora tendo como parametro o id de sua supervisora

    public void salvarRevendedor(String idSupervisor) {
        DatabaseReference reference = ConfiguracoesFirebase.getFirebase();

        //salva revendora na tabela especifica de sua supervisora, para controle e monitoria das vendas
        reference.child(idSupervisor + "/" + ConstantsUtils.BANCO_REVENDEDORES).child(String.valueOf(getId())).setValue(this);

        //salva na base raiz para edicao dos dados
        reference.child(String.valueOf(getId())).setValue(this);

    }


    @Exclude
    public Map<String, Object> map() {
        HashMap<String, Object> hashMapRevendedor = new HashMap<>();
        hashMapRevendedor.put("id", getId());
        hashMapRevendedor.put("nome", getNome());
        hashMapRevendedor.put("email", getEmail());
        hashMapRevendedor.put("senha", getSenha());
        hashMapRevendedor.put("photoUrl", getPhotoUrl());
        hashMapRevendedor.put("pathImagem", getPathImagem());
        hashMapRevendedor.put("numero", getNumero());
        hashMapRevendedor.put("saldoTotal", getSaldoTotal());
        return hashMapRevendedor;


    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(String saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public String getPathImagem() {
        return pathImagem;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setPathImagem(String pathImagem) {
        this.pathImagem = pathImagem;
    }


    public void atualizaRevendedor(String idSupervisor, String idRevendedor) {
        DatabaseReference reference = ConfiguracoesFirebase.getFirebase();

        //atualiza revendora na tabela especifica de sua supervisora, para controle e monitoria das vendas
        reference.child(idSupervisor + "/" + ConstantsUtils.BANCO_REVENDEDORES + "/" + idRevendedor).setValue(this);

        //atualiza na base raiz para edicao dos dados
        reference.child(idRevendedor).setValue(this);


    }
    public void removerContaRevendedora(String idSupervisor, String idRevendedor) {
        try {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_REVENDEDORES+ "/"+ idRevendedor);
            reference.removeValue();

            DatabaseReference referenceProdutosVendas = FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.BANCO_PRODUTOS_VENDAS+ "/"+  idRevendedor);
            referenceProdutosVendas.removeValue();

            DatabaseReference referenceVendasRealizadas = FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.VENDAS_REALIZADAS+ "/"+  idRevendedor);
            referenceVendasRealizadas.removeValue();

            DatabaseReference referenceRaiz = FirebaseDatabase.getInstance().getReference(idRevendedor);
            referenceRaiz.removeValue();

        } catch (Exception e) {
            Log.e(e + "", "Erro remover produto estoque!");
        }

    }

    public void limparHistorico(String idRevendedora, String idSupervisor, FragmentActivity activity) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.VENDAS_REALIZADAS+ "/"+ idRevendedora);
        reference.removeValue();



    }
}

