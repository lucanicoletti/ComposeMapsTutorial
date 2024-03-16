package com.lucanicoletti.ComposeMapsTutorial

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GroundOverlay
import com.google.maps.android.compose.GroundOverlayPosition
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucanicoletti.ComposeMapsTutorial.ui.theme.ComposeMapsTutorialTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class, MapsComposeExperimentalApi::class)
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
                            LatLng(51.4728, -0.1687), LatLng(51.5378, -0.0231)
                        ),
                        mapType = MapType.TERRAIN,
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
                    val context = LocalContext.current

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = mapProperties,
                        uiSettings = mapUiSettings,
                    ) {
                        GroundOverlay(
                            position = GroundOverlayPosition.create(
                                location = LatLng(51.697892, -0.521313),
                                width = 59782f,
                                height = 46221f,
                            ),
                            anchor = Offset.Zero,
                            image = drawableToBitmapDescriptor(
                                context,
                                R.drawable.overlay2
                            ),
                            transparency = 0.5f
                        )
                    }
                }
            }
        }
    }
}

fun drawableToBitmapDescriptor(context: Context, drawableId: Int): BitmapDescriptor {
    val drawable: Drawable? = ContextCompat.getDrawable(context, drawableId)
    drawable?.let {
        val bitmap: Bitmap =
            Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        it.setBounds(0, 0, canvas.width, canvas.height)
        it.draw(canvas)
        canvas.scale(15f, 15f)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    } ?: run {
        throw IllegalArgumentException("Drawable not found")
    }
}

@Composable
fun DrawingsOnMaps() {
    Circle(
        center = circleCentre,
        clickable = false,
        fillColor = Color.Blue.copy(alpha = 0.5f),
        radius = 50.0,
        strokeColor = Color.White,
        strokePattern = null,
        strokeWidth = 1f,
        tag = "circle",
        visible = true,
        zIndex = 1f,
        onClick = {},
    )

    Polygon(
        points = locationsForPolygon,
        clickable = false,
        fillColor = Color.Red.copy(alpha = 0.5f),
        geodesic = false,
        holes = emptyList(),
        strokeColor = Color.White,
        strokeJointType = JointType.DEFAULT,
        strokePattern = listOf(Dash(3f)),
        strokeWidth = 10f,
        tag = "polygon",
        visible = true,
        zIndex = 1f,
        onClick = {},
    )

    Polyline(
        points = locationsForPolyline,
        clickable = false,
        color = Color.Green.copy(alpha = 0.8f),
        endCap = ButtCap(),
        geodesic = false,
        jointType = JointType.ROUND,
        pattern = listOf(Dash(55f), Dot()),
        startCap = ButtCap(),
        tag = "line!",
        visible = true,
        width = 15f,
        zIndex = 1f,
        onClick = {},
    )
}

val locationsForPolyline = listOf(
    LatLng(51.517814, -0.1270), // British Museum
    LatLng(51.532924, -0.10584), // Angel Station
    LatLng(51.508021, -0.075971) // Tower of London
)

val locationsForPolygon = listOf(
    LatLng(51.503333, -0.119664), // London Eye
    LatLng(51.50082, -0.143016), // Buckingham Palace
    LatLng(51.517814, -0.1270), // British Museum
)

val circleCentre = LatLng(51.508021, -0.075971) // Tower of London

@Composable
fun PolygonAlter() {
    Polygon(
        points = locationsForPolygonAlter,
        clickable = false,
        fillColor = Color.DarkGray.copy(alpha = 0.5f),
        geodesic = false,
        holes = emptyList(),
        strokeColor = Color.Black,
        strokeJointType = JointType.DEFAULT,
        strokePattern = listOf(Dot()),
        strokeWidth = 10f,
        tag = "polygon 2",
        visible = true,
        zIndex = 1.5f,
        onClick = {},
    )
}

val locationsForPolygonAlter = listOf(
    LatLng(51.517814, -0.1270), // British Museum
    LatLng(51.503333, -0.119664), // London Eye
    LatLng(51.50082, -0.143016), // Buckingham Palace
    LatLng(51.513109, -0.158969), // ~Marble Arch
)