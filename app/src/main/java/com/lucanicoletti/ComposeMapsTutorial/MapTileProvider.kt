package com.lucanicoletti.ComposeMapsTutorial


import com.google.android.gms.maps.model.UrlTileProvider
import java.net.URL

class MapTileProvider(tileSize: Int) : UrlTileProvider(tileSize, tileSize) {

    private val urlTemplate = "https://" + BuildConfig.TILE_WEB_URL +
            "/%d/%d/%d.jpg?key=" + BuildConfig.TILE_API_KEY

    override fun getTileUrl(x: Int, y: Int, zoom: Int): URL =
        URL(urlTemplate.format(zoom, x, y))

}