package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.KeyStore;

public class MainActivity extends AppCompatActivity {

    private Button logout, update;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase database;
    private EditText nameEdit, surnameEdit, addressEdit, phoneEdit;
    BottomNavigationView bottomNavigationView;

    User user=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("Users");

        bottomNavigationView=findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_photos:
                        startActivity(new Intent(getApplicationContext(),PhotosActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_reminder:
                        startActivity(new Intent(getApplicationContext(),ReminderActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_location:
                        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_game:
                        startActivity(new Intent(getApplicationContext(),GamesActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_profile:
                        return true;
                }
                return false;
            }
        });


        update=(Button) findViewById(R.id.ProfileUpdate_button);

        logout=(Button) findViewById(R.id.button_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mReference=FirebaseDatabase.getInstance().getReference();
        mReference.child("Users").child(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            User user =task.getResult().getValue(User.class);

                            nameEdit=(EditText) findViewById(R.id.name_profile);
                            surnameEdit=(EditText) findViewById(R.id.surname_profile);
                            addressEdit=(EditText) findViewById(R.id.address_profile);
                            phoneEdit=(EditText) findViewById(R.id.phone_profile);

                            nameEdit.setText(user.name);
                            surnameEdit.setText(user.surname);
                            addressEdit.setText(user.address);
                            phoneEdit.setText(user.phone);
                        }
                        else{
                            System.err.println(task.getException().getMessage());

                        }
                    }
                });
    }

    public void update(View view) {
        reference.child(mAuth.getCurrentUser().getUid()).child("address").setValue(addressEdit.getText().toString());
        user.address = addressEdit.getText().toString();
        reference.child(mAuth.getCurrentUser().getUid()).child("phone").setValue(phoneEdit.getText().toString());
        user.phone = phoneEdit.getText().toString();
        reference.child(mAuth.getCurrentUser().getUid()).child("surname").setValue(surnameEdit.getText().toString());
        user.surname = surnameEdit.getText().toString();
        reference.child(mAuth.getCurrentUser().getUid()).child("name").setValue(nameEdit.getText().toString());
        user.name = nameEdit.getText().toString();
    }
}