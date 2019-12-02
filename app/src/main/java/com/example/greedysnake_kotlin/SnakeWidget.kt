package com.example.greedysnake_kotlin

class SnakeWidget(x: Int, y: Int, tag: Block.Companion.Type)
    : GameObjectWidget(x, y, tag) {

    var preX = x
    var preY = y

}