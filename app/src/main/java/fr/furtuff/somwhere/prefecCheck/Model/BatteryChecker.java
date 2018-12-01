package fr.furtuff.somwhere.prefecCheck.Model;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by tuffery on 27/04/17.
 */

public enum BatteryChecker {

    instance;
    private IntentFilter ifilter;
    private Intent batteryStatus;


    private void init(Context context) {

        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);


        batteryStatus = context.registerReceiver(null, ifilter);


    }

    public float checkBatteryLevel(Context context) {

        init(context);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return level / (float) scale;

    }


}
