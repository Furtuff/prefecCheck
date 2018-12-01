package fr.furtuff.somwhere.prefecCheck.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import fr.furtuff.somwhere.prefecCheck.R;

public class PermissionRequest extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 12354;

    public static Intent buildIntentKeyguard(Context context) {
        Intent intent = new Intent(context, PermissionRequest.class);
        intent.putExtra("KEYGUARD", true);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);
        if (getIntent() != null) {
            requestPermissions(new String[]{Manifest.permission.DISABLE_KEYGUARD, Manifest.permission.INTERNET}, REQUEST_CODE_ASK_PERMISSIONS);
        } else
            requestPermissions(new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.INTERNET}, REQUEST_CODE_ASK_PERMISSIONS);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        finish();
    }
}
