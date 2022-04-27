package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView register, forgetPassword;
    private EditText loginEmail, loginPassword;
    private Button login;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register=(TextView) findViewById(R.id.textViewCreateAccount);
        register.setOnClickListener(this);

        login=(Button) findViewById(R.id.buttonLogin);
        login.setOnClickListener(this);

        loginEmail=(EditText) findViewById(R.id.editText_LoginEmail);
        loginPassword=(EditText) findViewById(R.id.editTextTextPasswordLogin);

        forgetPassword=(TextView) findViewById(R.id.textViewForget);
        forgetPassword.setOnClickListener(this);

        mAuth=FirebaseAuth.getInstance();

    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            Intent mainIntent=new Intent(this,MainActivity.class);
            startActivity(mainIntent);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.textViewCreateAccount:
                startActivity(new Intent(this,RegisterActivity.class));
                break;

            case R.id.buttonLogin:
                userLogin();
                break;

            case R.id.textViewForget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
        }
    }

    private void userLogin() {
        String email=loginEmail.getText().toString().trim();
        String password=loginPassword.getText().toString().trim();

        if(email.isEmpty()){
            loginEmail.setError("Email is required");
            loginEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmail.setError("This is not valid email");
            loginEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            loginPassword.setError("Password is required");
            loginPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            loginPassword.setError("Minimum password length should be 6 characters");
            loginPassword.requestFocus();
            return;
        }
        Intent mainIntent = new Intent(this, MainActivity.class);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(mainIntent);;

                }

                else{
                    Toast.makeText(LoginActivity.this,"Failed to login! Check your information", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}