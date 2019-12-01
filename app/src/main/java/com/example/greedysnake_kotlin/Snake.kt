package com.example.greedysnake_kotlin

class Snake(tag: BodyType, speed: Int = 0, dir: Direction = Direction.NONE) : Body(tag) {
    var speed = speed
    var dir = dir

    /*方向*/
    enum class Direction{
        NONE,UP,DOWN,LEFT,RIGHT
    }

}