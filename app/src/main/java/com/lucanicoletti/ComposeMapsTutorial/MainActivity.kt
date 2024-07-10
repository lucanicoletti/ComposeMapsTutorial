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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucanicoletti.ComposeMapsTutorial.ui.theme.ComposeMapsTutorialTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @OptIn(MapsComposeExperimentalApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMapsTutorialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val locationLondon = LatLng(
                        /* latitude = */ 51.48725,
                        /* longitude = */ -0.12768
                    )
                    val positions = mutableListOf<LatLng>()
                    for (i in 0..10) {
                        val deltaX = Random.nextDouble(0.0, 0.05)
                        val deltaY = Random.nextDouble(0.0, 0.05)
                        positions.add(
                            LatLng(
                                locationLondon.latitude + deltaX,
                                locationLondon.longitude + deltaY
                            )
                        )
                    }
                    for (i in 0..10) {
                        val deltaX = Random.nextDouble(0.0, 0.05)
                        val deltaY = Random.nextDouble(0.0, 0.05)
                        positions.add(
                            LatLng(
                                locationLondon.latitude - deltaX,
                                locationLondon.longitude - deltaY
                            )
                        )
                    }
                    val markerItems = positions.map { ClusterItemImpl(it) }
                    val cameraPositionState = rememberCameraPositionState {
                        this.position = CameraPosition.fromLatLngZoom(
                            /* target = */ locationLondon,
                            /* zoom = */ 12f
                        )
                    }

                    val configuration = LocalConfiguration.current
                    val screenHeight = configuration.screenHeightDp.dp
                    val screenWidth = configuration.screenWidthDp.dp

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                    ) {
                        val clusterManager = rememberClusterManager<ClusterItemImpl>()

                        // Here the clusterManager is being customized with a NonHierarchicalViewBasedAlgorithm.
                        // This speeds up by a factor the rendering of items on the screen.
                        clusterManager?.setAlgorithm(
                            NonHierarchicalViewBasedAlgorithm(
                                screenWidth.value.toInt(),
                                screenHeight.value.toInt()
                            )
                        )
                        if (clusterManager != null) {
                            Clustering(items = markerItems, clusterManager = clusterManager)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconAsClusterContentItem(data: MarkerData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = data.name,
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Custom Icon for Cluster",
            tint = Color.Red,
        )
    }
}

@Composable
fun IconAsClusterContent(cluster: Cluster<MarkerData>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format("%d elements", cluster.size),
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Custom Icon for Cluster",
            tint = Color.Blue,
        )
    }
}

@Composable
fun CustomClusterContent(cluster: Cluster<MarkerData>) {
    val size = cluster.size
    Surface(
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
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
        location = LatLng(51.517814, -0.1270),
        name = "British Museum",
        description = "The most famous museum in the city.",
        imageResourceId = R.drawable.museum,
    ),
    MarkerData(
        location = LatLng(51.532924, -0.10584),
        name = "Angel Station",
        description = "A well-known station in London City Centre.",
        imageResourceId = R.drawable.angel,
    ),
)


data class ClusterItemImpl(val location: LatLng) : ClusterItem {
    override fun getPosition(): LatLng = location

    override fun getTitle(): String? = null

    override fun getSnippet(): String? = null

    override fun getZIndex(): Float? = null
}


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