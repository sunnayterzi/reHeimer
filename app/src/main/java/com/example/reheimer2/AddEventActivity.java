package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.reheimer2.databinding.ActivityAddEventBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class AddEventActivity extends AppCompatActivity {
    /*
    * AddEventActivity takes date, hour and event name, creates a single event object and
    * adds that object to firebase database under the current user's id
    */

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;
    private Button cancelEventBtn;
    ActivityAddEventBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* Getting firebase instances because we will save the events to firebase */
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        /* Database reference to "Events" because that is the place where the events will be saved */
        mDBRef = mDatabase.getReference("Events");

        /* Database reference to "Events" because that is the place where the events will be saved */
        mDBRef = mDatabase.getReference("Events");

        /* Intent to go back to Reminder Activity */
        Intent toReminderActivity = new Intent(this,ReminderActivity.class);

        createNotificationChannel();

        /* addEvent button creates a single event object by taking the entered information from the
         *  user and adds that to "Events" under the user's id. Finally redirects user to Reminder
         *  Activity
         */
        binding.addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleNotification();

                FirebaseUser currentUser = mAuth.getCurrentUser();
                double eventId = Math.random() * 1000;
                int m = binding.datePicker.getMonth()+1;
                String selectedDate = binding.datePicker.getDayOfMonth() + "/" + m + "/"
                        + binding.datePicker.getYear();

                String selectedTime = binding.timePicker.getHour() + ":" + binding.timePicker.getMinute();

                SingleEvent singleEvent = new SingleEvent(eventId,selectedDate
                        ,binding.eventDetails.getText().toString(),selectedTime);

                String id = mDBRef.push().getKey();

                mDBRef.child(currentUser.getUid()).child(id).setValue(singleEvent);

                startActivity(toReminderActivity);

            }
        });


        /* Button to cancel the process*/
        cancelEventBtn = (Button) findViewById(R.id.cancelEventBtn);

        /* Cancel button cancels the process and sends the user to Reminder Activity */
        cancelEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toReminderActivity);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String name = "notification channel";
        String desc = "notification channel description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel= new NotificationChannel("channel1", name, importance);
        channel.setDescription(desc);

        NotificationManager notificationManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void scheduleNotification() {
        Intent intent = new Intent(getApplicationContext(),Notification.class);
        String message = binding.eventDetails.getText().toString();
        intent.putExtra("messageExtra",message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long time = getTime();
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time,pendingIntent);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private long getTime() {
        int minute = binding.timePicker.getMinute();
        int hour = binding.timePicker.getHour();
        int day = binding.datePicker.getDayOfMonth();
        int month = binding.datePicker.getMonth();
        int year = binding.datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.HOUR,hour);

        //calendar.set(year,month,day,hour,minute);
        return calendar.getTimeInMillis();
    }


}