# GreedySnake_kotlin
使用kotlin撰寫的android貪食蛇



| Class| Describtion|extends/Implement|
| ---- | ---------- | --------------- |
|MainActivity|主要類別|AppCompatActivity, SurfaceHolder.Callback|
|BodyContainer|乘載所有遊戲物件的容器||
|SnakeWidget|Snake的組件|GameWidget|
|Snake|蛇|Body|
|WallWidget|Wall的組件|GameWidget|
|Wall|牆壁|Body|
|Tile| 儲存canvas繪圖用的資訊|Block|
|TileMap|地圖||

<br>
<br>

|Abstract Class|Describtion|extends/Implement|
| ---- | ---------- | --------------- |
|GameWidget|Body組件|Block|
|Body|遊戲物件||

<br>
<br>

|Interface|Describtion|extends/Implement|
| ---- | ---------- | --------------- |
|Block|定義基本元素||



```
                                                  
                         Block                       
                           |                      
               -----------------------            
               |                     |            
          GameWidget               Tile           
               |                                  
     -------------------                          
     |                 |                          
   SnakeWidget    WallWidget                      



              Body                                 
               |                                  
        ----------------                            
        |              |                            
      Snake           Wall                          
```                                                  
