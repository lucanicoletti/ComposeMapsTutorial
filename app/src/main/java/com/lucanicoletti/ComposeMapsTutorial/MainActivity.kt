package com.lucanicoletti.ComposeMapsTutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
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
                        latLngBoundsForCameraTarget = LatLngBounds(
                            LatLng(51.4728, -0.1687), LatLng(51.5378, -0.0231)
                        ),
                        maxZoomPreference = 21f,
                        minZoomPreference = 3f,
                    )

                    val mapUiSettings = MapUiSettings(
                        compassEnabled = true,
                        myLocationButtonEnabled = true,
                        mapToolbarEnabled = true,
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
                        val markerState = remember {
                            MarkerState(LatLng(51.49, -0.1))
                        }
                    }
                }
            }
        }
    }
}

val positions = listOf(
    LatLng(51.508021, -0.075971),
    LatLng(51.503333, -0.119664),
    LatLng(51.50082, -0.143016),
    LatLng(51.517814, -0.1270),
    LatLng(51.532924, -0.10584),
)

data class MarkerData(
    val location: LatLng,
    val name: String,
    val description: String?,
    val imageResourceId: Int? = null,
) : ClusterItem {
    override fun getPosition(): LatLng = location

    override fun getTitle(): String? = name

    override fun getSnippet(): String? = description

    override fun getZIndex(): Float? = null

}