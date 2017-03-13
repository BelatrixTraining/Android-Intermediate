package com.belatrixsf.bobbyproject;

import android.Manifest;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.belatrixsf.bobbyproject.commons.AndroidMPermission;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * @author Carlos Piñan
 * @date 3/13/17
 */

public class RunTimeUsingLibExample extends AppCompatActivity {

    private static final int PERMISSION_CODE = 7777;
    private static final int GPS_SETTINGS = 7778;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private PendingResult<LocationSettingsResult> pendingResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(@Nullable Bundle bundle) {
                                try {
                                    Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                            googleApiClient);
                                    if (mLastLocation != null) {
                                    }
                                } catch (SecurityException e) {

                                }
                            }

                            @Override
                            public void onConnectionSuspended(int i) {

                            }
                        }
                )
                .addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                            }
                        }
                )
                .build();

        AndroidMPermission.get().askForPermissions(
                new AndroidMPermission.Callback() {
                    @Override
                    public void onPermissionSuccess() {
                        startGPS();
                    }

                    @Override
                    public void onPermissionDeny() {
                        // El permiso fue denegado, mostrar un error diciendo.
                        // NO DENIEGUES EL PERMISO!!!!! QUE TE PASA!!!!!
                    }

                    @Override
                    public void onShowRationale(String permissionName) {
                        // Se debe mostrar un mensaje diciendo.
                        // OE! ESTE PERMISO ES IMPORTANTE!!!!
                    }
                },
                PERMISSION_CODE,
                RunTimeUsingLibExample.this,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        );
    }

    private void startGPS() {
        // Aqui comienza la magia del GPS
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(RunTimeUsingLibExample.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AndroidMPermission.get().permissionSuccess(requestCode, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
