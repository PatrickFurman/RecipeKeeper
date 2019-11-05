package com.recipes.recipekeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

public class ViewGroceryListActivity extends AppCompatActivity {
    DatabaseReference groceriesRef = FirebaseDatabase.getInstance().getReference().child("Groceries");
    ListView list;
    int numMarkers;
    Location mCurrentLocation;
    LocationCallback mLocationCallback;
    LocationRequest locationRequest;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grocery_list);
        TextView newItemButton = new TextView(this);
        newItemButton.setText(R.string.add_grocery);
        newItemButton.setTextSize(20);
        newItemButton.setTextColor(Color.BLACK);
        newItemButton.setGravity(Gravity.CENTER_HORIZONTAL);
        newItemButton.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        list = (findViewById(R.id.list));
        list.addFooterView(newItemButton);
        //TODO add ability to set shopping point from this screen (maybe using mapview from Homecooked to pin and mark coordinates) and then check in background if near every few min and notify user
        //TODO ALTERNATIVE PLAN -- Button to set point coordinates automatically, then just mapview to see the pins and edit/name/remove from there?
        groceriesRef.child("Items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    TextView t = new TextView(getApplicationContext());
                    String s = child.getKey() +" " + child.getValue().toString();
                    t.setText(s);
                    t.setTextSize(20);
                    t.setTextColor(Color.BLACK);
                    list.addHeaderView(t);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("List gen error", databaseError.getMessage());
            }
        });
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                latitude = mCurrentLocation.getLatitude();
                longitude = mCurrentLocation.getLongitude();
            }
        };
        getLocation();
    }

    public void addMarker(View v) {
        getLocation();
        groceriesRef.child("Markers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numMarkers = (int) Math.ceil(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String s = "" + (numMarkers + 1);
        //TODO coordinates always being set as (0,0) - need to fix
        groceriesRef.child("Markers").child(s).child("Latitude").setValue(latitude);
        groceriesRef.child("Markers").child(s).child("Longitude").setValue(longitude);
        Toast.makeText(getApplicationContext(), "Marker Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(getApplicationContext(), "Not all features of this app will work without" +
                            " location services turned on", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void getLocation() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            try {
                LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, mLocationCallback, null);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), R.string.error + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void goToMapView(View v) {
        Intent i = new Intent(this, MapViewActivity.class);
        startActivity(i);
    }
}
