/*
 * 貪食蛇主體
 */

package com.example.greedysnake_kotlin

/*
 * speed: 移動速度
 * dir: 方向
 * Direction: 方向類型
 */

class Snake(speed: Int = 0, dir: Direction = Direction.NONE, tag: Body.Companion.BodyType) : Body(tag) {

    var speed = speed
    var dir = dir

    companion object{
        /*方向*/
        enum class Direction{
            NONE,UP,DOWN,LEFT,RIGHT
        }
    }

}