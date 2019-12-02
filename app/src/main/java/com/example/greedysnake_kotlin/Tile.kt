/*
 * 儲存地圖每一個格子的資訊，用於canvas的繪圖
 */

package com.example.greedysnake_kotlin

/*
 * positionX: 繪圖x起點
 * positionY: 繪圖y起點
 * tag: 當前的widget類型
 */

class Tile(positionX: Float = -1.0f, positionY: Float = -1.0f, tag: Block.Companion.Type = Block.Companion.Type.UNDEFINED) : Block {
    var positionX = positionX
    var positionY = positionY
    var tag = tag
}