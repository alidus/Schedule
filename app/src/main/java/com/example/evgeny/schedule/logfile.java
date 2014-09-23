package com.example.evgeny.schedule;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Evgeny on 11.09.2014.
 */
public class logfile {
    RealData data = new RealData();
    public void appendLog(String text)
    {
        File logFile = new File(Environment.getExternalStorageDirectory()+"/Schedule/log.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.i("Creating file err","createNewFile");
            }
        }
        try
        {

            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(data.Data()+"  ");
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Write error","Write in log file error");
        }
    }
}
