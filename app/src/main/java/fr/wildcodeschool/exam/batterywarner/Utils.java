package fr.wildcodeschool.exam.batterywarner;

import android.content.Context;

import fr.wildcodeschool.exam.batterywarner.Model.BatteryChecker;

/**
 * Created by tuffery on 27/04/17.
 */

public class Utils {
    public static final float MAX_BATTERY_CHARGE = 0.85f;
    public static boolean isCharging = false;
    private static float lastpercent;

    public static float getLastPercent(Context context) {
        if (lastpercent == 0) {
            lastpercent = BatteryChecker.instance.checkBatteryLevel(context);
        }

        return lastpercent;
    }

    public static void setLastpercent(float newLastpercent) {
        lastpercent = newLastpercent;
    }
}
