/*
 * 遊戲管理者
 *
 * -------------------------------------------------------
 * [功能]
 * 每個Body的存取
 * 更新畫面(物件的移動)
 * 碰撞處理
 * -------------------------------------------------------
 */
package com.example.greedysnake_kotlin

class GameManager: Runnable {

    companion object{

        var bodys = ArrayList<Body>()
        var playerHead = mutableMapOf<String, Int>()

        fun init(){
            bodys = ArrayList<Body>()
        }

        fun add(body: Body){
            bodys.add(body)
        }

        fun remove(body: Body){
            bodys.remove(body)
        }

    }

    override fun run() {
        //處理碰撞
    }

    fun findBodysByTag(tag: Body.BodyType): Body? {
        bodys.iterator().forEach {
            if (it.tag == tag){
                return it
            }
        }
        return null
    }

    fun findTileByTag(body: Body, tag: Tile.Companion.Type): Tile?{
        body.widgets.iterator().forEach {
            if (it.tag == tag){
                return it
            }
        }
        return null
    }

}