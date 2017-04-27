package fr.wildcodeschool.exam.batterywarner.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by tuffery on 27/04/17.
 */

public class BackgroundService extends Service {

    public static Intent buildBackgroundServiceIntent(Context context) {
        return new Intent(context, BackgroundService.class);
    }


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("BackgroundService", "Received start id " + startId + ": " + intent);

        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
