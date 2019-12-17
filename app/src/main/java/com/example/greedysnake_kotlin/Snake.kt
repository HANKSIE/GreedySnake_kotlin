/*
 * 貪食蛇主體
 */

package com.example.greedysnake_kotlin

/*
 * speed: 移動速度
 * dir: 方向
 * Direction: 方向類型
 */

class Snake(speed: Int = 0, dir: Direction = Direction.NONE, tag: Body.Companion.BodyType) : Body(tag) {

    var speed = speed
    var dir = dir

    companion object{
        /*方向*/
        enum class Direction{
            NONE,UP,DOWN,LEFT,RIGHT
        }
    }

    fun setNewDir(touchDir: Direction) {

        val snakeDir = this.dir
        val dirSolve = when(snakeDir){
            Companion.Direction.UP->{
                if (this.widgets.size > 0){
                    if (touchDir == Snake.Companion.Direction.DOWN){
                        snakeDir
                    }else{
                        touchDir
                    }
                }else{
                    snakeDir
                }
            }
            Companion.Direction.DOWN->{
                if (this.widgets.size > 0){
                    if (touchDir == Snake.Companion.Direction.UP){
                        snakeDir
                    }else{
                        touchDir
                    }
                }else{
                    snakeDir
                }
            }
            Companion.Direction.LEFT->{
                if (this.widgets.size > 0){
                    if (touchDir == Snake.Companion.Direction.RIGHT){
                        snakeDir
                    }else{
                        touchDir
                    }
                }else{
                    snakeDir
                }
            }
            Companion.Direction.RIGHT->{
                if (this.widgets.size > 0){
                    if (touchDir == Snake.Companion.Direction.LEFT){
                        snakeDir
                    }else{
                        touchDir
                    }
                }else{
                    snakeDir
                }
            }
            else -> {
                //因為規定要填else，所以隨便填個方向
                snakeDir
            }
        }

        dir = dirSolve

    }

    /*取得蛇的頭部*/
    fun getHead(): SnakeWidget{
        val head = this.findWidgetByTag(this , Block.Companion.Type.SNAKE_HEAD)
        return head as SnakeWidget
    }

}