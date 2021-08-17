package com.example.easy_attendance;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    LottieAnimationView lottieAnimationView;
    private TextView register,forgotPassword;
    private EditText emailEditText,passwordEditText;
    private Button LogInButton;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(this);

        LogInButton = (Button)findViewById(R.id.loginButton);
        LogInButton.setOnClickListener(this);

        emailEditText = (EditText) findViewById(R.id.emailTextView);
        passwordEditText = (EditText) findViewById(R.id.passwordTextView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        lottieAnimationView = findViewById(R.id.login_animation);
        lottieAnimationView.animate().setStartDelay(1000);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogIn.super.onBackPressed();
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.register:
                startActivity(new Intent(this,RegisterUser.class));
                break;
            case R.id.loginButton:
                userLogIn();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this,ForgotPassword.class));
        }
    }

    private void userLogIn() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(email.isEmpty())
        {
            emailEditText.setError("Email address is required!");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText.setError("Invalid email address!");
            emailEditText.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            passwordEditText.setError("Password is required!");
            passwordEditText.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            passwordEditText.setError("Password length must be 6 character long");
            passwordEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        startActivity(new Intent(LogIn.this,MainActivity.class));
                        progressBar.setVisibility(View.GONE);
                        finish();
                    }else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(LogIn.this,"Check your email to verify your account!",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else
                {
                    Toast.makeText(LogIn.this,"Failed to Login! Check your credentials",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}