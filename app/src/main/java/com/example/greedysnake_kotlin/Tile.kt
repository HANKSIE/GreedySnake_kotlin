/*
 * 遊戲最基礎的物件(Ex: snake的head、body && food)
 */

package com.example.greedysnake_kotlin

import android.graphics.Bitmap
import android.graphics.BitmapFactory

/*
 * x: x座標
 * y: y座標
 * width: 寬度
 * height: 高度
 * tag: 物件類型
 */

data class Tile(var row: Int,var column: Int,var tag:Type = Type.UNDEFINED,
                var x: Int = -1, var y: Int = -1){

    companion object{
        enum class Type{
            UNDEFINED, SNAKE_HEAD, SNAKE_WIDGET, FOOD, WALL
        }
    }
}

