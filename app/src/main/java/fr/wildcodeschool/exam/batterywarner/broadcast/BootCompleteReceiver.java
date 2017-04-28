package fr.wildcodeschool.exam.batterywarner.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.wildcodeschool.exam.batterywarner.Service.BackgroundService;

/**
 * Created by tuffery on 27/04/17.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(BackgroundService.buildBackgroundServiceIntent(context));
    }
}
