package fr.furtuff.somwhere.prefecCheck.broadcast;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.zip.GZIPInputStream;

import fr.furtuff.somwhere.prefecCheck.MainActivity;
import fr.furtuff.somwhere.prefecCheck.R;
import fr.furtuff.somwhere.prefecCheck.Service.Prefecture;
import fr.furtuff.somwhere.prefecCheck.Service.Rest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tuffery on 27/04/17.
 */
//guichet 1 : 14500
//guichet 2 : 14510
//guichet 3 : 14520
//guichet 4 : 16456
//guichet 5 : 17481

public class AlarmReceiver extends BroadcastReceiver {
    public static final int FM_ALARM_REQUEST_CODE = 0;
    public static final long FM_ALARM_INTERVAL = AlarmManager.INTERVAL_HOUR; //300000L;
    final static String STOP = "STOP";
    private final static List<Integer> guichets = Arrays.asList(14500, 14510, 14520, 16456, 17481);
    private static final String nextButton = "Etape+suivante";
    private String stringFormat = "DD-HH-MM";
    static MediaPlayer sound;
    static AlertDialog alertDialog;
    private static boolean isPlanningSucces = false;
    private static AlarmManager alarmMgr;
    private static PendingIntent pendingIntent;
    Vibrator v;
    Button button;
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences(MainActivity.sharedArea, Context.MODE_PRIVATE);
        for (int i = 0; i < guichets.size(); i++) {
            sendCall(context, guichets.get(i), i + 1);
        }
        if (!isPlanningSucces) {
            setupAlarm(context);
        } else {
            if (alarmMgr != null && pendingIntent != null) {
                alarmMgr.cancel(pendingIntent);
            }
        }

        if (intent.getBooleanExtra(STOP, false)) {
            if (sound != null) {
                sound.pause();
                sound.stop();
            }
        }
    }

    public void showNotification(Context context, int guichetNumber) {
        NotificationCompat.Builder secondChanceNotif = new NotificationCompat.Builder(context, "3616 HULA").setSmallIcon(android.R.mipmap.sym_def_app_icon);
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_custom);

        contentView.setTextViewText(R.id.guichet, "Le guichet :" + guichetNumber + " est libre prends vite rendez-vous");
        if (sound == null) {
            sound = MediaPlayer.create(context, R.raw.ambiance);
        }
        sound.setLooping(true);
        if (!sound.isPlaying()) {
            sound.start();
        }
        AudioManager audioManager =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                80, AudioManager.FLAG_PLAY_SOUND);

        secondChanceNotif.setContent(contentView);
        secondChanceNotif.setContentText("rendez-vous prefecture");
        secondChanceNotif.setPriority(NotificationCompat.PRIORITY_HIGH);
        secondChanceNotif.setSmallIcon(R.mipmap.ic_launcher);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Prefecture rdv";
            String descriptionText = "Rendez-vous";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("3616 HULA", name, importance);
            channel.setDescription(descriptionText);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }


        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(STOP, true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 111, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.secondChance, pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(16465, secondChanceNotif.build());

    }

    private void wakeUp(Context context) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "toto :TAG");
        wakeLock.acquire();

    }

    private void sendCall(final Context context, final Integer guichetId, final int guichetNumber) {
        Prefecture prefecture = Rest.getInstance().getPrefecture();
        prefecture.loadPlanning(nextButton, guichetId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                //Decode the response text/html (gzip encoded)
                boolean isSuccess = false;
                if (response.code() == 200) {
                    try {
                        ByteArrayInputStream bais = new ByteArrayInputStream(((ResponseBody) response.body()).bytes());
                        GZIPInputStream gzis = new GZIPInputStream(bais);
                        InputStreamReader reader = new InputStreamReader(gzis);
                        BufferedReader in = new BufferedReader(reader);
                        String readed;
                        String document = "";
                        while ((readed = in.readLine()) != null) {

                            document += readed;
                        }
                        if (!document.contains("Il n'existe plus de plage horaire libre pour votre demande de rendez-vous. Veuillez recommencer ultérieurement.")) { //Il n'existe plus de plage horaire libre pour votre demande de rendez-vous
                            //showPopup(context);
                            showNotification(context, guichetNumber);
                            isPlanningSucces = true;
                        }
                    } catch (Exception e) {

                    }

                    sharedPreferences.edit().putString(new SimpleDateFormat(stringFormat).format(Calendar.getInstance().getTime()) + " guichet " + guichetNumber, String.valueOf(isSuccess)).commit();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void showPopup(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.pop_up_window, null, false);
        button = (Button) inflatedView.findViewById(R.id.button);
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("rendez-vous");
        alertDialog.setMessage("rendez-vous prefecture");
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setView(inflatedView);
        //alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        alertDialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sound.stop();
                sound = null;
                alertDialog.dismiss();
            }
        });
    }

    private void setupAlarm(Context context) {
        if (alarmMgr == null) {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
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
