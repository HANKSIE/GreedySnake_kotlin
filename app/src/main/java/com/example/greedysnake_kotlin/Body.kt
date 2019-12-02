/*
 * 身體的處理
 */

package com.example.greedysnake_kotlin

/*
 * tag: 物件類型
 * speed: 移動速度
 * dir: 方向
 */

abstract class Body(tag: BodyType) {

    var tag = tag
    /*身體部件清單*/
    var widgets = ArrayList<GameObjectWidget>()

    /*玩家類型*/
    companion object{
        enum class BodyType{
            UNDEFINED, WALL, ME, ENEMY
        }
    }

    fun addWidget(widget: GameObjectWidget){
        widgets.add(widget)
    }

}