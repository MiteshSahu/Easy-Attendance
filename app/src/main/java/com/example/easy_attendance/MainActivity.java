package com.example.easy_attendance;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_attendance.ui.main.SectionsPagerAdapter;
import com.example.easy_attendance.databinding.ActivityMainBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    LottieAnimationView animationView;
    private ActivityMainBinding binding;

    private FirebaseUser user;
    private DatabaseReference reference;
    String userID;
    TextView title;
    String sTitle;
    TextView number;
    String sNumber;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("order","MainActivity");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User userName = snapshot.getValue(User.class);
                if (userName != null) {
                    String uName = userName.name;
                    userName= new User(MainActivity.this,uName);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });


       // MainActivity.this.getSupportActionBar().hide();
//        final ActionBar actionBar = getActionBar();
//      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
    }
    public void callAttendance(View view) {
        title = findViewById(R.id.attendance_Title);
        sTitle = title.getText().toString().trim();
        number = findViewById(R.id.number_Of_Students);
        sNumber = number.getText().toString();
        System.out.println("numbers "+ sNumber);
        if(sNumber.isEmpty()&&sTitle.isEmpty())
        {
            title.setError("Enter the detail!");
            title.requestFocus();
            return;
        }
        if( Integer.parseInt(sNumber)==0)
        {
            number.setError("Enter the number of students!");
            number.requestFocus();
            return;
        }
        if(Integer.parseInt(sNumber)>200)
        {
            number.setError("Number of students should be less than 200");
            number.requestFocus();
            return;
        }
        if(sTitle.isEmpty())
        {
            title.setError("Title is required!");
            title.requestFocus();
            return;
        }
        animationView = findViewById(R.id.create_animated_Button);
        animationView.playAnimation();
        Intent i = new Intent(this,Attendance.class);
        i.putExtra("Roll Number",Integer.parseInt(sNumber));
        i.putExtra("Title",sTitle);
        i.putExtra("UserID",userID);
        startActivity(i);
    }

    public void onLogOut() {

        animationView.playAnimation();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you wish to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(MainActivity.this,LogIn.class);
                        Toast.makeText(MainActivity.this,"Logout Successful!!",Toast.LENGTH_LONG).show();
                        startActivity(i);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       animationView.playAnimation();
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();

    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


        public void showPopup(View v) {
            animationView = findViewById(R.id.optionMenu_animation);
            animationView.playAnimation();
            PopupMenu popup = new PopupMenu(this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_items, popup.getMenu());
            popup.show();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle().equals("Logout")) {
                        onLogOut();
                        return true;
                    }
                    if(item.getTitle().equals("Change Password")) {
                        showChangePasswordDialog();
                        return true;
                    }
                    if(item.getTitle().equals("Change Name")) {
                        showChangeNameDialog();
                        return true;
                    }
                    return false;
                }
            });
        }

    private void showChangeNameDialog() {
        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.update_name_dialogue,null);
        EditText newNameET = v.findViewById(R.id.changeName_dialogue);
        Button updateName = v.findViewById(R.id.changeName_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
        updateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = newNameET.getText().toString().trim();
                if(TextUtils.isEmpty(newName)){
                    newNameET.setError("Enter the name!");
                    newNameET.requestFocus();
                    return;
                }
                dialog.dismiss();
                updateName(newName);
            }
        });

    }

    private void updateName(String newName) {
        //reference = FirebaseDatabase.getInstance().getReference("Users");
        HashMap hashMap = new HashMap();
        hashMap.put("name",newName);
        reference.child(userID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                User u = new User(MainActivity.this,newName);
                Toast.makeText(MainActivity.this,"Name changed!",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(MainActivity.this,"Error while changing Name!"+e,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showChangePasswordDialog() {

        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.change_password_dialogue,null);
        EditText currentPasswordET = v.findViewById(R.id.current_password_dialogue);
        EditText newPasswordET = v.findViewById(R.id.new_password_dialogue);
        Button changePassword = v.findViewById(R.id.changePassword_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = currentPasswordET.getText().toString().trim();
                String newPassword = newPasswordET.getText().toString().trim();
                if(TextUtils.isEmpty(currentPassword)){
                    currentPasswordET.setError("Current Password is required!");
                    currentPasswordET.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(newPassword)){
                    newPasswordET.setError("Current Password is required!");
                    newPasswordET.requestFocus();
                    return;
                }
                if(newPassword.length()<6)
                {
                    newPasswordET.setError("Password length must be 6 character long");
                    newPasswordET.requestFocus();
                    return;
                }
                dialog.dismiss();
                updatePassword(currentPassword,newPassword);
            }
        });
    }
    public void updatePassword(String currentPassword,String newPassword)
    {
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(),currentPassword);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this,"Password changed!",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error while changing password!",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(MainActivity.this,"Error while changing password!",Toast.LENGTH_LONG).show();
            }
        });
    }

}