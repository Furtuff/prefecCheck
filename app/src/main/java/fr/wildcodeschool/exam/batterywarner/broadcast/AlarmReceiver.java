package fr.wildcodeschool.exam.batterywarner.broadcast;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;

import fr.wildcodeschool.exam.batterywarner.Model.BatteryChecker;
import fr.wildcodeschool.exam.batterywarner.R;
import fr.wildcodeschool.exam.batterywarner.Utils;

/**
 * Created by tuffery on 27/04/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final int FM_ALARM_REQUEST_CODE = 111;

    public static final long FM_ALARM_INTERVAL = 1000L;//300000L;
    final static String STOP = "STOP";
    static MediaPlayer sound;
    AlertDialog alertDialog;
    Vibrator v;
    Button button;

    @Override
    public void onReceive(Context context, Intent intent) {

        Utils.isCharging = Utils.getLastPercent(context) < BatteryChecker.instance.checkBatteryLevel(context);

        if (Utils.isCharging) {
            if (BatteryChecker.instance.checkBatteryLevel(context) > Utils.MAX_BATTERY_CHARGE) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View inflatedView = layoutInflater.inflate(R.layout.pop_up_window, null, false);
                sound = MediaPlayer.create(context, R.raw.beep);
                sound.setLooping(true);
                sound.start();
                AudioManager audioManager =
                        (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        100, AudioManager.FLAG_PLAY_SOUND);
                v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                long[] pattern = {0, 100, 1000};
                v.vibrate(pattern, 0);

                button = (Button) inflatedView.findViewById(R.id.button);
                alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setView(inflatedView);
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alertDialog.show();
                Utils.setLastpercent(0f);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sound.stop();
                        sound = null;
                        alertDialog.dismiss();
                    }
                });
                showNotification(context);

            }

        }
        if (intent.getBooleanExtra(STOP, false)) {
            if (sound != null) {
                sound.stop();
                Utils.setLastpercent(0f);
            }
        }
    }

    public void showNotification(Context context) {
        NotificationCompat.Builder secondChanceNotif = new NotificationCompat.Builder(context).setSmallIcon(android.R.mipmap.sym_def_app_icon);
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_custom);

        secondChanceNotif.setContent(contentView);
        secondChanceNotif.setContentText("deuxième chance");
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(STOP, true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 111, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.secondChance, pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(16465, secondChanceNotif.build());

    }
}
