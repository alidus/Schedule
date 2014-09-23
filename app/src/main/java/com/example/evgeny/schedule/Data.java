package com.example.evgeny.schedule;


import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Data {
    public String Data() {
        Date nowday = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowday);
        cal.add(Calendar.DATE,1);
        nowday = cal.getTime();
       DateFormat snowday = new SimpleDateFormat("dd.MM");
       String reportDate = snowday.format(nowday);
        Log.i("Tomorrow from Data", reportDate);

        return reportDate;
//        DateFormat df = new SimpleDateFormat("dd.MM");
//        Date today = Calendar.getInstance().getTime();
//        String reportDate = df.format(today);
//        return reportDate;
    }
}
