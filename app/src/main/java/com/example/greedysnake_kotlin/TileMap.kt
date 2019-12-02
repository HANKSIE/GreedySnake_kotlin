package com.example.greedysnake_kotlin

import android.util.Log

class TileMap(gridColumn: Int, gridRow: Int, tileWidth: Int, tileHeight: Int){

    val gridColumn = gridColumn
    val gridRow = gridRow
    val tileWidth = tileWidth
    val tileHeight = tileHeight
    val map = Array(gridColumn, {arrayOfNulls<Tile>(gridRow)})

    init {
        for (i in map.indices){
            for (j in map[i].indices){
                map[i][j] = Tile()
            }
        }
    }

    fun init(){
        map.iterator().forEach {
            it.iterator().forEach {
                it?.tag = Block.Companion.Type.UNDEFINED
            }
        }
    }

    fun setTileTagByBody(body: Body){
        for (widget in body.widgets){
            val x = widget.x
            val y = widget.y
            map[y][x]?.tag = widget.tag
            Log.i("訊息 物件", widget.tag.toString())
            Log.i("訊息 x", x.toString())
            Log.i("訊息 y", y.toString())
            Log.i("訊息 px", map[x][y]?.positionX.toString())
            Log.i("訊息 py", map[x][y]?.positionY.toString())
        }
    }

    fun showAllTiles(){
        map.iterator().forEach {
            it.iterator().forEach {
                Log.e("Tile positionX", it?.positionX.toString())
                Log.e("Tile positionY", it?.positionY.toString())
            }
        }
    }

}