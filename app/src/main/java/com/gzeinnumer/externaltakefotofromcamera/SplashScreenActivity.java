package com.gzeinnumer.externaltakefotofromcamera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.gzeinnumer.externaltakefotofromcamera.helper.FunctionGlobalDir;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashScreenActivity_";

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    TextView tv;
    String msg="externaltakefotofromcamera\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setTitle(TAG);

        tv = findViewById(R.id.tv);

        if (checkPermissions()) {
            msg+="Izin diberikan\n";
            tv.setText(msg);
            onSuccessCheckPermitions();
        } else {
            msg+="Beri izin dulu\n";
            tv.setText(msg);
        }
    }

    private void onSuccessCheckPermitions() {
        if (FunctionGlobalDir.initFolder()){
            if (FunctionGlobalDir.isFileExists(FunctionGlobalDir.appFolder)){
                msg+="Sudah bisa lanjut\n";
                tv.setText(msg);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                msg+="Direktory tidak ditemukan\n";
                tv.setText(msg);
            }
        } else {
            msg+="Gagal membuat folder\n";
            tv.setText(msg);
        }
    }

    int MULTIPLE_PERMISSIONS = 1;
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onSuccessCheckPermitions();
            } else {
                StringBuilder perStr = new StringBuilder();
                for (String per : permissions) {
                    perStr.append("\n").append(per);
                }
            }
        }
    }
}