package com.example.greedysnake_kotlin

import android.util.Log

class TileMap(gridColumn: Int, gridRow: Int, tileWidth: Int, tileHeight: Int){

    val gridColumn = gridColumn
    val gridRow = gridRow
    val tileWidth = tileWidth
    val tileHeight = tileHeight
    val map = Array(gridColumn, {arrayOfNulls<Tile>(gridRow)})
    val size = map.size

    init {
        for (i in map.indices){
            for (j in map[i].indices){
                map[i][j] = Tile(row=i,column= j)
            }
        }
    }

    fun setTile(row: Int, column: Int, x:Int, y:Int,
                tag: Tile.Companion.Type = Tile.Companion.Type.UNDEFINED){
        val tile = map[row][column]
        tile?.tag = tag
        tile?.x = x
        tile?.y = y
        map[row][column] = tile
    }

    fun setTile(row: Int, column: Int, newTile: Tile): TileMap{
        val tile = map[row][column]
        newTile.apply {
            x = tile!!.x
            y = tile!!.y
        }
        map[row][column] = newTile

        return this
    }

    fun getTile(row: Int, column: Int): Tile = map[row][column]!!

    fun showAllTiles(){
        map.iterator().forEach {
            it.iterator().forEach {
                Log.e("Tile X", it?.x.toString())
                Log.e("Tile Y", it?.y.toString())
            }
        }
    }

}