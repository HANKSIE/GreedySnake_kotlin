/*
 * 遊戲最基礎的物件(Ex: snake的head、body && food)
 */

package com.example.greedysnake_kotlin

interface Block{

    companion object{
        enum class Type{
            UNDEFINED, SNAKE_HEAD, SNAKE_WIDGET, FOOD, WALL
        }
    }

}

