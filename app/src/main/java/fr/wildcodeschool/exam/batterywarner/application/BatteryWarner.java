package fr.wildcodeschool.exam.batterywarner.application;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import fr.wildcodeschool.exam.batterywarner.Service.BackgroundService;
import fr.wildcodeschool.exam.batterywarner.activity.PermissionRequest;

/**
 * Created by tuffery on 27/04/17.
 */

public class BatteryWarner extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Show alert dialog to the user saying a separate permission is needed
            // Launch the settings activity if the user prefers
            if (!Settings.canDrawOverlays(this)) {
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
            }

            if (getApplicationContext().checkSelfPermission(android.Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_DENIED) {
                startActivity(new Intent(this, PermissionRequest.class));
            }
            if (getApplicationContext().checkSelfPermission(android.Manifest.permission.DISABLE_KEYGUARD) == PackageManager.PERMISSION_DENIED) {

                startActivity(PermissionRequest.buildIntentKeyguard(this));
            }

        }
        startService(BackgroundService.buildBackgroundServiceIntent(this));
    }


}
