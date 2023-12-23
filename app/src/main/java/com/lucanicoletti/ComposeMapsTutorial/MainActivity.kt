package com.lucanicoletti.ComposeMapsTutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucanicoletti.ComposeMapsTutorial.ui.theme.ComposeMapsTutorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMapsTutorialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val locationLiverpool = LatLng(53.4084, -2.9916)
                    val cameraPositionState = rememberCameraPositionState {
                        this.position = CameraPosition.fromLatLngZoom(locationLiverpool, 13f)
                    }
                    var mapProperties by remember {
                        mutableStateOf(
                            MapProperties(
                                maxZoomPreference = 10f,
                                minZoomPreference = 5f,
                                isBuildingEnabled = false,
                                isIndoorEnabled = false,
                                isMyLocationEnabled = false,
                                isTrafficEnabled = false,
                                latLngBoundsForCameraTarget = LatLngBounds(
                                    /* southwest = */ LatLng(53.4084, -2.9916),
                                    /* northeast = */ LatLng(53.4084, -2.9916),
                                ),
                                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                                    context,
                                    R.raw.map_style
                                ),
                                mapType = MapType.SATELLITE,
                            )
                        )
                    }
                    var mapUiSettings by remember {
                        mutableStateOf(
                            MapUiSettings(
                                mapToolbarEnabled = false,
                                compassEnabled = false,
                                indoorLevelPickerEnabled = false,
                                myLocationButtonEnabled = false,
                                rotationGesturesEnabled = true,
                                scrollGesturesEnabled = true,
                                scrollGesturesEnabledDuringRotateOrZoom = true,
                                tiltGesturesEnabled = true,
                                zoomControlsEnabled = true,
                                zoomGesturesEnabled = true,
                            )
                        )
                    }
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = mapProperties,
                        uiSettings = mapUiSettings
                    )
                }
            }
        }
    }
}