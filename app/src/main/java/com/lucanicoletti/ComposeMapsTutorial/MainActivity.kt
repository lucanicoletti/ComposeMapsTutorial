package com.lucanicoletti.ComposeMapsTutorial

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucanicoletti.ComposeMapsTutorial.ui.theme.ComposeMapsTutorialTheme
import java.util.Locale
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
                        val nonHierarchicalViewBasedAlgorithm =
                            NonHierarchicalViewBasedAlgorithm<ClusterItemImpl>(
                                screenWidth.value.toInt(),
                                screenHeight.value.toInt()
                            )
                        val distanceBasedAlgorithm =
                            NonHierarchicalDistanceBasedAlgorithm<ClusterItemImpl>().apply {
                                setMaxDistanceBetweenClusteredItems(150)
                            }
                        val clusterManager = rememberClusterManager<ClusterItemImpl>()
                        clusterManager?.let { manager ->
                            val renderer = rememberClusterRenderer(
                                clusterContent = { IconAsClusterContent(it) },
                                clusterItemContent = { IconAsClusterContentItem(it) },
                                clusterManager = manager,
                            )
                            manager.algorithm = distanceBasedAlgorithm
                            SideEffect {
                                if (manager.renderer != renderer) {
                                    manager.renderer = renderer ?: return@SideEffect
                                }
                            }
                            ApplyClicks(manager)
                            Clustering(
                                items = positions.map { ClusterItemImpl(it) },
                                clusterManager = manager,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApplyClicks(clusterManager: ClusterManager<ClusterItemImpl>) {
    val context = LocalContext.current
    SideEffect {
        clusterManager.setOnClusterClickListener {
            Toast.makeText(context, "setOnClusterClickListener", Toast.LENGTH_SHORT).show()
            false
        }
        clusterManager.setOnClusterItemClickListener {
            Toast.makeText(context, "setOnClusterItemClickListener", Toast.LENGTH_SHORT).show()
            false
        }
        clusterManager.setOnClusterItemInfoWindowClickListener {
            Toast.makeText(context, "setOnClusterItemInfoWindowClickListener", Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@Composable
fun IconAsClusterContentItem(data: ClusterItemImpl) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        data.title?.let {
            Text(
                text = it,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Custom Icon for Cluster",
            tint = Color.Red,
        )
    }
}

@Composable
fun IconAsClusterContent(cluster: Cluster<ClusterItemImpl>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format(Locale.getDefault(), "%d elements", cluster.size),
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


data class ClusterItemImpl(val location: LatLng) : ClusterItem {
    override fun getPosition(): LatLng = location

    override fun getTitle(): String? = null

    override fun getSnippet(): String? = null

    override fun getZIndex(): Float? = null
}