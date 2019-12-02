/*
 * 貪食蛇的身體部件
 */

package com.example.greedysnake_kotlin

/*
 * preX = 上一個x位置
 * preY = 上一個y位置
 */

class SnakeWidget(x: Int, y: Int, tag: Block.Companion.Type)
    : GameWidget(x, y, tag) {

    var preX = x
    var preY = y

}