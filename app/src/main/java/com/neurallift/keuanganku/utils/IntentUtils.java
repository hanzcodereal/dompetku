package com.neurallift.keuanganku.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.neurallift.keuanganku.R;

public class IntentUtils {

    public static void kirimEmail(Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + context.getString(R.string.info_email_alamat)));
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.info_email_subjek));
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.info_tidak_ada_email, Toast.LENGTH_SHORT).show();
        }
    }

    public static void bukaSaweria(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.info_saweria_url)));
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.info_tidak_ada_browser, Toast.LENGTH_SHORT).show();
        }
    }
}
