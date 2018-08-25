package com.nickhe.hikingfun;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView latTextView;
    TextView lngTextView;
    TextView accTextView;
    TextView altTextView;
    TextView addTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latTextView = findViewById(R.id.latTextView);
        lngTextView = findViewById(R.id.lngTextView);
        accTextView = findViewById(R.id.accTextView);
        altTextView = findViewById(R.id.altTextView);
        addTextView = findViewById(R.id.addTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,
                    0, 0, locationListener);

            Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

            if (location != null) {
                updateLocationInfo(location);
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    private void startListening() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,
                    0, 0, locationListener);

        }

    }

    private void updateLocationInfo(Location location) {

        DecimalFormat formatter = new DecimalFormat("#.##");

        String lat = formatter.format(location.getLatitude());
        String lng = formatter.format(location.getLongitude());
        String acc = formatter.format(location.getAccuracy());
        String alt = formatter.format(location.getAltitude());

        latTextView.setText("Latitude: " + lat);
        lngTextView.setText("Longitude: " + lng);
        accTextView.setText("Accuracy: " + acc);
        altTextView.setText("Altitude: " + alt);

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            String address = "Could not find your location!";

            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);

            if(addressList != null && addressList.size() > 0)
            {

                address = "";

                if(addressList.get(0).getSubThoroughfare() != null)
                {
                    address += addressList.get(0).getSubThoroughfare()+" ";
                }

                if(addressList.get(0).getThoroughfare() != null)
                {
                    address += addressList.get(0).getThoroughfare()+"\n";
                }

                if(addressList.get(0).getLocality() != null)
                {
                    address += addressList.get(0).getLocality()+"\n";
                }

                if(addressList.get(0).getPostalCode() != null)
                {
                    address += addressList.get(0).getPostalCode()+"\n";
                }

                if(addressList.get(0).getCountryName() != null)
                {
                    address += addressList.get(0).getCountryName();
                }
            }

            addTextView.setText("Location:\n"+address);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
