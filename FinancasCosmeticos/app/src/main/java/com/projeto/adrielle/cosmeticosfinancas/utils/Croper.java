package com.projeto.adrielle.cosmeticosfinancas.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikelau.croperino.AlertInterface;
import com.mikelau.croperino.CameraDialog;
import com.mikelau.croperino.CropImage;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;
import com.mikelau.croperino.InternalStorageContentProvider;
import com.projeto.adrielle.cosmeticosfinancas.PerfilActivity;
import com.tecnoia.matheus.financascosmeticos.R;

import java.io.File;
import java.io.IOException;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by matheus on 28/10/17.
 */

public class Croper {
    public static void runCropImage(File file, Activity ctx, boolean isScalable, int aspectX, int aspectY, int color, int bgColor) {
        Intent intent = new Intent(ctx, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, isScalable);
        intent.putExtra(CropImage.ASPECT_X, aspectX);
        intent.putExtra(CropImage.ASPECT_Y, aspectY);
        intent.putExtra("color", color);
        intent.putExtra("bgColor", bgColor);
        ctx.startActivityForResult(intent, CroperinoConfig.REQUEST_CROP_PHOTO);
    }

    public static void prepareChooser(final Activity ctx, AlertDialog dialog) {

        LayoutInflater inflater = ctx.getLayoutInflater();
        final View view1 = inflater.inflate(R.layout.adapter_dialog_camera, null);
        final TextView camera = view1.findViewById(R.id.selecet_camera);
        final TextView galeria = view1.findViewById(R.id.selecet_gealeria);




        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Escolha");

        builder.setView(view1);
        builder.setCancelable(false);


        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CroperinoFileUtil.verifyStoragePermissions(ctx)) {
                    prepareGallery(ctx);
                }
            }



        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CroperinoFileUtil.verifyCameraPermissions(ctx)){
                    prepareCamera(ctx);
                }

            }
        });


        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        dialog = builder.create();
        dialog.show();

    }

    public static void prepareCamera(Activity ctx) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri mImageCaptureUri;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                if (Uri.fromFile(CroperinoFileUtil.newCameraFile()) != null) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        mImageCaptureUri = FileProvider.getUriForFile(ctx,
                                ctx.getApplicationContext().getPackageName() + ".provider",
                                CroperinoFileUtil.newCameraFile());
                    } else {
                        mImageCaptureUri = Uri.fromFile(CroperinoFileUtil.newCameraFile());
                    }
                } else {
                    mImageCaptureUri = FileProvider.getUriForFile(ctx,
                            ctx.getApplicationContext().getPackageName() + ".provider",
                            CroperinoFileUtil.newCameraFile());
                }
            } else {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            ctx.startActivityForResult(intent, CroperinoConfig.REQUEST_TAKE_PHOTO);
        } catch (Exception e) {
            Crouton.cancelAllCroutons();
            if (e instanceof ActivityNotFoundException) {
                Crouton.makeText(ctx, "Activity not found", Style.ALERT).show();
            } else if (e instanceof IOException) {
                Crouton.makeText(ctx, "Image file captured not found", Style.ALERT).show();
            } else {
                Crouton.makeText(ctx, "Camera access failed", Style.ALERT).show();
            }
        }
    }

    public static void prepareGallery(Activity ctx) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        ctx.startActivityForResult(i, CroperinoConfig.REQUEST_PICK_FILE);
    }
}
