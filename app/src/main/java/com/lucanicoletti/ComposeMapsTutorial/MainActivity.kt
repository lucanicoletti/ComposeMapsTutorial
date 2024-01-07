package com.lucanicoletti.ComposeMapsTutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucanicoletti.ComposeMapsTutorial.ui.theme.ComposeMapsTutorialTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMapsTutorialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val locationLondon = LatLng(
                        /* latitude = */ 51.5072,
                        /* longitude = */ -0.1276
                    )
                    val cameraPositionState = rememberCameraPositionState {
                        this.position = CameraPosition.fromLatLngZoom(
                            /* target = */ locationLondon,
                            /* zoom = */ 12f
                        )
                    }

                    val locationPermissions = rememberMultiplePermissionsState(
                        permissions = listOf(
                            "android.permission.ACCESS_FINE_LOCATION",
                            "android.permission.ACCESS_COARSE_LOCATION"
                        ),
                    )

                    LaunchedEffect(key1 = locationPermissions.permissions) {
                        locationPermissions.launchMultiplePermissionRequest()
                    }

                    val mapProperties = MapProperties(
                        isBuildingEnabled = false,
                        isIndoorEnabled = false,
                        isMyLocationEnabled = locationPermissions.allPermissionsGranted,
                        isTrafficEnabled = false,
                        latLngBoundsForCameraTarget = LatLngBounds(
                            LatLng(51.4728, -0.1687),
                            LatLng(51.5378, -0.0231)
                        ),
                        mapType = MapType.TERRAIN,
                        maxZoomPreference = 21f,
                        minZoomPreference = 3f,
                    )

                    val mapUiSettings = MapUiSettings(
                        compassEnabled = true,
                        myLocationButtonEnabled = true,
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
                        for (data in markersData) {
                            MarkerInfoWindowContent(
                                state = MarkerState(
                                    position = data.location
                                ),
                                title = data.title,
                                snippet = data.description,
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(top = 6.dp),
                                        text = data.title,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                    )
                                    data.description?.let { desc -> Text(desc) }
                                    data.imageResourceId?.let { res ->
                                        Image(
                                            modifier = Modifier
                                                .padding(top = 6.dp)
                                                .size(240.dp),
                                            painter = painterResource(id = res),
                                            contentDescription = data.description
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

private val markersData = listOf(
    MarkerData(
        location = LatLng(51.508021, -0.075971),
        title = "Tower of London",
        description = "Nearby Tower Bridge, a beautiful castle in Central London.",
        imageResourceId = R.drawable.tower,
    ),
    MarkerData(
        location = LatLng(51.517814, -0.1270),
        title = "British Museum",
        description = "The most famous museum in the city.",
        imageResourceId = R.drawable.museum,
    ),
    MarkerData(
        location = LatLng(51.503333, -0.119664),
        title = "London Eye",
        description = "The highest wheel, the iconic Coca-Cola attraction.",
        imageResourceId = R.drawable.eye,
    ),
    MarkerData(
        location = LatLng(51.50082, -0.143016),
        title = "Buckingham Palace",
        description = "Where the Royal Family lives.",
        imageResourceId = R.drawable.palace,
    ),
    MarkerData(
        location = LatLng(51.532924, -0.10584),
        title = "Angel Station",
        description = "A well-known station in London City Centre.",
        imageResourceId = R.drawable.angel,
    ),
)


data class MarkerData(
    val location: LatLng,
    val title: String,
    val description: String?,
    val imageResourceId: Int? = null,
)