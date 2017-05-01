package fr.wildcodeschool.exam.batterywarner.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import fr.wildcodeschool.exam.batterywarner.R;

public class PermissionRequest extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

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
            requestPermissions(new String[]{Manifest.permission.DISABLE_KEYGUARD}, REQUEST_CODE_ASK_PERMISSIONS);
        } else
            requestPermissions(new String[]{android.Manifest.permission.RECEIVE_BOOT_COMPLETED}, REQUEST_CODE_ASK_PERMISSIONS);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        finish();
    }
}
