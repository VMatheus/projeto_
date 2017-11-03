package com.projeto.adrielle.cosmeticosfinancas.adapters;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.projeto.adrielle.cosmeticosfinancas.model.Revendedor;
import com.projeto.adrielle.cosmeticosfinancas.supervisor.fragments.ConsultarVendasFragment;
import com.projeto.adrielle.cosmeticosfinancas.utils.FragmentUtils;
import com.tecnoia.matheus.financascosmeticos.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by matheus on 23/08/17.
 */

public class AdapterRevendedores extends RecyclerView.Adapter<AdapterRevendedores.ViewHolder> {

    private List<Revendedor> revendedorList;
    private DatabaseReference databaseRevendedores;
    private FragmentActivity activity;
    private Bitmap circleBitmap;

    private FirebaseAuth mAuth1, mAuth2;
    private FirebaseApp myApp;
    private ProgressDialog progressDialog;

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

                        Fragment fragment = ConsultarVendasFragment.newInstance();
                        fragment.setArguments(bundle);
                        FragmentUtils.replaceRetorno(activity, fragment);
                        break;

                    case R.id.item_remover_revendedor:

                        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setMessage("Você tem certeza que deseja  excluir esse usuario?");
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        });
                        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {




                /*
*/

                            }
                        });


                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                showProgressDialog();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                removerConta(revendedor.getEmail(), revendedor.getSenha(), revendedor.getId(), progressDialog, user.getUid().toString());


                            }


                        });


                        break;
                }

                return true;
            }
        });
        popupMenu.show();
    }

    private void removerConta(String email, String senha, final String idRevendedora, final ProgressDialog progressDialog, final String idSupervisor) {
        mAuth1 = FirebaseAuth.getInstance();


        try {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://fir-fcm-40651.firebaseio.com/")
                    .setApiKey("AIzaSyBvaCYgm4KanJ7JhvBlvYjsBGVQB3UVGOI")
                    .setApplicationId("1:855606521832:android:be1214118720ccaa").build();

            myApp = FirebaseApp.initializeApp(activity, firebaseOptions, UUID.randomUUID() + "");

            mAuth2 = FirebaseAuth.getInstance(myApp);


        } catch (Exception e) {
            e.printStackTrace();
        /*  Toast.makeText(getActivity(),"Erro! " + e, Toast.LENGTH_SHORT).show();*/
            Log.e(e + "", "erro!");

        }

        mAuth2.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth2.getCurrentUser();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("USER", "User account deleted.");
                                                //deletar dados
                                                Revendedor revendedor = new Revendedor();
                                                revendedor.removerContaRevendedora(idSupervisor, idRevendedora);

                                                progressDialog.dismiss();
                                            } else {
                                                Log.d("USER", "User account not deleted.");
                                            }
                                        }
                                    });


                        } else {
                            progressDialog.dismiss();


                        }

                        // ...
                    }
                });


    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(activity.getString(R.string.aguarde));
        progressDialog.show();

    }
}
