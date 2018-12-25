package fr.furtuff.somwhere.prefecCheck;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import fr.furtuff.somwhere.prefecCheck.Model.BatteryChecker;
import fr.furtuff.somwhere.prefecCheck.broadcast.AlarmReceiver;

/**
 * Created by tuffery on 27/04/17.
 */

public class Utils {
    public static final float MAX_BATTERY_CHARGE = 0.85f;
    public static boolean isCharging = false;
    private static float lastpercent;
    private static AlarmManager alarmMgr;
    private static PendingIntent pendingIntent;


    public static float getLastPercent(Context context) {
        if (lastpercent == 0) {
            lastpercent = BatteryChecker.instance.checkBatteryLevel(context);
        }
        Log.d("Percent", String.valueOf(lastpercent));
        return lastpercent;
    }

    public static void setLastpercent(float newLastpercent) {
        lastpercent = newLastpercent;
    }

    public static void setupAlarm(Context context, AlarmManager alarmMgr) {

        if (pendingIntent == null) {
            Intent alarmReceiver = new Intent(context, AlarmReceiver.class);
            alarmReceiver.setAction("ça s'en va et ça revient");
            pendingIntent = PendingIntent.getBroadcast(context, AlarmReceiver.FM_ALARM_REQUEST_CODE, alarmReceiver, PendingIntent.FLAG_CANCEL_CURRENT);
        } else {
            alarmMgr.cancel(pendingIntent);
        }
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + AlarmReceiver.FM_ALARM_INTERVAL, pendingIntent);
    }

}
