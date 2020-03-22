package com.example.buber.Views.Components;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.buber.R;

import static com.example.buber.App.DRIVER_CHANNEL_ID;
import static com.example.buber.App.RIDER_CHANNEL_ID;

public class BUberNotificationManager {
    private NotificationManagerCompat notificationManager;
    private Activity referenceActivity;
    private Class referenceClass;

    public BUberNotificationManager(Activity activity, Class referenceClass) {
        notificationManager = NotificationManagerCompat.from(activity);
        this.referenceActivity = activity;
        this.referenceClass = referenceClass;
    }

    public void notifyOnDriverChannel(int id, String title, String content, int color) {

        Intent activityIntent = new Intent(this.referenceActivity, this.referenceClass);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this.referenceActivity,
                0,
                activityIntent,
                0);

        Notification driverNotification = new NotificationCompat.Builder(this.referenceActivity, DRIVER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_pickup_rider_24dp)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(color)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(id, driverNotification);
    }

    public void notifyOnRiderChannel(int id, String title, String content, int color) {
        Intent activityIntent = new Intent(this.referenceActivity, this.referenceClass);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this.referenceActivity,
                0,
                activityIntent,
                0);

        Notification riderNotification = new NotificationCompat.Builder(this.referenceActivity, RIDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_driver_offering_24dp)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(color)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(id, riderNotification);
    }

    public void notifyOnAllChannels(int id, String title, String content, int color) {
        notifyOnRiderChannel(id, title, content, color);
        notifyOnDriverChannel(id, title, content, color);
    }
}
