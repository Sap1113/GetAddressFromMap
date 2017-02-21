package com.getaddressfrommap;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddressLocation extends AppCompatActivity implements View.OnClickListener {
    public static String mylocation = "", myaddress = "";
    private TextView txt_address;
    private static final int MY_PERMISSIONS_REQUEST = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_location);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        myaddress = "Select Address from Map";
        mylocation = "";

        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_address.setOnClickListener(this);
        txt_address.setText("Select Address from Map");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_address:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(AddressLocation.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
                    } else {
                        Intent intent = new Intent(AddressLocation.this, MapActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(AddressLocation.this, MapActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().post(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(AddressLocation.this, MapActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        txt_address.setText(myaddress);
    }
}
