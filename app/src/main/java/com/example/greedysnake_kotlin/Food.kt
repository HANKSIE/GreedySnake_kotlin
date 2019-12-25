/*
 *裝Food元件的Body
 */

package com.example.greedysnake_kotlin

import android.util.Log
import java.util.ArrayList


class Food(tag: Body.Companion.BodyType = Body.Companion.BodyType.FOOD) : Body(tag){

    /*移除r,c為rcData中r,c的GameWidget*/
    fun removeWidget(rcData: Map<String,Int>){
        val r = rcData["r"]
        val c = rcData["c"]

        for (i in widgets.lastIndex downTo 0){
            val widget = widgets[i]
            if(widget.r == r && widget.c == c){
                widgets.remove(widget)
            }
        }
    }

    /*隨機生成food*/
    fun generateFood(tileMap: TileMap): Boolean{

        val canSetFoodArr = getCanSetFoodArr(tileMap)
        if (canSetFoodArr.size > 0){
            val result = canSetFoodArr.random()
            this.addWidget(result)
            Log.i("RESULT", "${result.r}, ${result.c}")
            return true
        }else{
            return false
        }

    }

    /*隨機生成多個food*/
    fun generateMuitipleFoods(tileMap: TileMap, amount: Int): Boolean{

        var isAllSuccess = true

        for (count in 1..amount){
            if(!generateFood(tileMap)){
                isAllSuccess = false
            }
        }

        return isAllSuccess

    }

    /*取得可以設定food的位置(以新產生的GameWidget回傳位置)*/
    fun getCanSetFoodArr(tileMap: TileMap): ArrayList<GameWidget> {
        val map = tileMap.map
        val canSetFoodArr = ArrayList<GameWidget>()
        for (r in map.indices){
            for (c in map[r].indices){
                if (map[r][c]?.tag == Block.Companion.Type.UNDEFINED){
                    canSetFoodArr.add(GameWidget(r,c,Block.Companion.Type.FOOD))
                }
            }
        }
        return canSetFoodArr
    }

}

