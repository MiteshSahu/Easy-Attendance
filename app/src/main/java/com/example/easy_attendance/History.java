package com.example.easy_attendance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class History extends Fragment {
    @Nullable
    @org.jetbrains.annotations.Nullable
    View view;
    String title,userID;
    List list;
    LayoutInflater lf;
    TextView historyTitles,historyRecords;
    SwipeRefreshLayout refreshLayout;
    private FirebaseUser user;
    private DocumentReference dataRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.history,container,false);
        lf = getActivity().getLayoutInflater();
        view =  lf.inflate(R.layout.history, container, false); //pass the correct layout name for the fragment
        historyTitles = (TextView) view.findViewById(R.id.historyTitle);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        getData();
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               getData();
                refreshLayout.setRefreshing(false);
            }
        });
        return  view;
    }

    private void getData() {
        db.collection(""+userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    setDetails(list);
                } else {
                    //Toast.makeText(History.this,"Error while Fetching the data..",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setDetails(List list) {

        this.list = list;

        ListView listView = (ListView) view.findViewById(R.id.titles_listview);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemName = (String) parent.getItemAtPosition(position);
                Intent i = new Intent(getActivity(), Record.class);
                i.putExtra("itemName",itemName);
                i.putExtra("userID",userID);
                startActivity(i);
            }
        });
    }

}
