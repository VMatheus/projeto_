package com.projeto.adrielle.cosmeticosfinancas.adapters;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.projeto.adrielle.cosmeticosfinancas.model.Revendedor;
import com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments.ConsultarVendasFragment;
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;
import com.tecnoia.matheus.financascosmeticos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 23/08/17.
 */

public class AdapterRevendedores extends RecyclerView.Adapter<AdapterRevendedores.ViewHolder> {

    private List<Revendedor> revendedorList;
    private DatabaseReference databaseRevendedores;
    private FragmentActivity activity;
    private Bitmap circleBitmap;

    public AdapterRevendedores(FragmentActivity activity, ArrayList<Revendedor> revendedoresList, DatabaseReference databaserevendedores) {
        this.revendedorList = revendedoresList;
        this.activity = activity;
        this.databaseRevendedores = databaserevendedores;

    }

    public void atualiza(ArrayList<Revendedor> revendedoresList) {
        this.revendedorList = revendedoresList;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_revendedores, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Revendedor revendedor = revendedorList.get(position);
        try {

            holder.textViewNome.setText(revendedor.getNome());
            holder.textViewEmail.setText(revendedor.getEmail());
            holder.imageMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    menuRevendedor(holder.imageMenu, revendedor);
                }
            });


            try {
                Glide.with(activity).load(revendedor.getPhotoUrl()).into(holder.imageViewPerfil);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {


            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return revendedorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNome, textViewEmail;
        private ImageView imageViewPerfil, imageMenu;

        public ViewHolder(final View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.text_nome_revendedor);
            textViewEmail = itemView.findViewById(R.id.text_email_revendedor);
            imageMenu = itemView.findViewById(R.id.image_menu);
            imageViewPerfil = itemView.findViewById(R.id.image_perfil_revendedor);


        }
    }

    private void menuRevendedor(ImageView imageMenu, final Revendedor revendedor) {
        PopupMenu popupMenu = new PopupMenu(activity, imageMenu);
        final MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_list_revendedoras, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_consultar_vendas:

                        Bundle bundle = new Bundle();

                        bundle.putString("idRevendedora", revendedor.getId());
                        bundle.putString("nome", revendedor.getNome());
                        bundle.putString("email", revendedor.getEmail());
                        bundle.putString("senha", revendedor.getSenha());
                        bundle.putString("photoUrl", revendedor.getPhotoUrl());
                        bundle.putString("pathImagem", revendedor.getPathImagem());
                        bundle.putString("numero", revendedor.getNumero());
                        bundle.putString("saldoTotal", revendedor.getSaldoTotal());
                        Fragment fragment = ConsultarVendasFragment.newInstance();
                        fragment.setArguments(bundle);
                        FragmentUtils.replaceRetorno(activity, fragment);
                        break;
                }

                return true;
            }
        });
        popupMenu.show();
    }
}
