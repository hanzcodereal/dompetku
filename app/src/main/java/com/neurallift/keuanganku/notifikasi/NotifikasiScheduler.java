package com.neurallift.keuanganku.notifikasi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Menjadwalkan pengingat dengan AlarmManager sehingga tetap muncul
 * walaupun aplikasi tidak sedang dibuka.
 */
public class NotifikasiScheduler {

    public static final int REQ_HARIAN = 1001;
    public static final int REQ_TARGET = 1002;
    public static final String EXTRA_JENIS = "jenis";

    private static final String PREFS = "dompetku_notifikasi";
    private static final String KEY_HARIAN = "harian_aktif";
    private static final String KEY_TARGET = "target_aktif";
    private static final String KEY_JAM = "jam";
    private static final String KEY_MENIT = "menit";

    private static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static boolean isHarianAktif(Context c) { return prefs(c).getBoolean(KEY_HARIAN, false); }
    public static void setHarianAktif(Context c, boolean aktif) { prefs(c).edit().putBoolean(KEY_HARIAN, aktif).apply(); }

    public static boolean isTargetAktif(Context c) { return prefs(c).getBoolean(KEY_TARGET, false); }
    public static void setTargetAktif(Context c, boolean aktif) { prefs(c).edit().putBoolean(KEY_TARGET, aktif).apply(); }

    public static int getJam(Context c) { return prefs(c).getInt(KEY_JAM, 20); }
    public static int getMenit(Context c) { return prefs(c).getInt(KEY_MENIT, 0); }
    public static void setWaktu(Context c, int jam, int menit) {
        prefs(c).edit().putInt(KEY_JAM, jam).putInt(KEY_MENIT, menit).apply();
    }

    /** Jadwalkan ulang semua pengingat yang aktif (dipanggil saat aplikasi dibuka dan setelah HP menyala). */
    public static void jadwalkanSemua(Context c) {
        if (isHarianAktif(c)) jadwalkanHarian(c); else batalkan(c, REQ_HARIAN);
        if (isTargetAktif(c)) jadwalkanTarget(c); else batalkan(c, REQ_TARGET);
    }

    /** Pengingat harian: setiap hari pada jam yang dipilih. */
    public static void jadwalkanHarian(Context c) {
        Calendar cal = waktuBerikutnya(c, -1);
        pasang(c, REQ_HARIAN, cal.getTimeInMillis());
    }

    /** Pengingat target tabungan: setiap hari Senin pada jam yang dipilih. */
    public static void jadwalkanTarget(Context c) {
        Calendar cal = waktuBerikutnya(c, Calendar.MONDAY);
        pasang(c, REQ_TARGET, cal.getTimeInMillis());
    }

    public static void batalkan(Context c, int requestCode) {
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        if (am != null) am.cancel(buatPendingIntent(c, requestCode));
    }

    /** hariDalamMinggu = -1 berarti setiap hari. */
    private static Calendar waktuBerikutnya(Context c, int hariDalamMinggu) {
        long sekarang = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, getJam(c));
        cal.set(Calendar.MINUTE, getMenit(c));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (hariDalamMinggu > 0) {
            cal.set(Calendar.DAY_OF_WEEK, hariDalamMinggu);
            while (cal.getTimeInMillis() <= sekarang + 60_000L) cal.add(Calendar.DAY_OF_YEAR, 7);
        } else {
            while (cal.getTimeInMillis() <= sekarang + 60_000L) cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return cal;
    }

    private static void pasang(Context c, int requestCode, long waktu) {
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;
        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, waktu, buatPendingIntent(c, requestCode));
    }

    private static PendingIntent buatPendingIntent(Context c, int requestCode) {
        Intent intent = new Intent(c, NotifikasiReceiver.class);
        intent.putExtra(EXTRA_JENIS, requestCode);
        return PendingIntent.getBroadcast(c, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}
