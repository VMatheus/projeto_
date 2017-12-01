package com.projeto.adrielle.cosmeticosfinancas.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;

import java.lang.annotation.Annotation;

/**
 * Created by matheus on 30/10/17.
 */

public class GlideConfiguration implements com.bumptech.glide.module.GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

    }
}
