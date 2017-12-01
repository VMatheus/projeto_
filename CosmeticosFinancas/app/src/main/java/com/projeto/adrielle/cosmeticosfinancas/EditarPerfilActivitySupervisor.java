package com.projeto.adrielle.cosmeticosfinancas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;
import com.projeto.adrielle.cosmeticosfinancas.model.Supervisor;
import com.projeto.adrielle.cosmeticosfinancas.utils.ConstantsUtils;
import com.projeto.adrielle.cosmeticosfinancas.utils.GetDataFromFirebase;
import com.projeto.adrielle.cosmeticosfinancas.utils.Utilitarios;
import com.projeto.adrielle.cosmeticosfinancas.R;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class EditarPerfilActivitySupervisor extends AppCompatActivity {
    private ImageView imageView;
    private AlertDialog dialog;
    private SharedPreferences sharedPrefSupervisor;
    String idSupervisor, nome, email, senha, photoUrl, pathImagem, numero;
    private DatabaseReference referencePerfil;
    private EditText editTextNome, editTextNumero, editTextEmail;
    private FirebaseUser firebaseUser;
    private String imgPathEdit, imgPathUpdate;
    private StorageReference storageReference;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private static String TAG = "Adicionado ae banco";
    private boolean cancel = false;
    private View focusView = null;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        storageReference = FirebaseStorage.getInstance().getReference();

        imageView = findViewById(R.id.image);

        editTextNome = findViewById(R.id.edit_nome);
        editTextNumero = findViewById(R.id.edit_numero);
        editTextEmail = findViewById(R.id.edit_email);
        editTextNome.setError(null);
        editTextNumero.setError(null);
        editTextEmail.setError(null);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // recupera id
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        idSupervisor = firebaseUser.getUid();

        //carrega dados contidos no banco

        preencherPerfil();


        new CroperinoConfig("IMG_" + System.currentTimeMillis() + ".png", "/Financas_Cosmeticos/Pictures", "/sdcard/Financas_Cosmeticos/Pictures");
        CroperinoFileUtil.setupDirectory(EditarPerfilActivitySupervisor.this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //libmodificada
                if (CroperinoFileUtil.verifyStoragePermissions(EditarPerfilActivitySupervisor.this)) {
                    selecionarImagem();

                }


            }
        });


    }

    private void preencherPerfil() {

        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        referencePerfil = FirebaseDatabase.getInstance().getReference(idSupervisor + "/" + ConstantsUtils.PERFIL);
        referencePerfil.keepSynced(true);
        referencePerfil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Supervisor supervisor = dataSnapshot.getValue(Supervisor.class);
                    editTextNome.setText(supervisor.getNome());
                    editTextNumero.setText(supervisor.getNumero());
                    editTextEmail.setText(supervisor.getEmail());
                    imgPathEdit = supervisor.getPathImagem();
                    senha = supervisor.getSenha();


                    Glide.with(EditarPerfilActivitySupervisor.this).load(supervisor.getPhotoUrl()).into(imageView);


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void selecionarImagem() {
        Utilitarios.alertDialogNewImage(this);


    }

    private void prepareCamera() {
        Croperino.prepareCamera(EditarPerfilActivitySupervisor.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CroperinoConfig.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Croperino.runCropImage(CroperinoFileUtil.getmFileTemp(), EditarPerfilActivitySupervisor.this, true, 1, 1, 0, 0);
                }
                break;
            case CroperinoConfig.REQUEST_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(data, EditarPerfilActivitySupervisor.this);
                    Croperino.runCropImage(CroperinoFileUtil.getmFileTemp(), EditarPerfilActivitySupervisor.this, true, 1, 1, 0, 0);
                }
                break;
            case CroperinoConfig.REQUEST_CROP_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Uri i = Uri.fromFile(CroperinoFileUtil.getmFileTemp());
                    imageView.setImageURI(i);
                }
                break;
            default:
                break;


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CroperinoFileUtil.REQUEST_CAMERA) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.CAMERA)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        prepareCamera();
                    }
                }
            }
        } else if (requestCode == CroperinoFileUtil.REQUEST_EXTERNAL_STORAGE) {
            boolean wasReadGranted = false;
            boolean wasWriteGranted = false;

            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        wasReadGranted = true;
                    }
                }
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        wasWriteGranted = true;
                    }
                }
            }

            if (wasReadGranted && wasWriteGranted) {
                selecionarImagem();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_editar_perfil, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                break;
            case R.id.save:
                validaCampos();


                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void validaCampos() {
        // valida
        nome = editTextNome.getText().toString();
        numero = editTextNumero.getText().toString();
        email = editTextEmail.getText().toString();

        if (TextUtils.isEmpty(nome)) {
            editTextNome.setError(getString(R.string.campo_vazio));
            focusView = editTextNome;
            cancel = true;

        } else if (!Utilitarios.validaNome(nome)) {
            editTextNome.setError(getString(R.string.nome_invalido));
            focusView = editTextNome;
            cancel = true;


        }
        if (TextUtils.isEmpty(numero)) {
            editTextNumero.setError(getString(R.string.campo_vazio));
            focusView = editTextNumero;
            cancel = true;

        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.campo_vazio));
            focusView = editTextEmail;
            cancel = true;

        } else if (!Utilitarios.validaEmail(email)) {
            editTextEmail.setError(getString(R.string.email_invalido));
            focusView = editTextEmail;
            cancel = true;

        }
        if (cancel) {
            focusView.requestFocus();

        } else {
            showProgressDialog();
            atualizaPerfil(idSupervisor, nome, email, senha, imgPathEdit, numero);


        }

    }

    private void atualizaPerfil(final String idSupervisor, final String nome, final String email, final String senha, String imgPathEdit, final String numero) {
        //exclui a imagem atual no banco
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReferenceFromUrl("gs://fir-fcm-40651.appspot.com/").child(imgPathEdit);
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] data = outputStream.toByteArray();
                imgPathUpdate = UUID.randomUUID().toString()+".png";

                StorageReference storageImagem = storageReference.child("/").child(imgPathUpdate);

                StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata(TAG, "perfil").build();


                UploadTask uploadTask = storageImagem.putBytes(data, metadata);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri uri = taskSnapshot.getDownloadUrl();

                        photoUrl = uri.toString();
                        Supervisor supervisor = new Supervisor(idSupervisor, nome, email, senha, photoUrl, imgPathUpdate, numero);
                        supervisor.atualizaPerfil(idSupervisor);


                        progressDialog.dismiss();
                        finish();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] data = outputStream.toByteArray();
                imgPathUpdate = UUID.randomUUID().toString()+".png";

                StorageReference storageImagem = storageReference.child("/").child(imgPathUpdate);

                StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata(TAG, "perfil").build();


                UploadTask uploadTask = storageImagem.putBytes(data, metadata);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri uri = taskSnapshot.getDownloadUrl();

                        photoUrl = uri.toString();
                        Supervisor supervisor = new Supervisor(idSupervisor, nome, email, senha, photoUrl, imgPathUpdate, numero);
                        supervisor.atualizaPerfil(idSupervisor);


                        progressDialog.dismiss();
                        finish();
                    }
                });


            }
        });

    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(EditarPerfilActivitySupervisor.this);

        progressDialog.setMessage(getString(R.string.aguarde));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }


}