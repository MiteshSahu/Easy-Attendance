package com.example.easy_attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class Attendance extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter;
    List<String> data;
    SwipeFlingAdapterView swipeFlingAdapterView;
    int out=0;
    String title;
    Button present ,absent;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        swipeFlingAdapterView = findViewById(R.id.swipe);
        data = new ArrayList<>();

        int rollNo = getIntent().getExtras().getInt("Roll Number");
        String title = getIntent().getExtras().getString("Title");
        String userID = getIntent().getExtras().getString("UserID");
        this.userID= userID;
        this.title = title;
        String s[] = new String[rollNo];
        for(int i=1;i<=rollNo;i++)
        {
            data.add(i+"");
        }
        arrayAdapter = new ArrayAdapter<>(Attendance.this,R.layout.item,R.id.data,data);
        swipeFlingAdapterView.setAdapter(arrayAdapter);

        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                data.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                if(out<rollNo) {
                    s[out] = "Absent";
                    if(out==rollNo-1)
                    {
                       callResult(s);
                    }
                    out++;
                }
            }

            @Override
            public void onRightCardExit(Object o) {
                if(out<rollNo)
                {
                    s[out]="Present";
                    if(out==rollNo-1)
                    {
                        callResult(s);
                    }
                    out++;
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });
        present = findViewById(R.id.totalPresent);
        absent = findViewById(R.id.absent);

        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeFlingAdapterView.getTopCardListener().selectRight();
                if(out<rollNo) {
                    s[out] = "Present";
                    if(out==rollNo-1)
                    {
                        callResult(s);
                    }
                }
            }
        });

        absent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                swipeFlingAdapterView.getTopCardListener().selectLeft();
                if(out<rollNo) {
                    s[out] = "Absent";
                    if(out==rollNo-1)
                    {
                        callResult(s);
                    }
                }
            }
        });
    }
    public void callResult(String s[])
    {
        present.setVisibility(View.GONE);
        absent.setVisibility(View.GONE);
        Intent i = new Intent(Attendance.this,Result.class);
        i.putExtra("Stats Of Student",s);
        i.putExtra("Title",title);
        i.putExtra("UserID",userID);
        startActivity(i);
        finish();
    }}