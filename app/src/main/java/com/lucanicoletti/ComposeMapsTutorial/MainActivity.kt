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
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GroundOverlay
import com.google.maps.android.compose.GroundOverlayPosition
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
                    }
                }
            }
        }
    }
}

private val pins = listOf(
    LatLng(51.51223, -0.08317),
    LatLng(51.517619, -0.082958),
    LatLng(51.525481, -0.087196),
    LatLng(51.510949, -0.086413),
)
private val pins1 = listOf(
    LatLng(51.511656, -0.078864),
    LatLng(51.510426, -0.076534),
    LatLng(51.514857, -0.077570),
)
private val outerPins = listOf(
    LatLng(51.509553, -0.089834),
    LatLng(51.505770, -0.071093),
    LatLng(51.518602, -0.070377),
    LatLng(51.528659, -0.080588),
    LatLng(51.529625, -0.097786),
    LatLng(51.516029, -0.093583),
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
        transparency = 0f,
        zIndex = 6f,
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

@Composable
fun CoordTileOverlay() {
    val context = LocalContext.current
    TileOverlay(
        tileProvider = CoordTileProvider(context),
        state = rememberTileOverlayState(),
        fadeIn = true,
        transparency = 0f,
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