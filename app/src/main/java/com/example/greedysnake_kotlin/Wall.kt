/*
 * 牆壁
 */

package com.example.greedysnake_kotlin

class Wall(x: Int= -1 , y: Int = -1, tag: Body.Companion.BodyType = Body.Companion.BodyType.WALL) : Body(tag) {

    companion object{
        //初始化牆壁的靜態方法，會得到環繞地圖的牆壁
        fun initWall(tileMap: TileMap) :Body{
            val wall = Wall()
            for (i in 0 until tileMap.gridColumn){
                if(i == 0 || i == tileMap.gridRow-1){
                    for (j in tileMap.map[i].indices){
                        wall.addWidget(WallWidget(i,j,Block.Companion.Type.WALL))
                    }
                }else{
                    val start = 0
                    val end = tileMap.map[i].size-1

                    wall.addWidget(WallWidget(i,start,Block.Companion.Type.WALL))
                    wall.addWidget(WallWidget(i,end,Block.Companion.Type.WALL))
                }
            }
            return wall
        }
    }

}