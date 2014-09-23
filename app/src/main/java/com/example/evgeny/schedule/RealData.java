package com.example.evgeny.schedule;


import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RealData {
    String dateinddmm;
    String dateinhhmm;
    public String Data() {
        Date nowday = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowday);
        nowday = cal.getTime();
       DateFormat snowday = new SimpleDateFormat("HH:mm  dd.MM.yyyy");
        DateFormat bnowday = new SimpleDateFormat("dd.MM");
        DateFormat hnowday = new SimpleDateFormat("HHmm");
       String reportDate = snowday.format(nowday);
        dateinddmm = bnowday.format(nowday);
        dateinhhmm = hnowday.format(nowday);
        return reportDate;

    }
}
