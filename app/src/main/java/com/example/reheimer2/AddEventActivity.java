package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddEventActivity extends AppCompatActivity {
    /*
    * AddEventActivity takes date, hour and event name, creates a single event object and
    * adds that object to firebase database under the current user's id
    */

    private CalendarView calendarView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String selectedDate;
    private String selectedTime;
    private DatabaseReference mDBRef;
    private TimePicker picker;
    private Button addEventBtn;
    private TextView eventContext;
    private Button cancelEventBtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        /* Widget to pick hour and minute*/
        picker=(TimePicker)findViewById(R.id.datePicker1);
        picker.setIs24HourView(true);

        /* Calendar view to pick date from a calendar*/
        calendarView = (CalendarView) findViewById(R.id.calendarView);

        eventContext = (TextView) findViewById(R.id.eventNameInput);

        /* Buttons to create an event or cancel the process*/
        addEventBtn = (Button) findViewById(R.id.addEventBtn);
        cancelEventBtn = (Button) findViewById(R.id.cancelEventBtn);

        /* When there is change on the calendar view, selected date saved to selectedDate */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                selectedDate = i2+ "/" + (i1 + 1) + "/" + i;
            }
        });

        /* When there is change on the time widget, selected time saved to selectedTime */
        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                selectedTime = i + ":" + i1;
            }
        });

        /* Getting firebase instances because we will save the events to firebase */
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        /* Intent to go back to Reminder Activity */
        Intent toReminderActivity = new Intent(this,ReminderActivity.class);

        /* Database reference to "Events" because that is the place where the events will be saved */
        mDBRef = mDatabase.getReference("Events");

        /* addEvent button creates a single event object by taking the entered information from the
        *  user and adds that to "Events" under the user's id. Finally redirects user to Reminder
        *  Activity
        */
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                double eventId = Math.random() * 1000;
                SingleEvent singleEvent = new SingleEvent(eventId,selectedDate,eventContext.getText().toString(),selectedTime);

                String id = mDBRef.push().getKey();

                mDBRef.child(currentUser.getUid()).child(id).setValue(singleEvent);

                startActivity(toReminderActivity);
            }
        });

        /* Cancel button cancels the process and sends the user to Reminder Activity */
        cancelEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toReminderActivity);
            }
        });
    }


}