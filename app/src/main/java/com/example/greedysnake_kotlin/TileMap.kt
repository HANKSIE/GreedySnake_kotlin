/*
 * 處理地圖的類別
 */

package com.example.greedysnake_kotlin

import android.util.Log

/*
 * gridColumn: 橫向格子數量
 * gridRow: 縱向格子數量
 * tileWidth: 地圖元素單位寬
 * tileHeight: 地圖元素單位高
 * map: 存處地圖元素的二維陣列
 */

class TileMap(gridColumn: Int, gridRow: Int, tileWidth: Float, tileHeight: Float){

    val gridColumn = gridColumn
    val gridRow = gridRow
    val tileWidth = tileWidth
    val tileHeight = tileHeight
    val map = Array(gridColumn, {arrayOfNulls<Tile>(gridRow)})

    /*地圖初始化，將map裡面塞滿tile*/
    init {
        for (i in map.indices){
            for (j in map[i].indices){
                map[i][j] = Tile()
            }
        }
    }

    /*對已經塞滿tile的map進行tile.tag的初始化*/
    fun init(){
        map.iterator().forEach {
            it.iterator().forEach {
                it?.tag = Block.Companion.Type.UNDEFINED
            }
        }
    }

    /*將Body的GameWidget.tag到相對應的map位置*/
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

    fun showAllTilesInfo(){
        map.iterator().forEach {
            it.iterator().forEach {
                Log.e("Tile positionX", it?.positionX.toString())
                Log.e("Tile positionY", it?.positionY.toString())
            }
        }
    }

}