/*
 * 最基礎的遊戲物件類別
 */

package com.example.greedysnake_kotlin

/*
 * r: row
 * c: column
 * tag: 此物件的類型
 */

open class GameWidget(r: Int = -1, c: Int = -1, tag: Block.Companion.Type) : Block {
    var r:Int = r
    var c:Int = c
    var tag = tag
}