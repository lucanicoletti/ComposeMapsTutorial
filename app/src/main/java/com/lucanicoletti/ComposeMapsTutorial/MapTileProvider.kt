package com.lucanicoletti.ComposeMapsTutorial


import android.util.Log
import com.google.android.gms.maps.model.UrlTileProvider
import java.net.URL

class MapTileProvider(
    tileSize: Int,
) : UrlTileProvider(tileSize, tileSize) {

    companion object {
        // Order of format arguments: variant name, tile size, zoom level, tile x, tile y
        private const val URL_TEMPLATE = "https://" + BuildConfig.TILE_WEB_URL +
                "/%d/%d/%d.jpg?key=" + BuildConfig.TILE_API_KEY

        private const val BASE_TILE_SIZE = 256

        fun forDensity(densityDpi: Float): MapTileProvider {
            // Choose a size suitable for the given screen density. Adding .3f makes tvdpi (1.3x)
            // use a higher scale and looks nicer.
            val scale = Math.round(densityDpi + .3f)
                .coerceIn(1, 3) // we only support up to 3x the base tile size
            val tileSize = BASE_TILE_SIZE * scale
            return MapTileProvider(tileSize)
        }
    }

    override fun getTileUrl(x: Int, y: Int, zoom: Int): URL {
        val url = URL(URL_TEMPLATE.format(zoom, x, y))
        Log.e("url", url.toString())
        return url
    }
}