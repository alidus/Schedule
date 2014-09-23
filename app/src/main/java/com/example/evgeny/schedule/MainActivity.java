package com.example.evgeny.schedule;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MainActivity extends Activity {
    String lastupdate = "";
    LinearLayout rl;
    String omode;
    String daystat;
    int s = 0;

    public Day[] days; //Создание массива days с типом Day


    int currentDay; //Начальное значение дней (т.е. Понедельник)
    int currentLesson;
    logfile logfile = new logfile();
    TextView dayView;
    TextView lesson1;
    TextView lesson2;
    TextView lesson3;
    TextView lesson4;
    TextView lesson5;
    TextView lesson6;
    TextView lesson7;
    TextView data;
    TextView mode;
    TextView status;
    ProgressBar progress;
    Button button1;
    Button button2;
    Button refresh;
    TextView lastu;
    TextView currentl = null;
    Animation anim;

    public class LessonAlphaChange extends AsyncTask<Void, Void, Void> {
        public Void doInBackground(Void... params) {


           RealData hhmm = new RealData();
           hhmm.Data();

           int time = Integer.parseInt(hhmm.dateinhhmm);
            Log.i("Time", String.valueOf(time));
            if(time >= Integer.parseInt(getResources().getString(R.string.l1s)) && time <= Integer.parseInt(getResources().getString(R.string.l1f))  )
            {
                currentLesson = 1;
                currentl = (TextView) findViewById(R.id.lesson1);

            }
            if(time >= Integer.parseInt(getResources().getString(R.string.l2s)) && time <= Integer.parseInt(getResources().getString(R.string.l2f))  )
            {
                currentLesson = 2;
                currentl = (TextView) findViewById(R.id.lesson2);
            }
            if(time >= Integer.parseInt(getResources().getString(R.string.l3s)) && time <= Integer.parseInt(getResources().getString(R.string.l3f))  )
            {
                currentLesson = 3;
                currentl = (TextView) findViewById(R.id.lesson3);
            }
            if(time >= Integer.parseInt(getResources().getString(R.string.l4s)) && time <= Integer.parseInt(getResources().getString(R.string.l4f))  )
            {
                currentLesson = 4;
                currentl = (TextView) findViewById(R.id.lesson4);
            }
            if(time >= Integer.parseInt(getResources().getString(R.string.l5s)) && time <= Integer.parseInt(getResources().getString(R.string.l5f))  )
            {
                currentLesson = 5;
                currentl = (TextView) findViewById(R.id.lesson5);
            }
            if(time >= Integer.parseInt(getResources().getString(R.string.l6s)) && time <= Integer.parseInt(getResources().getString(R.string.l6f))  )
            {
                currentLesson = 6;
                currentl = (TextView) findViewById(R.id.lesson6);
            }
            if(time >= Integer.parseInt(getResources().getString(R.string.l7s)) && time <= Integer.parseInt(getResources().getString(R.string.l7f))  )
            {
                currentLesson = 7;
                currentl = (TextView) findViewById(R.id.lesson7);
            }




            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (currentLesson > 1) {
                currentl.startAnimation(anim);
            }


        }
    }

    public class BGThread extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.INVISIBLE);
            refresh.setEnabled(false);
            Log.i("Visible", "Visible Set");
        }

        public Void doInBackground(Void... params) {


            try {

                RealData today = new RealData();
                URL url = new URL("https://www.dropbox.com/s/rlyi7c23sdk7bp7/schedule.txt?raw=1");  //создание объекта url типа URL
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                //Открытие потока in для загрузки информации из url
                File root = Environment.getExternalStorageDirectory();
                File saveDir = new File(root + "/Schedule");
                saveDir.mkdirs();
                File save = new File(saveDir, "save.txt");
                String TAG = "Schedule";

                FileOutputStream f = new FileOutputStream(save);
                PrintWriter pw = new PrintWriter(f);

                days = new Day[6];//Создание в памяти 6-ти местного массива дней
                String d;
                String n;
                String l;
                Data data = new Data();
                for (int i = 0; i < 6; i++) {  //Переберание дней для отображения
                    Day day = new Day();  //Создание экземпляра класса Day в main activity
                    day.dateofday = d = in.readLine();
                    pw.println(d);
                    day.name = n = in.readLine();  //Считывание названия дня из строки текста
                    pw.println(n);
                    day.lessons = new Lesson[7]; //Создание массива уроков в отдельно взятом дне
                    for (int g = 0; g < 7; g++) { //Перебор уроков в дне
                        Lesson lesson = new Lesson(); //Создание экземпляра класса Lesson в main activity
                        lesson.name = l = in.readLine(); //Считывание названия урока из строки текста
                        pw.println(l);
                        day.lessons[g] = lesson; //Занесение части массива в объект day с номером строки, сообветствующей условной переменной цикла
                    }
                    pw.println(today.Data());
                    lastupdate = today.Data();
                    if (data.Data().equals(day.dateofday)) {
                        currentDay = i;
                        Log.i("curday", String.valueOf(currentDay));

                    }
                    days[i] = day; //Присвоение части массива под номером i свойства считанного дня
                }
                pw.flush();
                pw.close();
                f.close();
                in.close(); //Закрытие потока in
                omode = getResources().getString(R.string.offline_mod_false);;
            } catch (IOException e) {
                Log.i("Server", "Server download problems");
                logfile.appendLog("server download problems");
                Data data = new Data();


                try {

                    Log.i("curday", String.valueOf(currentDay));
                    File save = new File(Environment.getExternalStorageDirectory().toString() + "/Schedule", "save.txt");

                    FileInputStream infile = new FileInputStream(save);

                    BufferedReader in = new BufferedReader(new InputStreamReader(infile));


                    days = new Day[6];//Создание в памяти 6-ти местного массива дней

                    for (int i = 0; i < 6; i++) {  //Переберание дней для отображения
                        Day day = new Day();  //Создание экземпляра класса Day в main activity
                        day.dateofday = in.readLine();

                        day.name = in.readLine();  //Считывание названия дня из строки текста

                        day.lessons = new Lesson[7]; //Создание массива уроков в отдельно взятом дне
                        for (int g = 0; g < 7; g++) { //Перебор уроков в дне
                            Lesson lesson = new Lesson(); //Создание экземпляра класса Lesson в main activity
                            lesson.name = in.readLine(); //Считывание названия урока из строки текста
                            day.lessons[g] = lesson; //Занесение части массива в объект day с номером строки, сообветствующей условной переменной цикла
                        }
                        if (data.Data().equals(day.dateofday)) {
                            currentDay = i;
                        }
                        lastupdate = in.readLine();
                        days[i] = day; //Присвоение части массива под номером i свойства считанного дня
                    }
                    in.close();
                    infile.close();
                    omode = getResources().getString(R.string.offline_mod_true);
                } catch (Exception n) {
                    Log.i("File", "No access to the internet and no save-file");
                    try {

                        days = new Day[6];//Создание в памяти 6-ти местного массива дней

                        for (int i = 0; i < 6; i++) {  //Переберание дней для отображения
                            Day day = new Day();  //Создание экземпляра класса Day в main activity
                            day.dateofday = "";

                            day.name = "";  //Считывание названия дня из строки текста

                            day.lessons = new Lesson[7]; //Создание массива уроков в отдельно взятом дне
                            for (int g = 0; g < 7; g++) { //Перебор уроков в дне
                                Lesson lesson = new Lesson(); //Создание экземпляра класса Lesson в main activity
                                lesson.name = ""; //Считывание названия урока из строки текста
                                day.lessons[g] = lesson; //Занесение части массива в объект day с номером строки, сообветствующей условной переменной цикла
                            }
                            days[i] = day; //Присвоение части массива под номером i свойства считанного дня
                            Log.i("No data", "No data");
                        }


                        omode = getResources().getString(R.string.offline_mod_ndf);;
                    } catch (Exception x) {
                        Log.i("No file", "Error in exception");
                    }
                }
                ;


            }

            return null;
        }

        ;

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            Day day = days[currentDay];    //Переменная day получает свойства для, выбранного из массива days под номером currentDay
            Log.i("day", "day detected");

            Data date = new Data();
            RealData dat = new RealData();
            dat.Data();
            String c = dat.dateinddmm;
            Log.i("date", "date detected");
            String d = date.Data(); //Объявление переменной d сегодняшней датой в виде dd/MM

            Lesson[] lessons = day.lessons; //Создает массив lessons, включающий в себя уроки из массива lessons обьекта days
            Log.i("lessons", "array of lessons detected");

            Log.i("daynameview", "day name view set");
            dayView.setText(day.name);
            lesson1.setText(lessons[0].name);
            lesson2.setText(lessons[1].name);
            lesson3.setText(lessons[2].name);
            lesson4.setText(lessons[3].name);
            lesson5.setText(lessons[4].name);
            lesson6.setText(lessons[5].name);
            lesson7.setText(lessons[6].name);
            data.setText(getResources().getString(R.string.schedule_for)+" " + day.dateofday);
            mode.setText(omode);

            lastu.setText(getResources().getString(R.string.last_update)+" " + lastupdate);
            if (day.dateofday.equals(d))

            {
                daystat = getResources().getString(R.string.tomorrow);
                status.setTextColor(Color.RED);
                rl.setBackgroundColor(Color.RED);
                rl.getBackground().setAlpha(48);
            } else {


                if (day.dateofday.equals(c)) {
                    daystat = getResources().getString(R.string.today);
                    status.setTextColor(Color.GREEN);
                    rl.setBackgroundColor(Color.GREEN);
                    rl.getBackground().setAlpha(24);
                } else {
                    daystat = "";
                    rl.setBackgroundColor(Color.CYAN);
                    rl.getBackground().setAlpha(16);
                }

            }
            status.setText(daystat);



            status.setText(daystat);
            progress.setVisibility(View.INVISIBLE);
            refresh.setBackgroundColor(Color.BLACK);
            refresh.getBackground().setAlpha(128);
            refresh.setVisibility(View.VISIBLE);
            refresh.setEnabled(true);
            LessonAlphaChange change = new LessonAlphaChange();
            change.execute();
        }
    }

    public class BGThreadFirst extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {


        }

        public Void doInBackground(Void... params) {


            try {
                Data data = new Data();
                Log.i("curday", String.valueOf(currentDay));
                File save = new File(Environment.getExternalStorageDirectory().toString() + "/Schedule", "save.txt");

                FileInputStream infile = new FileInputStream(save);

                BufferedReader in = new BufferedReader(new InputStreamReader(infile));


                days = new Day[6];//Создание в памяти 6-ти местного массива дней

                for (int i = 0; i < 6; i++) {  //Переберание дней для отображения
                    Day day = new Day();  //Создание экземпляра класса Day в main activity
                    day.dateofday = in.readLine();

                    day.name = in.readLine();  //Считывание названия дня из строки текста

                    day.lessons = new Lesson[7]; //Создание массива уроков в отдельно взятом дне
                    for (int g = 0; g < 7; g++) { //Перебор уроков в дне
                        Lesson lesson = new Lesson(); //Создание экземпляра класса Lesson в main activity
                        lesson.name = in.readLine(); //Считывание названия урока из строки текста
                        day.lessons[g] = lesson; //Занесение части массива в объект day с номером строки, сообветствующей условной переменной цикла
                    }
                    lastupdate = in.readLine();
                    Log.i("LA", lastupdate);
                    if (data.Data().equals(day.dateofday)) {
                        currentDay = i;
                    }
                    days[i] = day; //Присвоение части массива под номером i свойства считанного дня
                }

                Log.i("Preupdate", "last update time = " + lastupdate);
                in.close();
                infile.close();
                omode = getResources().getString(R.string.offline_mod_wait);
            } catch (Exception n) {
                Log.i("File", "No access to the internet and no save-file");
                try {

                    days = new Day[6];//Создание в памяти 6-ти местного массива дней

                    for (int i = 0; i < 6; i++) {  //Переберание дней для отображения
                        Day day = new Day();  //Создание экземпляра класса Day в main activity
                        day.dateofday = "";

                        day.name = "";  //Считывание названия дня из строки текста

                        day.lessons = new Lesson[7]; //Создание массива уроков в отдельно взятом дне
                        for (int g = 0; g < 7; g++) { //Перебор уроков в дне
                            Lesson lesson = new Lesson(); //Создание экземпляра класса Lesson в main activity
                            lesson.name = ""; //Считывание названия урока из строки текста
                            day.lessons[g] = lesson; //Занесение части массива в объект day с номером строки, сообветствующей условной переменной цикла
                        }
                        days[i] = day; //Присвоение части массива под номером i свойства считанного дня
                        Log.i("No data", "No data");
                    }


                    omode = getResources().getString(R.string.offline_mod_ndf);
                } catch (Exception x) {
                    Log.i("No file", "Error in exception");
                }
            }
            ;

            return null;
        }

        protected void onPostExecute(Void result) {
Log.i("starting PE of Bg","");
            super.onPostExecute(result);
            Day day = days[currentDay];    //Переменная day получает свойства для, выбранного из массива days под номером currentDay
            Log.i("day", "day detected");

            Data date = new Data();
            Log.i("date", "date detected");
            String d = date.Data(); //Объявление переменной d сегодняшней датой в виде dd/MM
            Lesson[] lessons = day.lessons; //Создает массив lessons, включающий в себя уроки из массива lessons обьекта days
            Log.i("lessons", "array of lessons detected");
            Log.i("323232", "423423234");

            Log.i("daynameview", "day name view set");
            dayView.setText(day.name);

            lesson1.setText(lessons[0].name);
            lesson2.setText(lessons[1].name);
            lesson3.setText(lessons[2].name);
            lesson4.setText(lessons[3].name);
            lesson5.setText(lessons[4].name);
            lesson6.setText(lessons[5].name);
            lesson7.setText(lessons[6].name);
            data.setText(getResources().getString(R.string.schedule_for)+ " " + day.dateofday);
            mode.setText(omode);

            Log.i("Preparing update time", "Update time set:" + lastupdate);
            lastu.setText(getResources().getString(R.string.last_update)+" " + lastupdate);
            RealData dat = new RealData();
            dat.Data();
            String c = dat.dateinddmm;
            Boolean truth;


                if (day.dateofday.equals(d))

                {
                    daystat = getResources().getString(R.string.tomorrow);
                    status.setTextColor(Color.RED);
                    rl.setBackgroundColor(Color.RED);
                    rl.getBackground().setAlpha(48);
                } else {


                    if (day.dateofday.equals(c)) {
                        daystat = getResources().getString(R.string.today);
                        status.setTextColor(Color.GREEN);
                        rl.setBackgroundColor(Color.GREEN);
                        rl.getBackground().setAlpha(24);
                    } else {
                        daystat = "";
                        rl.setBackgroundColor(Color.CYAN);
                        rl.getBackground().setAlpha(16);
                    }

                }
                status.setText(daystat);

        }
    }


    public void back() {  //Уменьшение значения текущего дня на 1
        if (currentDay == 0) {
            currentDay = days.length - 1;
        } else {
            currentDay--;
        }
        render();
    }

    public void ahead() {   //Увеличение значения текущего дня на 1
        if (currentDay == days.length - 1) {
            currentDay = 0;
        } else {
            currentDay++;
        }
        Log.i("Ahead","Starting render");
        render();
    }

    public void render() {                   //Перерисовывает расписание на экран

        Day day = days[currentDay];    //Переменная day получает свойства для, выбранного из массива days под номером currentDay
        Log.i("day", "day detected");

        Data date = new Data();
        Log.i("date", "date detected");
        String d = date.Data(); //Объявление переменной d сегодняшней датой в виде dd/MM
        Lesson[] lessons = day.lessons; //Создает массив lessons, включающий в себя уроки из массива lessons обьекта days
        Log.i("lessons", "array of lessons detected");
        dayView.setText(day.name);
        Log.i("daynameview", "day name view set");
        lesson1.setText(lessons[0].name);
        lesson2.setText(lessons[1].name);
        lesson3.setText(lessons[2].name);
        lesson4.setText(lessons[3].name);
        lesson5.setText(lessons[4].name);
        lesson6.setText(lessons[5].name);
        lesson7.setText(lessons[6].name);
        data.setText(getResources().getString(R.string.schedule_for)+ " " + day.dateofday);
        status.setText(omode);

        lastu.setText(getResources().getString(R.string.last_update)+" " + lastupdate);
        RealData dat = new RealData();
        dat.Data();
        String c = dat.dateinddmm;
        Log.i("Today",c);
        Log.i("comparation dod",day.dateofday);
        Log.i("comparation d",d);
        Log.i("comparation c",c);

        if (d.equals(day.dateofday))
        {
            Log.i("Compare ","equal");
        }
        else
        {
            Log.i("Compare ","not equal");
        }







        if (d.equals(day.dateofday))
        {
            Log.i("starting comparation","1");

            daystat = getResources().getString(R.string.tomorrow);
            rl.setBackgroundColor(Color.RED);
            status.setTextColor(Color.RED);
            rl.getBackground().setAlpha(48);
            Log.i("redner",String.valueOf(daystat));
        }
        else {


            if (c.equals(day.dateofday)) {
                Log.i("starting comparation","2");
                daystat = getResources().getString(R.string.today);
                status.setTextColor(Color.GREEN);
                rl.setBackgroundColor(Color.GREEN);
                rl.getBackground().setAlpha(24);
                Log.i("redner",String.valueOf(daystat));
            } else {
                Log.i("starting comparation","");
                daystat = "";
                rl.setBackgroundColor(Color.CYAN);
                rl.getBackground().setAlpha(16);
                Log.i("redner",String.valueOf(daystat));
            }

        }
        Log.i("Daystat", "Daystat = " + daystat);
        status.setText(daystat);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ChangeLog cl = new ChangeLog(this);
        if (cl.firstRun())
            cl.getLogDialog().show();

rl = (LinearLayout) findViewById(R.id.colorstat);
        mode = (TextView) findViewById(R.id.offline_mode);
        dayView = (TextView) findViewById(R.id.day);
        lesson1 = (TextView) findViewById(R.id.lesson1);
        lesson2 = (TextView) findViewById(R.id.lesson2);
        lesson3 = (TextView) findViewById(R.id.lesson3);
        lesson4 = (TextView) findViewById(R.id.lesson4);
        lesson5 = (TextView) findViewById(R.id.lesson5);
        lesson6 = (TextView) findViewById(R.id.lesson6);
        lesson7 = (TextView) findViewById(R.id.lesson7);
        data = (TextView) findViewById(R.id.data);
        status = (TextView) findViewById(R.id.updatestatus);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        refresh = (Button) findViewById(R.id.refresh);
        lastu = (TextView) findViewById(R.id.lastu);
        button1.setText(getResources().getString(R.string.back));
        button2.setText(getResources().getString(R.string.ahead));
        refresh.setText(getResources().getString(R.string.refresh));


        anim = new AlphaAnimation(1,0);
        anim.setDuration(1000);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);


        final BGThreadFirst bgthreadfirst = new BGThreadFirst();
        bgthreadfirst.execute();

        final BGThread bgthread = new BGThread();
        bgthread.execute();


        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                back();
                button1.setBackgroundColor(Color.BLACK);
                button1.getBackground().setAlpha(128);
            }
        });
        button1.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                button1.setBackgroundColor(Color.GRAY);
                button1.getBackground().setAlpha(128);
                return false;
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ahead();
                button2.setBackgroundColor(Color.BLACK);
                button2.getBackground().setAlpha(128);
            }
        });
        button2.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                button2.setBackgroundColor(Color.GRAY);
                button2.getBackground().setAlpha(128);
                return false;
            }
        });

        Log.i("0", "322");


        refresh.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v) {
                                           BGThread bgthread = new BGThread();
                                           bgthread.execute();
                                       }
                                   }
        );
        refresh.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                refresh.setBackgroundColor(Color.GRAY);
                refresh.getBackground().setAlpha(128);
                return false;
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
