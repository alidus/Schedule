package com.example.evgeny.schedule;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Save {
    public void Saveaction() {
    File sdDir = Environment.getExternalStorageDirectory();
    File saveDir = new File(sdDir + "/Schedule/");
    File scsave = new File(saveDir,"save.txt");
    scsave.mkdir();

        if (scsave.exists())
        {

        }
        else
        {
            
        }
//        try
//        {
//            FileWriter fw = new FileWriter(scsave);
//        fw.write("тестовая запись");
//        fw.flush();
//        fw.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

}
