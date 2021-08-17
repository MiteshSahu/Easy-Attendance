package com.example.easy_attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StudentAdapter  extends ArrayAdapter<StudentsStats> {
 private Context mContext;
 private int mResource;
    public StudentAdapter(@NonNull @NotNull Context context, int resource, @NonNull @NotNull ArrayList<StudentsStats> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);
        TextView txtName = convertView.findViewById(R.id.rollno);
        TextView subTxt = convertView.findViewById(R.id.subStats);
        txtName.setText(getItem(position).getRollno());
        subTxt.setText(getItem(position).getStats());
        return convertView;

    }
}
