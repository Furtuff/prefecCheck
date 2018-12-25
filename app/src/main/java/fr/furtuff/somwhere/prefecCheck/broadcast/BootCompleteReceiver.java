package fr.furtuff.somwhere.prefecCheck.broadcast;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.furtuff.somwhere.prefecCheck.Utils;

/**
 * Created by tuffery on 27/04/17.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Utils.setupAlarm(context, alarmMgr);
    }
}
