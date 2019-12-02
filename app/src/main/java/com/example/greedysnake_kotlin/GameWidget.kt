/*
 * 最基礎的遊戲物件的抽象類別
 */

package com.example.greedysnake_kotlin

/*
 * x: x
 * y: y
 * tag: 此物件的類型
 */

abstract class GameWidget(x: Int, y: Int, tag: Block.Companion.Type) : Block {
    var x:Int = x
    var y:Int = y
    var tag = tag
}