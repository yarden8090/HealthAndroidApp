package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class login extends AppCompatActivity {


    private TextView registerBtn;
    private EditText emailTextView, passwordTextView;
    private Button Btn;//login
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailTextView = findViewById(R.id.etEmail);
        passwordTextView = findViewById(R.id.etPassword);
        Btn = findViewById(R.id.btnLogin);
        registerBtn = findViewById(R.id.tvForgotPassword);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              loginUserAccount();

            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, registration.class));
            }
        });
    }

    private void loginUserAccount() {
        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        if (email.isEmpty() && password.isEmpty()){
            Toast.makeText(getApplicationContext(), "enter mail/ pass!!", Toast.LENGTH_LONG).show();
        }else {
            // signin existing user
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(
                                        @NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(login.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // sign-in failed
                                        Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }
    }
}