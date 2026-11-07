package com.neurallift.keuanganku.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.neurallift.keuanganku.R;

import java.util.Locale;

/**
 * Pengaturan tampilan grafik agar tulisan rapi, terbaca, dan berbahasa Indonesia.
 */
public class ChartUtils {

    private static final Locale ID = new Locale("in", "ID");

    private static int textColor(Context context) {
        return ContextCompat.getColor(context, R.color.colorPrimaryText);
    }

    private static Typeface font(Context context) {
        Typeface tf = ResourcesCompat.getFont(context, R.font.inter);
        return tf != null ? tf : Typeface.DEFAULT;
    }

    /** Persen ala Indonesia (36,4%). Irisan kecil (di bawah 4%) tidak diberi angka agar tidak bertumpuk. */
    public static ValueFormatter percentFormatter() {
        return new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value < 4f) return "";
                return String.format(ID, "%.1f%%", value);
            }
        };
    }

    /** Angka sumbu singkat: 1,5 jt / 300 rb. */
    public static String compactRupiah(float value) {
        float a = Math.abs(value);
        if (a >= 1_000_000_000f) return trim(value / 1_000_000_000f) + " M";
        if (a >= 1_000_000f) return trim(value / 1_000_000f) + " jt";
        if (a >= 1_000f) return trim(value / 1_000f) + " rb";
        return String.valueOf((int) value);
    }

    private static String trim(float x) {
        String s = String.format(ID, "%.1f", x);
        return s.endsWith(",0") ? s.substring(0, s.length() - 2) : s;
    }

    public static void stylePie(PieChart chart, Context context) {
        Typeface tf = font(context);
        chart.setDrawEntryLabels(false);
        int warnaKartu = ContextCompat.getColor(context, R.color.colorCardBackground);
        chart.setHoleColor(warnaKartu);
        chart.setTransparentCircleColor(warnaKartu);
        chart.setCenterTextColor(textColor(context));
        chart.setCenterTextTypeface(tf);
        chart.setEntryLabelTypeface(tf);
        chart.setExtraOffsets(8f, 8f, 8f, 8f);
        if (chart.getData() != null) {
            chart.getData().setValueTypeface(tf);
        }
        Legend legend = chart.getLegend();
        legend.setTypeface(tf);
        legend.setTextSize(12f);
        legend.setTextColor(textColor(context));
        legend.setWordWrapEnabled(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
    }

    public static void styleLine(LineChart chart, Context context) {
        Typeface tf = font(context);
        chart.setExtraBottomOffset(8f);

        XAxis x = chart.getXAxis();
        x.setTypeface(tf);
        x.setTextSize(11f);
        x.setTextColor(textColor(context));
        x.setDrawGridLines(false);
        x.setLabelCount(6, false);
        x.setLabelRotationAngle(0f);

        YAxis left = chart.getAxisLeft();
        left.setTypeface(tf);
        left.setTextSize(11f);
        left.setTextColor(textColor(context));
        left.enableGridDashedLine(8f, 8f, 0f);
        left.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return compactRupiah(value);
            }
        });
        chart.getAxisRight().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setTypeface(tf);
        legend.setTextSize(12f);
        legend.setTextColor(textColor(context));
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    }
}
