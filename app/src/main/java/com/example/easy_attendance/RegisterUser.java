package com.example.easy_attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private TextView banner,registerButton;
    private EditText nameTextView,emailTextView,passwordTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        banner = (TextView)findViewById(R.id.banner);
        banner.setOnClickListener(this);
        registerButton = (TextView) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        nameTextView = (EditText) findViewById(R.id.nameTextView);
        emailTextView = (EditText) findViewById(R.id.emailTextViewRegister);
        passwordTextView = (EditText) findViewById(R.id.passwordTextViewRegister);

        progressBar = (ProgressBar) findViewById(R.id.progressBarRegister);

    }
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.banner:
                startActivity(new Intent(this,LogIn.class));
                break;

            case R.id.registerButton:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        String email = emailTextView.getText().toString().trim();
        String password = passwordTextView.getText().toString().trim();
        String name = nameTextView.getText().toString().trim();

        if(name.isEmpty())
        {
            nameTextView.setError("Name is required!");
            nameTextView.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            emailTextView.setError("Email address is required!");
            emailTextView.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailTextView.setError("Invalid email address!");
            emailTextView.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            passwordTextView.setError("Password is required!");
            passwordTextView.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            passwordTextView.setError("Password length must be 6 character long");
            passwordTextView.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            User user = new User(name,email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterUser.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterUser.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                              });
                        }
                        else
                        {
                            Toast.makeText(RegisterUser.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }
}