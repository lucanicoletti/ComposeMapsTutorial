package com.lucanicoletti.ComposeMapsTutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucanicoletti.ComposeMapsTutorial.ui.theme.ComposeMapsTutorialTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class, MapsComposeExperimentalApi::class)
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
                        Clustering(
                            items = markersData,
                            onClusterClick = {
                                cameraPositionState.move(
                                    CameraUpdateFactory.zoomIn()
                                )
                                false
                            },
                            onClusterItemClick = { _ -> false },
                            clusterContent = { CustomClusterContent(cluster = it) },
                            clusterItemContent = { CustomClusterContentItem(data = it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomClusterContent(cluster: Cluster<MarkerData>) {
    val size = cluster.size
    Surface(
        modifier = Modifier
            .width(40.dp)
            .height(20.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.Blue,
        contentColor = Color.White,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                size.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CustomClusterContentItem(data: MarkerData) {
    Surface(
        modifier = Modifier
            .width(80.dp)
            .height(40.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.Red,
        contentColor = Color.White,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                data.name,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            data.snippet?.let {
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = it,
                    fontSize = 8.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private val markersData = listOf(
    MarkerData(
        location = LatLng(51.508021, -0.075971),
        name = "Tower of London",
        description = "Nearby Tower Bridge, a beautiful castle in Central London.",
        imageResourceId = R.drawable.tower,
    ),
    MarkerData(
        location = LatLng(51.517814, -0.1270),
        name = "British Museum",
        description = "The most famous museum in the city.",
        imageResourceId = R.drawable.museum,
    ),
    MarkerData(
        location = LatLng(51.503333, -0.119664),
        name = "London Eye",
        description = "The highest wheel, the iconic Coca-Cola attraction.",
        imageResourceId = R.drawable.eye,
    ),
    MarkerData(
        location = LatLng(51.50082, -0.143016),
        name = "Buckingham Palace",
        description = "Where the Royal Family lives.",
        imageResourceId = R.drawable.palace,
    ),
    MarkerData(
        location = LatLng(51.532924, -0.10584),
        name = "Angel Station",
        description = "A well-known station in London City Centre.",
        imageResourceId = R.drawable.angel,
    ),
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

    override fun getZIndex(): Float? = 1f

}