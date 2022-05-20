package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ReminderActivity extends AppCompatActivity {
    /*
    * This activity shows current events that user has entered.*/

    /* To list certain user's events, firebase needed. */
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;
    BottomNavigationView bottomNavigationView;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        calendarView = (CalendarView) findViewById(R.id.calendarV);
        Button addEventBtn = (Button) findViewById(R.id.calendar_button);

        /* When clicked on addEventBtn redirected to AddEventActivity */
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReminderActivity.this,AddEventActivity.class));
            }
        });

        /* Events to show on the screen will be hold in this list.*/
        ArrayList<SingleEvent> eventList = new ArrayList<>();

        /* List above will be put in a Recycler View */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler1);

        /* Adapter works as a bridge between eventList and recycler view */
        EventAdapter eventAdapter = new EventAdapter(eventList);



        /* To get user's events we first get FirebaseAuth and FirebaseDatabase instances.
           After that we get reference to the events under the user's uid. */
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference("Events/" + mAuth.getCurrentUser().getUid());
        mDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    /*
                    * Every event has a date, hour and name. Data taken from the database
                    * turns into a singleEvent object which has these attributes. And then
                    * singleEvent object is added to eventList array list.
                    * */
                    SingleEvent sEvent = dataSnapshot.getValue(SingleEvent.class);
                    eventList.add(sEvent);
                }
                eventAdapter.notifyDataSetChanged();

            }

            /* If fetching data from database failed, then an error message is shown.*/
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReminderActivity.this,"Failed to fetch data! Try again later.", Toast.LENGTH_LONG).show();
            }
        });



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /* After fetching the data final view is set. */
        recyclerView.setAdapter(eventAdapter);

        /*      Bottom Navigation Bar         */

        bottomNavigationView=findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_reminder);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_photos:
                        startActivity(new Intent(getApplicationContext(),PhotosActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_reminder:

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
}