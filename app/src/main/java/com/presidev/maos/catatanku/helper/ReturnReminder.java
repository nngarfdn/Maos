package com.presidev.maos.catatanku.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.presidev.maos.catatanku.reminder.model.Reminder;

import java.util.Arrays;
import java.util.Calendar;

import static com.presidev.maos.catatanku.helper.ReminderHelper.showNotification;
import static com.presidev.maos.utils.DateUtils.addDay;
import static com.presidev.maos.utils.DateUtils.getArrayDate;
import static com.presidev.maos.utils.DateUtils.isValidDateFormat;

public class ReturnReminder extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_REMINDER_ID = "extra_reminder_id";
    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_MESSAGE = "extra_message";

    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderId = intent.getStringExtra(EXTRA_REMINDER_ID);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        int notifId = reminderId.hashCode();
        showNotification(context, title, message, notifId);
    }

    public void setReminder(Context context, Reminder reminder){
        Log.d(TAG, reminder.getReturnDate());
        if (!isValidDateFormat(reminder.getReturnDate())) return;

        String title = "Jangan lupa kembalikan buku";
        String message = "Besok kamu harus mengembalikan " + reminder.getBookTitle();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReturnReminder.class);
        intent.putExtra(EXTRA_REMINDER_ID, reminder.getId());
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_MESSAGE, message);

        int[] dateArray = getArrayDate(addDay(reminder.getReturnDate(), -1)); // Reminder H-1
        if (dateArray == null) return;
        Log.d(TAG, "H-1: " + Arrays.toString(dateArray));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, dateArray[0]);
        calendar.set(Calendar.MONTH, dateArray[1]-1);
        calendar.set(Calendar.DAY_OF_MONTH, dateArray[2]);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.getId().hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null){
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Log.d(TAG, "Reminder set up: " + reminder.getId());
    }
}