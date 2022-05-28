package com.example.reheimer2;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class Notification extends BroadcastReceiver {

    int notificationID = 1;
    String channelID = "channel1";
    String titleExtra = "titleExtra";
    String messageExtra = "messageExtra";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context,channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(intent.getStringExtra(titleExtra))
                .setContentText(intent.getStringExtra(messageExtra));


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        android.app.Notification n = notification.build();
        manager.notify(notificationID,n);

    }
}
