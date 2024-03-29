package com.lucanicoletti.ComposeMapsTutorial

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucanicoletti.ComposeMapsTutorial.ui.theme.ComposeMapsTutorialTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMapsTutorialTheme {
                val locationLondon = LatLng(/* latitude = */ 51.5072,/* longitude = */ -0.1276)
                val cameraPositionState = rememberCameraPositionState {
                    this.position = CameraPosition.fromLatLngZoom(locationLondon, 15f)
                }
                val locationPermissions = rememberMultiplePermissionsState(
                    listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
                )

                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(
                    LocalContext.current
                )


                val myLocationSource = object : LocationSource {
                    var listener: LocationSource.OnLocationChangedListener? = null

                    override fun activate(p0: LocationSource.OnLocationChangedListener) {
                        this.listener = p0
                    }

                    override fun deactivate() {
                        this.listener = null
                    }

                    fun onLocation(userLocation: Location) {
                        listener?.onLocationChanged(userLocation)
                    }
                }

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        for (location in locationResult.locations) {
                            myLocationSource.onLocation(location)
                        }
                    }
                }


                fun startListeningToLocations() {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    fusedLocationClient.requestLocationUpdates(
                        LocationRequest.Builder(
                            100L
                        ).build(),
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                if (locationPermissions.allPermissionsGranted) {
                                    startListeningToLocations()
                                } else {
                                    locationPermissions.launchMultiplePermissionRequest()
                                }
                            },
                            shape = CircleShape,
                            containerColor = Color.White,
                            contentColor = if (locationPermissions.allPermissionsGranted) {
                                Color(0xFF1C73E8)
                            } else {
                                Color.Gray
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.locator),
                                contentDescription = "myLocation"
                            )
                        }
                    },
                ) {

                    val mapProperties = MapProperties(
                        isBuildingEnabled = false,
                        isIndoorEnabled = false,
                        isMyLocationEnabled = locationPermissions.allPermissionsGranted,
                        isTrafficEnabled = false,
                        mapStyleOptions = null,
                        mapType = MapType.TERRAIN,
                        maxZoomPreference = 21f,
                        minZoomPreference = 3f,
                    )

                    val mapUiSettings = MapUiSettings(
                        compassEnabled = true,
                        indoorLevelPickerEnabled = false,
                        mapToolbarEnabled = true,
                        myLocationButtonEnabled = false,
                        rotationGesturesEnabled = true,
                        scrollGesturesEnabled = true,
                        scrollGesturesEnabledDuringRotateOrZoom = true,
                        tiltGesturesEnabled = true,
                        zoomControlsEnabled = false,
                        zoomGesturesEnabled = true,
                    )
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = it),
                        cameraPositionState = cameraPositionState,
                        properties = mapProperties,
                        uiSettings = mapUiSettings,
                        locationSource = myLocationSource
                    )
                }
            }
        }
    }
}
