package com.lucanicoletti.ComposeMapsTutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberTileOverlayState
import com.lucanicoletti.ComposeMapsTutorial.ui.theme.ComposeMapsTutorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMapsTutorialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val locationLondon = LatLng(/* latitude = */ 51.5072,/* longitude = */ -0.1276
                    )
                    val cameraPositionState = rememberCameraPositionState {
                        this.position =
                            CameraPosition.fromLatLngZoom(/* target = */ locationLondon,/* zoom = */
                                12f
                            )
                    }

                    val mapProperties = MapProperties(
                        isBuildingEnabled = false,
                        isIndoorEnabled = false,
                        isTrafficEnabled = false,
//                        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
//                            LocalContext.current,
//                            R.raw.map_style
//                        ),
                        maxZoomPreference = 21f,
                        minZoomPreference = 3f,
                    )

                    val mapUiSettings = MapUiSettings(
                        compassEnabled = false,
                        myLocationButtonEnabled = false,
                        rotationGesturesEnabled = true,
                        scrollGesturesEnabled = true,
                        scrollGesturesEnabledDuringRotateOrZoom = true,
                        tiltGesturesEnabled = true,
                        zoomControlsEnabled = false,
                        zoomGesturesEnabled = true,
                    )

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = mapProperties,
                        uiSettings = mapUiSettings,
                    ) {
                        CoordTileOverlay()
//                        UrlTileOverlay()
                    }
                }
            }
        }
    }
}

@Composable
fun CoordTileOverlay() {
    val context = LocalContext.current
    TileOverlay(
        tileProvider = CoordTileProvider(context),
        state = rememberTileOverlayState(),
        fadeIn = true,
        transparency = 0.5f,
        visible = true,
        zIndex = 5f,
        onClick = { tileOverlay ->
            // Do something with the tileOverlay
        }
    )
}

@Composable
fun UrlTileOverlay() {
    val dpi = LocalContext.current.resources.displayMetrics.densityDpi / 160f
    TileOverlay(
        tileProvider = MapTileProvider(dpi.toInt()),
        state = rememberTileOverlayState(),
        fadeIn = true,
        transparency = 0.5f,
        visible = true,
        zIndex = 4f,
        onClick = { tileOverlay ->
            // Do something with the tileOverlay
        }
    )
}