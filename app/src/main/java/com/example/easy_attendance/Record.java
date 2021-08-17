package com.example.easy_attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Record extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference dataRef ;
    String title[],itemName;
    TextView title_Record,absentTextView_record,percentageTextView_record,presentTextView_record;
    ListView statsList_Record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        String itemName = getIntent().getExtras().getString("itemName");
        String userID = getIntent().getExtras().getString("userID");
        this.itemName = itemName;
        title = itemName.split(" ");
        dataRef = db.collection(""+userID).document(""+itemName);
        dataRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String record = documentSnapshot.getString("Records");
                    showData(record);
                }else {
                    Toast.makeText(Record.this,"Data not found",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d(TAG,e.toString());
            }
        });
    }

    private void showData(String record) {

        title_Record = (TextView) findViewById(R.id.title_Record);
        title_Record.setText(title[0]);
        statsList_Record = (ListView) findViewById(R.id.statsList_Record);
        ArrayList<String> arrayList= new ArrayList<>();
        for(int i=0;i<record.length();i++)
        {
            if(record.charAt(i)=='0')
            {
                arrayList.add("Roll no."+(i+1)+"       Absent");
            }
            else
            {
                arrayList.add("Roll no."+(i+1)+"       Present");
            }
        }
        int absent=0;
        int total = record.length();
        for(int i=0;i<record.length();i++)
        {
            if(record.charAt(i)=='0')
            {
                absent++;
            }
        }
        int present = total-absent;
        int percentage = (present*100)/total;
        absentTextView_record = findViewById(R.id.totalAbsent_Record);
        presentTextView_record = findViewById(R.id.totalPresent_Record);
        percentageTextView_record = findViewById(R.id.aggregatePercent_Record);
        absentTextView_record.setText("Absent - "+absent);
        presentTextView_record.setText("Present - "+present);
        percentageTextView_record.setText("Percentage - "+percentage+"%");
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(Record.this, android.R.layout.simple_list_item_1,arrayList);
        statsList_Record.setAdapter(listAdapter);

    }
}