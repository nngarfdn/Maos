package com.presidev.maos.catatanku.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.presidev.maos.catatanku.target.model.Target;

import java.util.Calendar;

import static com.presidev.maos.catatanku.helper.ReminderHelper.showNotification;
import static com.presidev.maos.utils.DateUtils.getArrayTime;
import static com.presidev.maos.utils.DateUtils.isValidTimeFormat;

public class TargetReminder extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_TARGET_ID = "extra_target_id";
    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_MESSAGE = "extra_message";

    @Override
    public void onReceive(Context context, Intent intent) {
        String targetId = intent.getStringExtra(EXTRA_TARGET_ID);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        int notifId = targetId.hashCode();
        showNotification(context, title, message, notifId);
    }

    public void setReminder(Context context, Target target){
        for (int dayOfWeek : target.getReminderDayOfWeeks()){
            Log.d(TAG, "dayOfWeek: " + dayOfWeek);
            setWeeklyReminder(context, target, dayOfWeek);
        }
    }

    private void setWeeklyReminder(Context context, Target target, int dayOfWeek){
        Log.d(TAG, target.getReminderTime());
        if (!isValidTimeFormat(target.getReminderTime())) return;

        String title = "Ayo baca buku";
        String message = "Baca " + target.getBookTitle() + " hari ini";

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TargetReminder.class);
        intent.putExtra(EXTRA_TARGET_ID, target.getId());
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_MESSAGE, message);

        int[] timeArray = getArrayTime(target.getReminderTime());
        if (timeArray == null) return;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timeArray[0]);
        calendar.set(Calendar.MINUTE, timeArray[1]);
        calendar.set(Calendar.SECOND, 0);

        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        // Check we aren't setting it in the past which would trigger it to fire instantly
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        // Bedakan id reminder per harinya
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                target.getId().hashCode() + dayOfWeek, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 7, pendingIntent); // Seminggu sekali
        }

        Log.d(TAG, "Reminder set up: " + target.getId() +
                ": " + target.getId().hashCode() + dayOfWeek);
    }
}