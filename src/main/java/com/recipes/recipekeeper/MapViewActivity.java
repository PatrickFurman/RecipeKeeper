package com.recipes.recipekeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {
    DatabaseReference markersRef = FirebaseDatabase.getInstance().getReference().child("Groceries").child("Markers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        markersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    LatLng postLoc = new LatLng(Double.parseDouble(child.child("Latitude").getValue().toString()), Double.parseDouble(child.child("Longitude").getValue().toString()));
                    Marker marker;
                    marker = googleMap.addMarker(new MarkerOptions().position(postLoc));
                    // marker.setTag(thing to show up when clicked);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
