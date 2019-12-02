package com.example.greedysnake_kotlin

abstract class GameObjectWidget(x: Int, y: Int, tag: Block.Companion.Type) : Block {
    var x:Int = x
    var y:Int = y
    var tag = tag
}