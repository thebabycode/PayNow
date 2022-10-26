package com.example.paynow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class sendSMS extends AppCompatActivity {

    TextView qrcode;
    EditText amt;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        qrcode = findViewById(R.id.qrcode);
        amt = findViewById(R.id.amount);
        send = findViewById(R.id.paynow);

        Intent i=getIntent();
        String str=i.getStringExtra("msg");
        qrcode.setText(str);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(sendSMS.this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                }
                else{
                    ActivityCompat.requestPermissions(sendSMS.this, new String[]{Manifest.permission.SEND_SMS},
                            100);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendSMS();
        }
        else{
            Toast.makeText(this,"Permission Denied!",Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS() {
        //number to which the sms has to be send
        String phone = "9188594788";
        String amount = amt.getText().toString();
        String qr= qrcode.getText().toString();
        String msgcontent = "Amount = " + amount + "\nQR code = " + qr;

        if (!amount.isEmpty()){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,msgcontent,null,null);
            Toast.makeText(this,"SMS sent Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(sendSMS.this, MainActivity.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this,"Please Enter the Amount",Toast.LENGTH_SHORT).show();
        }
    }
}






