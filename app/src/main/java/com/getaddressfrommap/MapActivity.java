package com.getaddressfrommap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.getaddressfrommap.AddressLocation.myaddress;
import static com.getaddressfrommap.AddressLocation.mylocation;

public class MapActivity extends FragmentActivity implements LocationListener, View.OnClickListener {
    private GoogleMap mMap;
    private TextView edt_map_search;
    private TextView btn_done;
    double Latitude = 0.0, Longitude = 0.0;
    private String TAG = "MapActivity";
    private ImageView img_regHeader_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_address);

        img_regHeader_back = (ImageView) findViewById(R.id.img_regHeader_back);
        img_regHeader_back.setOnClickListener(this);
        edt_map_search = (TextView) findViewById(R.id.txt_map_search);
        edt_map_search.setMovementMethod(new ScrollingMovementMethod());
        btn_done = (TextView) findViewById(R.id.txt_map_done);
        btn_done.setOnClickListener(this);


        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 1 second
                        FragmentManager fm = getSupportFragmentManager();
                        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
                        fm.beginTransaction().replace(R.id.map, supportMapFragment).commit();
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                mMap = googleMap;

                                GenerateMapLocation();
                            }
                        });
                    }
                }, 1000);
            }
        });

    }

    private void GenerateMapLocation() {
        // Add a marker in current location and move the camera
        if (mylocation != null && !mylocation.toString().equals("")) {
            String[] loc = mylocation.split(",");
            Latitude = Double.parseDouble(loc[0].toString());
            Longitude = Double.parseDouble(loc[1].toString());
            edt_map_search.setText(myaddress + "");
//            GenerateMapLocation();
        }

        LatLng current_loc = new LatLng(Latitude, Longitude);
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions().position(current_loc).title("You are Here!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current_loc));
            getAddress(current_loc);
            // Setting a click event handler for the map
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.clear();
                    Latitude = latLng.latitude;
                    Longitude = latLng.longitude;
                    getAddress(latLng);
                }
            });
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    private void getAddress(LatLng latLng) {

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15.0f);
        mMap.animateCamera(cameraUpdate);
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> address1 = geocoder.getFromLocation(Latitude, Longitude, 3);

            if (address1 != null && address1.size() > 0) {

                Address returnedaddress = address1.get(0);
                StringBuilder strReturnedaddress = new StringBuilder();
                for (int i = 0; i < returnedaddress.getMaxAddressLineIndex(); i++) {
                    strReturnedaddress.append(returnedaddress.getAddressLine(i)).append(",");
                    returnedaddress.getLocality();
                    returnedaddress.getCountryName();
                }
                // Log.d("RESULT", strReturnedaddress.toString());
                edt_map_search.setText(strReturnedaddress.toString());
                MarkerOptions marker = new MarkerOptions().position(latLng);
                mMap.addMarker(marker.title(strReturnedaddress.toString()));

            } else {
                // Log.d("NO-RESULT", "NO-RESULT");
            }
        } catch (IOException e) { // TOD Auto-generated

            e.printStackTrace();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_regHeader_back:
                finish();
                break;
            case R.id.txt_map_done:
                myaddress = edt_map_search.getText().toString();
                mylocation = Latitude + "," + Longitude;
                finish();
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if (Latitude == 0.0 || Longitude == 0.0) {
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();

        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


}
