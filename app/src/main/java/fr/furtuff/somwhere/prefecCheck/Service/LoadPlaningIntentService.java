package fr.furtuff.somwhere.prefecCheck.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;

import fr.furtuff.somwhere.prefecCheck.MainActivity;
import fr.furtuff.somwhere.prefecCheck.broadcast.AlarmReceiver;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class LoadPlaningIntentService extends IntentService {

    public static final String guichetId = "guichetID";

    public static final String guichetNumber = "guichetNumber";

    private SharedPreferences sharedPreferences;

    public LoadPlaningIntentService() {
        super("LoadPlaningIntentService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sharedPreferences = getSharedPreferences(MainActivity.sharedArea, Context.MODE_PRIVATE);

        int guichetID = intent.getIntExtra(guichetId, 0);
        int guichetNum = intent.getIntExtra(guichetNumber, 0);


        Call<ResponseBody> request = Rest.getInstance().getPrefecture().loadPlanning(AlarmReceiver.nextButton, guichetID);
        Response<ResponseBody> response = null;
        try {
            response = request.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null && response.code() == 200) {
            ResponseBody body = response.body();
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
                    AlarmReceiver.showNotification(this, guichetNum);
                    AlarmReceiver.isPlanningSucces = true;
                }
            } catch (Exception e) {

            }

            sharedPreferences.edit().putString(new SimpleDateFormat(AlarmReceiver.stringFormat).format(Calendar.getInstance().getTime()) + " guichet " + guichetNum, String.valueOf(AlarmReceiver.isPlanningSucces)).commit();

        }
    }
}
