package com.lucanicoletti.ComposeMapsTutorial

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import java.io.ByteArrayOutputStream

class CoordTileProvider(context: Context) : TileProvider {
    /* Scale factor based on density, with a 0.6 multiplier to increase tile generation
     * speed */
    private val scaleFactor: Float = context.resources.displayMetrics.density * 0.6f
    private val borderTile: Bitmap
    override fun getTile(x: Int, y: Int, zoom: Int): Tile {
        val coordTile = drawTileCoords(x, y, zoom)
        val stream = ByteArrayOutputStream()
        coordTile!!.compress(Bitmap.CompressFormat.PNG, 0, stream)
        val bitmapData = stream.toByteArray()
        return Tile(
            (TILE_SIZE_DP * scaleFactor).toInt(),
            (TILE_SIZE_DP * scaleFactor).toInt(), bitmapData
        )
        Tile.NULL
    }

    private fun drawTileCoords(x: Int, y: Int, zoom: Int): Bitmap? {
        // Synchronize copying the bitmap to avoid a race condition in some devices.
        var copy: Bitmap?
        synchronized(borderTile) { copy = borderTile.copy(Bitmap.Config.ARGB_8888, true) }
        val canvas = Canvas(copy!!)
        val tileCoords = "($x, $y)"
        val zoomLevel = "zoom = $zoom"
        /* Paint is not thread safe. */
        val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.color = Color.Blue.toArgb()
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = 18 * scaleFactor
        canvas.drawText(
            tileCoords, TILE_SIZE_DP * scaleFactor / 2,
            TILE_SIZE_DP * scaleFactor / 2, mTextPaint
        )
        canvas.drawText(
            zoomLevel, TILE_SIZE_DP * scaleFactor / 2,
            TILE_SIZE_DP * scaleFactor * 2 / 3, mTextPaint
        )
        return copy
    }

    companion object {
        private const val TILE_SIZE_DP = 256
    }

    init {
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint.style = Paint.Style.STROKE
        borderTile = Bitmap.createBitmap(
            (TILE_SIZE_DP * scaleFactor).toInt(),
            (TILE_SIZE_DP * scaleFactor).toInt(), Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(borderTile)
        canvas.drawRect(
            0f, 0f, TILE_SIZE_DP * scaleFactor, TILE_SIZE_DP * scaleFactor,
            borderPaint
        )
    }
}
