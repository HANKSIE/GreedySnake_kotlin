/*
 * 遊戲最基礎的物件的介面
 */

package com.example.greedysnake_kotlin

/*
 * Type: 物件類型
 */

interface Block{

    companion object{
        enum class Type{
            UNDEFINED, SNAKE_HEAD, SNAKE_WIDGET, FOOD, WALL
        }
    }

}

