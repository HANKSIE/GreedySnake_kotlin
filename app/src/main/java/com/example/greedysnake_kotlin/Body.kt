/*
 * 身體的處理
 */

package com.example.greedysnake_kotlin

/*
 * tag: 物件類型
 * speed: 移動速度
 * dir: 方向
 */

abstract class Body(tag: BodyType = BodyType.UNDEFINED) {

    var tag = tag

    /*身體部件清單*/
    var widgets = ArrayList<Tile>()

    /*玩家類型*/
    enum class BodyType{
        UNDEFINED, WALL, ME, ENEMY
    }

    fun addWidget(row: Int, column: Int, tag:Tile.Companion.Type, tileMap: TileMap){
        tileMap.setTile(row,column, Tile(row,column, tag))
        val widget = tileMap.getTile(row,column)
        widgets.add(widget)
    }

    fun removeWidget(block: Tile){
        widgets.remove(block)
    }

    fun getWidget(index: Int) = widgets.get(index)

}