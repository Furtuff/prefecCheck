package fr.wildcodeschool.exam.batterywarner.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import fr.wildcodeschool.exam.batterywarner.broadcast.AlarmReceiver;

/**
 * Created by tuffery on 27/04/17.
 */

public class BackgroundService extends Service {
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;

    public static Intent buildBackgroundServiceIntent(Context context) {
        return new Intent(context, BackgroundService.class);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        setupAlarm();

    }

    @Override
    public void onDestroy() {
        if (alarmMgr != null && pendingIntent != null) {
            alarmMgr.cancel(pendingIntent);
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("BackgroundService", "Received start id " + startId + ": " + intent);

        return START_STICKY;
    }

    private void setupAlarm() {
        if (alarmMgr == null) {
            alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        if (pendingIntent == null) {
            Intent alarmReceiver = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.FM_ALARM_REQUEST_CODE, alarmReceiver, PendingIntent.FLAG_CANCEL_CURRENT);
        } else {
            alarmMgr.cancel(pendingIntent);
        }
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, AlarmReceiver.FM_ALARM_INTERVAL, pendingIntent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
