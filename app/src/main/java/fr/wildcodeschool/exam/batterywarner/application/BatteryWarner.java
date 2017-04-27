package fr.wildcodeschool.exam.batterywarner.application;

import android.app.Application;

import fr.wildcodeschool.exam.batterywarner.Service.BackgroundService;

/**
 * Created by tuffery on 27/04/17.
 */

public class BatteryWarner extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(BackgroundService.buildBackgroundServiceIntent(this));
    }


}
