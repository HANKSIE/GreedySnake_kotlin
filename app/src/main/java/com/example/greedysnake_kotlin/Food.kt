/*
 *裝Food元件的Body
 */

package com.example.greedysnake_kotlin


class Food(tag: Companion.BodyType = Companion.BodyType.FOOD) : Body(tag){
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
}

