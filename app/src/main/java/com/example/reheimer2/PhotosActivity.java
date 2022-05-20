package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PhotosActivity extends AppCompatActivity implements PhotoAdapter.OnItemClickListener {

    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private ArrayList<Photo>list;
    private PhotoAdapter adapter;
    private DatabaseReference mReference;
    private FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseStorage mStorage;
    private ValueEventListener mDBListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        recyclerView = findViewById(R.id.recyclerView_photos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance();
        mAuth= FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
        mReference = database.getReference("Photos").child(mAuth.getCurrentUser().getUid());
        list = new ArrayList<>();
        adapter = new PhotoAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(PhotosActivity.this);

        // take photos to show users
        mDBListener=mReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Photo photo = dataSnapshot.getValue(Photo.class);
                    photo.setKey(dataSnapshot.getKey());
                    list.add(photo);
                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bottomNavigationView=findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_photos);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_photos:

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
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    // navigate user for adding photos
    public void navigateAddPhoto(View view) {

        Intent intent = new Intent(this, UploadPhotoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        //Toast.makeText(this, "Normal click"+position, Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onWhatEverClick(int position) {
        Intent intent = new Intent(PhotosActivity.this, UploadPhotoActivity.class);
        final String currentDescription = list.get(position).imageDescription;
        final String currentImage = list.get(position).imageUrl;
        intent.putExtra("currentDesc",currentDescription);
        intent.putExtra("currentPhoto",currentImage);
        startActivity(intent);


    }

    @Override
    public void onDeleteClick(int position) {

        Photo selectedItem = list.get(position);
        String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.imageUrl);
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mReference.child(selectedKey).removeValue();
                Toast.makeText(PhotosActivity.this,"Image Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mDBListener);
    }
}