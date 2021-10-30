package com.presidev.maos.catatanku.helper;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.presidev.maos.R;
import com.presidev.maos.catatanku.CatatanKuActivity;

public class ReminderHelper {
    private static final String TAG = ReminderHelper.class.getSimpleName();

    public static void cancelReminder(Context context, int notifId){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TargetReminder.class); // Ini SALAH
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notifId, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) alarmManager.cancel(pendingIntent);
        Log.d(TAG, "Reminder canceled: " + notifId);
    }

    static void showNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "channel_reminder";
        String CHANNEL_NAME = "Reminder";

        // Intent saat notif diklik
        Intent intent = new Intent(context, CatatanKuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_logo_notif)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                //.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            //channel.enableVibration(true);
            //channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();
        if (notificationManager != null) {
            notificationManager.notify(notifId, notification);
        }
    }
}
