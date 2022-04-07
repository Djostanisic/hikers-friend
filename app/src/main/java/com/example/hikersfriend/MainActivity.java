package com.example.hikersfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import hikersfriend.R;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    public void updateLocationInfo(Location location) {

        TextView latitude = findViewById(R.id.latitude);
        TextView longitude = findViewById(R.id.longitude);
        TextView accuracy = findViewById(R.id.accuracy);
        TextView altitude = findViewById(R.id.altitude);

        latitude.setText("Latitude: " + location.getLatitude());
        longitude.setText("Longitude: " + location.getLongitude());
        accuracy.setText("Accuracy: " + location.getAccuracy());
        altitude.setText("Altitude: " + location.getAltitude());

        Geocoder geocoder = new Geocoder( (getApplicationContext()), Locale.getDefault());

        try {

            String address = "Could not find address";

            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(listAddresses != null && listAddresses.size() > 0){

                address = "Address: \n";

                if(listAddresses.get(0).getThoroughfare() != null){
                    address += listAddresses.get(0).getThoroughfare() + " ";
                }
                if(listAddresses.get(0).getSubThoroughfare() != null){
                    address += listAddresses.get(0).getSubThoroughfare() + "\n";
                }
                if(listAddresses.get(0).getLocality() != null){
                    address += listAddresses.get(0).getLocality() + "\n";
                }
                if(listAddresses.get(0).getCountryName() != null){
                    address += listAddresses.get(0).getCountryName() + "\n";
                }
            }
                TextView addressTextView = findViewById(R.id.addressTextView);
                addressTextView.setText(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   startListening();
        }
    }

    public void  startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null){

            updateLocationInfo(location);
        }
        }
    }
}