# GreedySnake_kotlin
使用kotlin撰寫的android貪食蛇


---
#基本物件說明
<br>
<br>
![image](https://github.com/HANKSIE/GreedySnake_kotlin/blob/3d68fe137330e328e51cea375932b2f3d5d7e004/%E5%9F%BA%E6%9C%AC%E7%89%A9%E4%BB%B6%E8%AA%AA%E6%98%8E.png)
---
#流程

loop{<br>
<br>
1.將TileMap.map中所有的tile.tag初始化<br>
2.利用GameWidget的r,c設定到TileMap.map[r][c].tag(Tile.tag)<br>
3.利用TileMap.map上的positionX,positionY以及tag劃出相對應物件<br>
4.更新所有body的資料(r,c)<br>
<br>
}<br>
