package fr.furtuff.somwhere.prefecCheck;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    public static final String sharedArea = "fr.furtuff.somwhere.prefecCheck.prefecCheckData";
    public SharedPreferences pref;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist);
        list = findViewById(R.id.logList);
        pref = getSharedPreferences(sharedArea, MODE_PRIVATE);

        List<String> log = new ArrayList<>();

        for (String elem : pref.getAll().keySet()) {
            log.add(elem + " : " + pref.getAll().get(elem));
        }

        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, log));
    }

}
