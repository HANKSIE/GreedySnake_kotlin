/*
 * 牆壁
 */

package com.example.greedysnake_kotlin

class Wall(tag: Body.Companion.BodyType = Body.Companion.BodyType.WALL) : Body(tag) {

    companion object{
        //初始化牆壁的靜態方法，會得到環繞地圖的牆壁
        fun initWall(tileMap: TileMap) :Wall{
            val wall = Wall()
            for (r in 0..tileMap.gridColumn-1){
                if(r == 0 || r == tileMap.gridColumn-1){
                    for (c in 0..tileMap.gridRow-1){
                        wall.addWidget(GameWidget(r,c,Block.Companion.Type.WALL))
                    }
                }else{
                    val start = 0
                    val end = tileMap.gridRow-1

                    wall.addWidget(GameWidget(r,start,Block.Companion.Type.WALL))
                    wall.addWidget(GameWidget(r,end,Block.Companion.Type.WALL))
                }
            }
            return wall
        }
    }

}