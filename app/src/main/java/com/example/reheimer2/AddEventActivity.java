package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class AddEventActivity extends AppCompatActivity {
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

        picker=(TimePicker)findViewById(R.id.datePicker1);
        picker.setIs24HourView(true);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        addEventBtn = (Button) findViewById(R.id.addEventBtn);
        eventContext = (TextView) findViewById(R.id.eventNameInput);
        cancelEventBtn = (Button) findViewById(R.id.cancelEventBtn);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                selectedDate = i2+ "/" + (i1 + 1) + "/" + i;
            }
        });

        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                selectedTime = i + ":" + i1;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        Intent toReminderActivity = new Intent(this,ReminderActivity.class);
        mDBRef = mDatabase.getReference("Events");


        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                SingleEvent singleEvent = new SingleEvent(selectedDate,eventContext.getText().toString(),selectedTime);

                String id = mDBRef.push().getKey();

                mDBRef.child(currentUser.getUid()).child(id).setValue(singleEvent);

                startActivity(toReminderActivity);
            }
        });

        cancelEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toReminderActivity);
            }
        });
    }


}
/*
*
*
*  String dateC = "19/05/2022";
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date1 = LocalDate.parse(dateC, formatter1);

        LocalTime dateT = LocalTime.of(17, 40);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
        formatter2.format(dateT);

        Log.d("converting 1",date1.toString());
        Log.d("converting 2",formatter2.format(dateT));
*
*
* */