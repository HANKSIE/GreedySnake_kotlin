/*
 * 蛇的身體部件
 */

package com.example.greedysnake_kotlin

/*
 * preR = 上一個r位置
 * preC = 上一個c位置
 */

class SnakeWidget(r: Int, c: Int, tag: Block.Companion.Type)
    : GameWidget(r, c, tag) {

    var preR = r
    var preC = c

}