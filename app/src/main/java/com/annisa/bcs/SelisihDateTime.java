package com.annisa.bcs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 8/6/2019.
 */

public class SelisihDateTime {
    // Method menghitung selisih dua waktu
    protected static String selisihDateTime(Date waktuSatu, Date waktuDua) {
        long selisihMS = Math.abs(waktuSatu.getTime() - waktuDua.getTime());
        long selisihDetik = selisihMS / 1000 % 60;
        long selisihMenit = selisihMS / (60 * 1000) % 60;
        long selisihJam = selisihMS / (60 * 60 * 1000) % 24;
        long selisihHari = selisihMS / (24 * 60 * 60 * 1000);
        String selisih = selisihJam + " Jam "
                + selisihMenit + " Menit " ;
        return selisih;
    }

    protected static String brutoDateTime(Date waktuSatu, Date waktuDua) {
        long selisihMS = Math.abs(waktuSatu.getTime() - waktuDua.getTime());
        long selisihDetik = selisihMS / 1000 % 60;
        long selisihMenit = selisihMS / (60 * 1000) % 60;
        long selisihJam = selisihMS / (60 * 60 * 1000) % 24;
        long selisihHari = selisihMS / (24 * 60 * 60 * 1000);
        long bruto = (selisihJam - 8);
        String hasilBruto = bruto + " Jam "
                + selisihMenit + " Menit " ;
        return hasilBruto;
    }

    protected static String nettDateTime(Date waktuSatu, Date waktuDua) {
        long selisihMS = Math.abs(waktuSatu.getTime() - waktuDua.getTime());
        long selisihDetik = selisihMS / 1000 % 60;
        long selisihMenit = selisihMS / (60 * 1000) % 60;
        long selisihJam = selisihMS / (60 * 60 * 1000) % 24;
        long selisihHari = selisihMS / (24 * 60 * 60 * 1000);
        long bruto = (selisihJam - 8);
        long Zmenit = (bruto*60) + (selisihMenit-30);
        long jamnett = Zmenit/60;
        long menitnett = Zmenit % 60;
        String hasilNett = jamnett + " Jam " + menitnett + " Menit";
        return hasilNett;
    }

    protected static String nettJam(Date waktuSatu, Date waktuDua) {
        long selisihMS = Math.abs(waktuSatu.getTime() - waktuDua.getTime());
        long selisihDetik = selisihMS / 1000 % 60;
        long selisihMenit = selisihMS / (60 * 1000) % 60;
        long selisihJam = selisihMS / (60 * 60 * 1000) % 24;
        long selisihHari = selisihMS / (24 * 60 * 60 * 1000);
        long bruto = (selisihJam - 8);
        long Zmenit = (bruto*60) + (selisihMenit-30);
        long jamnett = Zmenit/60;
        long menitnett = Zmenit % 60;
        String hasilNett = jamnett + "";
        return hasilNett;
    }

    protected static Date konversiStringkeDate(String tanggalDanWaktuStr,
                                               String pola, Locale lokal) {
        Date tanggalDate = null;
        SimpleDateFormat formatter;
        if (lokal == null) {
            formatter = new SimpleDateFormat(pola);
        } else {
            formatter = new SimpleDateFormat(pola, lokal);
        }
        try {
            tanggalDate = formatter.parse(tanggalDanWaktuStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return tanggalDate;
    }

}
