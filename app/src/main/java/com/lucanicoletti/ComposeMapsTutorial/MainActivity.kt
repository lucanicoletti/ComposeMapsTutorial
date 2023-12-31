package com.lucanicoletti.ComposeMapsTutorial

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {


                    val locationLondon = LatLng(
                        /* latitude = */ 51.5072,
                        /* longitude = */ -0.1276
                    )
                    val cameraPositionState = rememberCameraPositionState {
                        this.position = CameraPosition.fromLatLngZoom(
                            /* target = */ locationLondon,
                            /* zoom = */ 15f
                        )
                    }

                    val locationPermissions = rememberMultiplePermissionsState(
                        listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
                    )

                    val myLocationSource = object : LocationSource {
                        var listener: LocationSource.OnLocationChangedListener? = null
                        var lastLocation: Location? = null
                        override fun activate(p0: LocationSource.OnLocationChangedListener) {
                            this.listener = p0
                            lastLocation?.let {
                                this.listener?.onLocationChanged(it)
                            }
                        }

                        override fun deactivate() {
                            this.listener = null
                        }

                    }

//                    LaunchedEffect(key1 = locationPermissions.permissions) {
//                        locationPermissions.launchMultiplePermissionRequest()
//                    }

                    val mapProperties = MapProperties(
                        isBuildingEnabled = false,
                        isIndoorEnabled = false,
                        isMyLocationEnabled = locationPermissions.allPermissionsGranted,
                        isTrafficEnabled = false,
                        latLngBoundsForCameraTarget = LatLngBounds(
                            LatLng(51.4728, -0.1687),
                            LatLng(51.5378, -0.0231)
                        ),
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
                            .fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = mapProperties,
                        uiSettings = mapUiSettings,
                        locationSource = myLocationSource
                    )
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(32.dp),
                        shape = RoundedCornerShape(32.dp),
                        color = Color.White,
                        tonalElevation = 18.dp,
                        shadowElevation = 18.dp,
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp)
                                .clickable {
                                    if (locationPermissions.allPermissionsGranted) {
                                        // TODO
                                    } else {
                                        locationPermissions.launchMultiplePermissionRequest()
                                    }
                                },
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "myLocation"
                        )
                    }
                }
            }
        }
    }

}
