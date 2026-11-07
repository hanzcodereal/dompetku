package com.neurallift.keuanganku.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

/** Menyimpan dan menerapkan pilihan tema (terang, gelap, atau mengikuti sistem). */
public class TemaUtils {

    public static final int TERANG = 0;
    public static final int GELAP = 1;
    public static final int SISTEM = 2;

    private static final String PREFS = "dompetku_pengaturan";
    private static final String KEY_TEMA = "tema";

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static int getTema(Context context) {
        return prefs(context).getInt(KEY_TEMA, SISTEM);
    }

    public static void setTema(Context context, int tema) {
        prefs(context).edit().putInt(KEY_TEMA, tema).apply();
        terapkan(tema);
    }

    /** Dipanggil saat aplikasi mulai agar tema tersimpan langsung dipakai. */
    public static void terapkan(Context context) {
        terapkan(getTema(context));
    }

    private static void terapkan(int tema) {
        int mode;
        if (tema == TERANG) {
            mode = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (tema == GELAP) {
            mode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
    }
}
