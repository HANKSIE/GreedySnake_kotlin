/*
 * 處理地圖的類別
 */

package com.example.greedysnake_kotlin

import android.util.Log
import android.view.SurfaceHolder

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
    var preMap: Array<Array<Tile?>>

    /*地圖初始化，將map裡面塞滿tile*/
    init {
        for (c in map.indices){
            for (r in map[c].indices){
                map[c][r] = Tile()
            }
        }
        preMap = map.copyOf()
    }

    companion object{
        /*取得tileMap*/
        fun getTileMap(holder: SurfaceHolder?, gridRow: Int = 20, gridColumn: Int = 40): TileMap {

            val canvas = holder!!.lockCanvas()
            val screenWidth = canvas.width
            val screenHeight = canvas.height
            val tileWidth = screenWidth.toFloat() / gridRow
            val tileHeight = screenHeight.toFloat() / gridColumn

            val tileMap = TileMap(gridRow, gridColumn, tileWidth,tileHeight)
            var y = 0f

            for (c in 0 until gridColumn){
                var x = 0f
                for (r in 0 until gridRow) {
                    tileMap.map[c][r]?.apply {
                        positionX = x
                        positionY = y
                    }
                    x += tileWidth
                }
                y += tileHeight
            }

            holder.unlockCanvasAndPost(canvas)
            return tileMap
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
    private fun setTileTagByBody(body: Body){
//        Log.i("訊息 map size", "[${map.size},${map[0].size}]")
//        Log.i("訊息 map grid", "[$gridRow,$gridColumn]")
        for (widget in body.widgets){
           setTileTagByGameWidget(widget)
//            Log.i("訊息 r,c,Body", "($r,$c,${body.tag.name})")
        }
    }

    private fun setTileTagByGameWidget(widget: GameWidget){
        val r = widget.r
        val c = widget.c
        map[r][c]?.tag = widget.tag
    }

    /*將每個Body設置到地圖上*/
    fun setBodys2tileMap(){
        for (body in BodyContainer.bodys){
            this.setTileTagByBody(body)
        }
    }

}