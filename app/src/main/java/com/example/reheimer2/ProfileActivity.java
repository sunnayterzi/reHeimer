package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private Button normLogout, normUpdate;
    private TextView normName, normSurname, normAddress, normPhone;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase database;
    BottomNavigationView bottomNavigationView;

    User user=new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // navigate user to activities
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
        normLogout = (Button) findViewById(R.id.Button_normLogout);
        normUpdate = (Button) findViewById(R.id.button_updateNormProfile);
        database=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");


        normLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });

        normUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
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
                            normName = (TextView) findViewById(R.id.textView_normName);
                            normSurname = (TextView) findViewById(R.id.textView_normSurname);
                            normAddress = (TextView) findViewById(R.id.textView_normAddress);
                            normPhone = (TextView) findViewById(R.id.textView_normPhone);



                            normName.setText(user.name);
                            normSurname.setText(user.surname);
                            normAddress.setText(user.address);
                            normPhone.setText(user.phone);
                        }
                        else{
                            System.err.println(task.getException().getMessage());

                        }
                    }
                });
    }
}