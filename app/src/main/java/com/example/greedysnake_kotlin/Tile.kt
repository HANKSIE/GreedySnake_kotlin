package com.example.greedysnake_kotlin

class Tile(positionX:Int = -1, positionY: Int = -1, tag: Block.Companion.Type = Block.Companion.Type.UNDEFINED) : Block {
    var positionX = positionX
    var positionY = positionY
    var tag = tag
}