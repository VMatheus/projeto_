package com.projeto.adrielle.cosmeticosfinancas.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.projeto.adrielle.cosmeticosfinancas.DAO.ConfiguracoesFirebase;
import com.tecnoia.matheus.financascosmeticos.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by matheus on 18/08/17.
 */

public class ValidaCamposConexao {

    //classe para validação de campos

    public static boolean validaValorEstoque(Integer itensVenda, Integer estoqueAtual, Integer novoValor) {
        return novoValor <= itensVenda + estoqueAtual;
    }

    public static boolean validaSenha(String s) {
        return s.length() > 4;

    }

    public static boolean validaEmail(String e) {
        return e.contains("@");
    }

    public static boolean validaNome(String n) {
        return n.length() >= 1;

    }


    public static boolean verificaConexao(FragmentActivity activity) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        conectado = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return conectado;
    }


    public static BigDecimal formatStringToBigDecimal(String str) {
        str = str.replace(".", "");
        str = str.replace(",", ".");
        str = str.trim();
        return new BigDecimal(str);
    }

    public static String formataBigDecimalToString(BigDecimal bigDecimal) {
        DecimalFormat decFormat = new DecimalFormat("#,###,##0.00");
        return decFormat.format(bigDecimal);
    }




    public static void alertDialogDesconectar(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


        alertDialogBuilder
                .setMessage("Sair?")
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.sim),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                FirebaseAuth autenticacao = ConfiguracoesFirebase.getFirebaseAutenticacao();
                                autenticacao.signOut();



                            }
                        })
                .setNegativeButton(context.getString(R.string.nao),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                //chama a activity principal
                                dialog.dismiss();

                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);


        alertDialog.show();


    }

    public static String exibeValor(String valor) {
        Locale Local = new Locale("pt", "BR");
        //Number pra string
        // double value = 2637.64;
        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Local));
        String s = df.format(valor);
        //System.out.println(s);//imprime 2.637,64
        return s;
    }

    //prepara imagem inicial para upload
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


}
