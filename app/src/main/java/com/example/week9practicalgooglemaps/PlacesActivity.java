package com.example.week9practicalgooglemaps;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.week9practicalgooglemaps.databinding.ActivityPlacesBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class PlacesActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityPlacesBinding activityPlacesBinding;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_places);
        activityPlacesBinding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(activityPlacesBinding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityResultLauncher<Intent> startAutoComplete = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK){
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        LatLng latLng = place.getLatLng();
                        if(latLng != null){
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(place.getName())
                                    .snippet(place.getAddress()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            Toast.makeText(this, "Selected: " + place.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        //****************** USE YOUR API KEY HERE: THIS API KEY WON'T WORK*********************************
        // GET API KEY FROM HERE  https://developers.google.com/maps/documentation/android-sdk/get-api-key
        // YOU HAVE TO USE YOUR OWN API KEY in TWO PLACES:
        // 1. HERE
        // 2. IN local.properties FILE
        Places.initialize(getApplicationContext(), "AIzaSyBKPb4stb_Co6br83QwLY_ybVlkk63gy3k");


        activityPlacesBinding.pickPlaceButton.setOnClickListener(v -> {
            //TODO: Open the Place Picker
//            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
            Place.Field[] fields = new Place.Field[]{Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS};
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, Arrays.asList(fields))
                    .build(this);
            startAutoComplete.launch(intent);
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-33.852, 151.211);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
    }
}