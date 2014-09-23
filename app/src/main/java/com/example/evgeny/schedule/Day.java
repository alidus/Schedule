package com.example.evgeny.schedule;


public class Day {

    public Day(String imput_name,String imput_date,Lesson [] lesson1) {
        name = imput_name;
        lessons = lesson1;
        dateofday = imput_date;
    }
    public Day()
    {

    }
    public String name;
    public Lesson [] lessons;
    public String dateofday;
}
