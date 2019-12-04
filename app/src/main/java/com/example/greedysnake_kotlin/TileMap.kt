/*
 * 處理地圖的類別
 */

package com.example.greedysnake_kotlin

import android.util.Log

/*
 * gridRow: 橫向格子數量
 * gridColumn: 縱向格子數量
 * tileWidth: 地圖元素單位寬
 * tileHeight: 地圖元素單位高
 * map: 存處地圖元素的二維陣列
 */

class TileMap(gridRow: Int, gridColumn: Int, tileWidth: Float, tileHeight: Float){

    val gridRow = gridRow
    val gridColumn = gridColumn
    val tileWidth = tileWidth
    val tileHeight = tileHeight
    val map = Array(gridColumn, {arrayOfNulls<Tile>(gridRow)})

    /*地圖初始化，將map裡面塞滿tile*/
    init {

        for (c in map.indices){
            for (r in map[c].indices){
                map[c][r] = Tile()
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
//        Log.i("訊息 map size", "[${map.size},${map[0].size}]")
//        Log.i("訊息 map grid", "[$gridRow,$gridColumn]")
        for (widget in body.widgets){
            val r = widget.r
            val c = widget.c
            map[r][c]?.tag = widget.tag

//            Log.i("訊息 r,c,Body", "($r,$c,${body.tag.name})")
        }
    }

}