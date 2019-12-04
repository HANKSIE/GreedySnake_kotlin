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
    var widgets = ArrayList<GameWidget>()

    /*玩家類型*/
    companion object{
        enum class BodyType{
            UNDEFINED, WALL, ME, ENEMY, FOOD
        }
    }

    fun addWidget(widget: GameWidget){
        widgets.add(widget)
    }

    fun removeAllWidget(){
        widgets = ArrayList<GameWidget>()
    }

    /*利用gameWidget.tag查找gameWidget*/
    fun findWidgetByTag(body: Body, tag: Block.Companion.Type): GameWidget?{
        body.widgets.iterator().forEach {
            if (it.tag == tag){
                return it
            }
        }
        return null
    }

}