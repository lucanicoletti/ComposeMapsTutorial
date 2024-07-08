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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GroundOverlay
import com.google.maps.android.compose.GroundOverlayPosition
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
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
                                13f
                            )
                    }

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                    ) {
                        GroundOverlayUnderground()
                        val polygonPoints = listOf(
                            LatLng(51.508021, -0.075971),
                            LatLng(51.483333, -0.119664),
                            LatLng(51.50082, -0.143016),
                            LatLng(51.517814, -0.1270),
                            LatLng(51.532924, -0.10584),
                        )
                        val polygonHole1 = listOf(
                            LatLng(51.503615, -0.118864),
                            LatLng(51.504942, -0.120934),
                            LatLng(51.501602, -0.121148),
                            LatLng(51.502439, -0.117604),
                        )
                        val polygonHole2 = listOf(
                            LatLng(51.510083, -0.108681),
                            LatLng(51.509483, -0.099916),
                            LatLng(51.513767, -0.099318),
                            LatLng(51.513568, -0.108818),
                        )
                        val polygonHole4 = listOf(
                            LatLng(51.517909, -0.125179),
                            LatLng(51.520447, -0.132289),
                            LatLng(51.522756, -0.126417),
                        )
                        Polygon(
                            points = polygonPoints,
                            holes = listOf(polygonHole1, polygonHole2, polygonHole4),
                            strokeColor = Color.Red,
                            strokeWidth = 10f,
                            strokePattern = listOf(Dash(20f), Gap(20f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroundOverlayWithColouredAreas() {
    val context = LocalContext.current
    GroundOverlay(
        position = GroundOverlayPosition.create(
            location = LatLng(51.697890, -0.521313),
            width = 59782f,
            height = 46221f,
        ),
        anchor = Offset.Zero,
        image = drawableToBitmapDescriptor(
            context,
            R.drawable.overlay2
        ),
        transparency = 0f
    )
}

private val polylinesPoints = listOf(
    LatLng(51.508021, -0.075971),
    LatLng(51.503333, -0.119664),
    LatLng(51.50082, -0.143016),
    LatLng(51.517814, -0.1270),
    LatLng(51.532924, -0.10584),
)


private val polygonPoints = listOf(
    LatLng(51.508021, -0.075971),
    LatLng(51.483333, -0.119664),
    LatLng(51.50082, -0.143016),
    LatLng(51.517814, -0.1270),
    LatLng(51.532924, -0.10584),
)

@Composable
fun GroundOverlayBoroughs() {
    val context = LocalContext.current
    GroundOverlay(
        position = GroundOverlayPosition.create(
            location = LatLng(51.697890, -0.521313),
            width = 59782f,
            height = 46221f,
        ),
        anchor = Offset.Zero,
        image = drawableToBitmapDescriptor(
            context,
            R.drawable.overlay3
        ),
        transparency = 0.5f,
    )
}

private val polygonHole1 = listOf(
    LatLng(51.503615, -0.118864),
    LatLng(51.504942, -0.120934),
    LatLng(51.501602, -0.121148),
    LatLng(51.502439, -0.117604),
)

private val polygonHole2 = listOf(
    LatLng(51.510083, -0.108681),
    LatLng(51.509483, -0.099916),
    LatLng(51.513767, -0.099318),
    LatLng(51.513568, -0.108818),
)

private val polygonHole3 = listOf(
    LatLng(51.523177, -0.085425),
    LatLng(51.523130, -0.057539),
    LatLng(51.531447, -0.080040),
)

@Composable
fun GroundOverlayUnderground() {
    val context = LocalContext.current
    GroundOverlay(
        position = GroundOverlayPosition.create(
            latLngBounds = LatLngBounds(
                /* southwest = */ LatLng(51.493684, -0.224643),
                /* northeast = */ LatLng(51.527323, -0.056233),
            )
        ),
        anchor = Offset.Zero,
        image = drawableToBitmapDescriptor(
            context,
            R.drawable.overlay1
        ),
        transparency = 0.5f,
    )
}

fun drawableToBitmapDescriptor(context: Context, drawableId: Int): BitmapDescriptor {
    val drawableResource: Drawable? = ContextCompat.getDrawable(context, drawableId)
    drawableResource?.let { drawable ->
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight

        val bitmap: Bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        canvas.scale(15f, 15f)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    } ?: run {
        throw IllegalArgumentException("Drawable not found")
    }
}

private val polygonHole4 = listOf(
    LatLng(51.517909, -0.125179),
    LatLng(51.520447, -0.132289),
    LatLng(51.522756, -0.126417),
)

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

private val polylinesColors = listOf(
    Color.Red,
    Color.Yellow,
    Color.Green,
    Color.Cyan
)

val locationsForPolyline = listOf(
    LatLng(51.517814, -0.1270), // British Museum
    LatLng(51.532924, -0.10584), // Angel Station
    LatLng(51.508021, -0.075971) // Tower of London
)

private val polylinesColorPairs = listOf(
    Color.Red to Color.Yellow,
    Color.Yellow to Color.Green,
    Color.Green to Color.Cyan,
    Color.Cyan to Color.Blue,
    Color.Blue to Color.Magenta,
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