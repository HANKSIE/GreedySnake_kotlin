package com.example.greedysnake_kotlin

class Wall(tag: BodyType) : Body(tag) {

        companion object{
        fun initWall(tileMap: TileMap) :Body{
            val wall = Wall(tag = BodyType.WALL)
            for (i in tileMap.map.indices){
                if(i == 0 || i == tileMap.map.size-1){
                    for (widget in tileMap.map[i]){
                        wall.addWidget(widget!!.row, widget!!.column, Tile.Companion.Type.WALL, tileMap)
                    }
                }else{
                    val start = tileMap.map[i].first()!!
                    val end = tileMap.map[i].last()!!
                    wall.addWidget(start.row, start.column, Tile.Companion.Type.WALL, tileMap)
                    wall.addWidget(end.row, end.column, Tile.Companion.Type.WALL, tileMap)
                }
            }
            return wall
        }
    }

}