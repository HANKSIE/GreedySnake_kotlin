/*
 * 處理所有Body物件的靜態類別
 */

package com.example.greedysnake_kotlin

/*
 * bodys: 裝Body物件的清單
 */

class BodyContainer {

    companion object{

        var bodys = ArrayList<Body>()

        /*新增*/
        fun add(body: Body){
            bodys.add(body)
        }

        /*利用Body的tag查找body*/
        fun findBodyByTag(tag: Body.Companion.BodyType): Body? {
            bodys.iterator().forEach {
                if (it.tag == tag){
                    return it
                }
            }
            return null
        }

        fun bodysInit(): ArrayList<Body>{
            return ArrayList<Body>()
        }

    }

}