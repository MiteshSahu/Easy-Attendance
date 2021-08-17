 package com.example.easy_attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 public class Result extends AppCompatActivity {

    TextView titleTextView,absentTextView,percentageTextView,presentTextView,historyTitle,historyRecord;
    ListView statsList;
    String userID,title;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        historyTitle = (TextView)findViewById(R.id.historyTitle);
        historyRecord = (TextView) findViewById(R.id.historyRecord);
        String userID = getIntent().getExtras().getString("UserID");
        this.userID = userID;
        title= getIntent().getExtras().getString("Title");
        String statsOfStudents[] = getIntent().getExtras().getStringArray("Stats Of Student");
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText(title);
        System.out.println("REsult me aagya");
        statsList = (ListView) findViewById(R.id.statsList);
        ArrayList<StudentsStats> arrayList= new ArrayList<>();
        for(int i=0;i<statsOfStudents.length;i++)
        {
            arrayList.add(new StudentsStats("Roll no. "+(i+1),""+statsOfStudents[i]));
        }
        int absent=0;
        int total = statsOfStudents.length;
        for(int i=0;i<statsOfStudents.length;i++)
        {
            if(statsOfStudents[i].equals("Absent"))
            {
                absent++;
            }
        }
        int present = total-absent;
        int percentage = (present*100)/total;
        absentTextView = findViewById(R.id.totalAbsent);
        presentTextView = findViewById(R.id.totalPresent);
        percentageTextView = findViewById(R.id.aggregatePercent);
        absentTextView.setText("Absent - "+absent);
        presentTextView.setText("Present - "+present);
        percentageTextView.setText("Percentage - "+percentage+"%");
        StudentAdapter studentAdapter = new StudentAdapter(this,R.layout.list_row,arrayList);
        statsList.setAdapter(studentAdapter);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        String strDate = dateFormat.format(date);
        title = title.concat(" "+strDate);
        String convertedStats="";

        for(int i=0;i<statsOfStudents.length;i++)
        {
            if(statsOfStudents[i].equals("Present"))
            {
                convertedStats=convertedStats.concat("1");
            }
            else
            {
                convertedStats=convertedStats.concat("0");
            }
        }
       saveData(title,convertedStats,userID);
    }


    public void saveData(String title,String convertedStats,String userID)
    {
        Map<String,Object> data  = new HashMap<>();
        //data.put("Titles",title);
        data.put("Records",convertedStats);
        db.collection(userID).document(title).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Result.this, "Data Saved!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(Result.this, "Failed to save!", Toast.LENGTH_SHORT).show();
            }
        });
       // loadData();
       // showData();
    }

//     private void showData() {
//         db.collection(userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//             @Override
//             public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                 if (task.isSuccessful()) {
//                     List<String> list = new ArrayList<>();
//                     for (QueryDocumentSnapshot document : task.getResult()) {
//                         list.add(document.getId());
//                     }
//                     System.out.println( "|||||----|||||||||_______||||-----"+list.toString());
//                    // History history = new History();
//                  //   history.setDetails(list);
//                 } else {
//                     Toast.makeText(Result.this,"Error while Fetching the data..",Toast.LENGTH_SHORT).show();
//                 }
//             }
//         });
//     }

     public void loadData()
    {
        dataRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String record = documentSnapshot.getString("Records");
                    historyTitle.setText("Record---------"+record);
                    historyRecord.setText(record+"-----");
                    db.collection(userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> list = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    list.add(document.getId());
                                }
                                System.out.println( "----"+list.toString());
                            } else {
                                Toast.makeText(Result.this,"Error while Fetching the data..",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else
                {
                    User u = new User("No Past Records!",Result.this);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(Result.this, "Loading Failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }
//     protected void onStart() {
//         super.onStart();
//         System.out.println("user id -"+userID+" title --"+title);
//         dataRef = db.collection(userID).document(title);
//         dataRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
//             @Override
//             public void onEvent( DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                 if(e!=null)
//                 {
//                     Toast.makeText(Result.this,"Error while Loading..",Toast.LENGTH_SHORT).show();
//                     return;
//                 }
//                 if(documentSnapshot.exists()){
//                     TextView historyTitle,historyRecord;
//                     String record = documentSnapshot.getString("Records");
//                     //History h = new History(record);
//                    // h.setDetails(record);
//                     db.collection(userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                         @Override
//                         public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                             if (task.isSuccessful()) {
//                                 List<String> list = new ArrayList<>();
//                                 for (QueryDocumentSnapshot document : task.getResult()) {
//                                     list.add(document.getId());
//                                 }
//                                 System.out.println( "|||||----|||||||||_______||||-----"+list.toString());
//                                 History history = new History();
//                                 history.setDetails(list);
//                             } else {
//                                 Toast.makeText(Result.this,"Error while Fetching the data..",Toast.LENGTH_SHORT).show();
//                             }
//                         }
//                     });
//                 }
//             }
//         });
//    }

 }
