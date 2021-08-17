package com.example.easy_attendance;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class User {
    public String name , email;
    public Activity activity;
    TextView historyTitles,historyRecords;

    public User()
    {

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
   public User(Activity activity,String name)
   {
        this.activity =activity;
       TextView greeting = (TextView) this.activity.findViewById(R.id.greeting);
       greeting.setText("Welcome, "+ name+"!");
   }
    public User(Activity activity,String title,String record)
    {
        this.activity = activity;
        historyTitles =  this.activity.findViewById(R.id.historyTitle);
        historyRecords =  this.activity.findViewById(R.id.historyRecord);
        historyTitles.setText("Title:- "+title);
        historyRecords.setText("Record:- "+ record);
    }
    public  User(String s,Activity activity)
    {
        System.out.println("Record  ..-.-.--..-.-.-.-.-.-.-.-.-,-,-,-"+s);
        this.activity = activity;
        historyTitles = (TextView) this.activity.findViewById(R.id.historyTitle);
        System.out.println("Record  ..-.-.--..-.-.-.-.-.-.-.-.-,-,-,-"+s);
        historyTitles.setText("-------"+s);
    }

}
