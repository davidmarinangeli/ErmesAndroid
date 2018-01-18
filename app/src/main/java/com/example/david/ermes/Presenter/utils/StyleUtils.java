package com.example.david.ermes.Presenter.utils;

import android.content.Context;

/**
 * Created by nicol on 13/01/2018.
 */

public class StyleUtils {
    public static int getDpByPixels(Context context, int pixels) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }

    public static int getPixelsByDp(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp - 0.5f) / scale);
    }
}
