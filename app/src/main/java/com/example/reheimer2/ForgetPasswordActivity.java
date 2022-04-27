package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText emailText;
    private Button resetPassword;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailText=(EditText) findViewById(R.id.editTextResetEmail);
        resetPassword=(Button) findViewById(R.id.button_resetPassword);

        auth= FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });



    }

    private void resetPassword(){
        String email=emailText.getText().toString().trim();

        if(email.isEmpty()){
            emailText.setError("Email is required");
            emailText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailText.setError("Please provide valid email");
            emailText.requestFocus();
            return;
        }
        Intent loginIntent = new Intent(this, LoginActivity.class);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetPasswordActivity.this,"Check your email to reset password", Toast.LENGTH_LONG).show();
                    startActivity(loginIntent);
                }
                else{
                    Toast.makeText(ForgetPasswordActivity.this,"Try again! Something wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}