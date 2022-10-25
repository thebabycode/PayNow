package com.example.paynow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannView ;
    TextView resultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannView = findViewById(R.id.scannerview);
        codeScanner = new CodeScanner(this, scannView);
        resultData = findViewById(R.id.tv_textview);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        resultData.setText(result.getText());
                    }
                });
            }
        });
        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    private void requestForCamera() {
       Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
           @Override
           public void onPermissionGranted(PermissionGrantedResponse response) {
               codeScanner.startPreview();
           }

           @Override
           public void onPermissionDenied(PermissionDeniedResponse response) {
               Toast.makeText(MainActivity.this,"Camera Permission is required.", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
               token.continuePermissionRequest();
           }
       }).check();
    }
}