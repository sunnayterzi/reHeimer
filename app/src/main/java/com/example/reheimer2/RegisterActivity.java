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
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView navigateLogin,registerButton;
    private EditText nameRegister, surnameRegister, emailRegister, passwordRegister, configPasswordRegister, phoneRegister,AddressRegister;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registerButton=(Button) findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(this);

        navigateLogin=(TextView) findViewById(R.id.textView_navigateLogin);
        navigateLogin.setOnClickListener(this);

        nameRegister=(EditText) findViewById(R.id.editText_Name);
        surnameRegister=(EditText) findViewById(R.id.editTextText_Surname);
        emailRegister=(EditText) findViewById(R.id.editTextText_Email);
        passwordRegister=(EditText) findViewById(R.id.editTextTextPassword);
        configPasswordRegister=(EditText) findViewById(R.id.editTextTextPasswordRetype);
        phoneRegister=(EditText) findViewById(R.id.editText_Relativesphone);
        AddressRegister=(EditText) findViewById(R.id.editText_AdressLogin);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.textView_navigateLogin:
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.buttonRegister:
                registerUser();
                break;
        }

    }

    // take user's register information
    private void registerUser() {
        String email= emailRegister.getText().toString().trim();
        String name= nameRegister.getText().toString().trim();
        String surname= surnameRegister.getText().toString().trim();
        String password= passwordRegister.getText().toString().trim();
        String confirmPassword= configPasswordRegister.getText().toString().trim();
        String adress=AddressRegister.getText().toString().trim();
        String phone=phoneRegister.getText().toString().trim();



        if(name.isEmpty()){
            nameRegister.setError("Name is required");
            nameRegister.requestFocus();
            return;
        }

        if(surname.isEmpty()){
            surnameRegister.setError("Surname is required");
            surnameRegister.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emailRegister.setError("Email is required");
            emailRegister.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailRegister.setError("This is not valid email");
            emailRegister.requestFocus();
            return;
        }

        if(adress.isEmpty()){
            AddressRegister.setError("Address is required");
            AddressRegister.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            phoneRegister.setError("Phone is required");
            phoneRegister.requestFocus();
            return;
        }




        if(password.isEmpty()){
            passwordRegister.setError("Password is required");
            passwordRegister.requestFocus();
            return;
        }



        if(password.length()<6){
            passwordRegister.setError("Minimum password length should be 6 characters");
            passwordRegister.requestFocus();
            return;

        }

        if(!password.equals(confirmPassword)){
            configPasswordRegister.setError("Password is not the same as confirm password");
            configPasswordRegister.requestFocus();
            return;
        }
        Intent mainIntent1 = new Intent(this, ProfileActivity.class);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user =new User(name,surname,email,adress,phone);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"User has been registered successfully",Toast.LENGTH_LONG).show();
                                        startActivity(mainIntent1);
                                    }

                                    else{
                                        Toast.makeText(RegisterActivity.this,"Failed to register! Try again", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }

                        else{
                            Toast.makeText(RegisterActivity.this,"Failed to register! Try again", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}