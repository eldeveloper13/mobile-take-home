package com.eldeveloper13.airportroutefinder;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.stream.Collectors;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View contentView;

    MapViewModel viewModel;
    Snackbar snackbar;
    EditText originEditText;
    EditText destinationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        contentView = findViewById(R.id.map);
        originEditText = findViewById(R.id.edt_origin);
        destinationEditText = findViewById(R.id.edt_destination);

        viewModel = ViewModelProviders.of(this).get(MapViewModel.class);

        View searchBtn = findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(v -> {
            String origin = originEditText.getText().toString();
            String destination = destinationEditText.getText().toString();
            viewModel.searchPath(origin, destination);
            hideKeyboard();
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initObservables();
    }

    private void initObservables() {
        viewModel.pathLiveData.observe(this, airports -> {
            if (airports != null && airports.size() > 0) {
                List<LatLng> markers = airports.stream().map(n -> new LatLng(n.lat, n.lng))
                        .collect(Collectors.toList());
                airports.forEach(m -> mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(m.lat, m.lng))).setTitle(String.format("%s - %s", m.city, m.IATA3)));
                mMap.addPolyline(new PolylineOptions().add(markers.toArray(new LatLng[markers.size()]))
                        .width(5)
                        .color(Color.RED));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(markers.get(0)));
            } else {
                mMap.clear();
            }
        });

        viewModel.searchErrorLiveData.observe(this, v -> {
            if (v != null) {
                showErrorMessage(v.errorMessage);
            } else if (snackbar != null) {
                snackbar.dismiss();
            }
        });
    }

    private void showErrorMessage(String errorMsg) {
        snackbar = Snackbar.make(contentView, errorMsg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.dismiss), view -> viewModel.dismissSearchError());
        snackbar.show();
    }

    private void hideKeyboard() {
        try {
            if (getCurrentFocus() == null) return;
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
