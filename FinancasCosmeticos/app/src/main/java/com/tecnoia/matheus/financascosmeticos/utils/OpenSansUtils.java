package com.tecnoia.matheus.financascosmeticos.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by matheus on 06/09/17.
 */

public class OpenSansUtils extends android.support.v7.widget.AppCompatTextView {
    Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), ConstantsUtils.FONT_OPEN_SANS);

    public OpenSansUtils(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public OpenSansUtils(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpenSansUtils(Context context) {
        super(context);
        init();
    }

    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(normalTypeface/*, -1*/);
    }

    public void init() {

        setTypeface(normalTypeface, 1);
    }
}