/*
 * 主類別
 */

package com.example.greedysnake_kotlin

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.abs

/*
 * tileMap: 存取tileMap
 * gridPaint: 畫格線的paint
 * tilePaint: 畫map元素的paint
 * timer: 畫面更新用的timer
 * task: 進行的任務(更新畫面)
 *
 *
 *
 * 主要流程位於surfaceCreated()內
 */

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback{

    lateinit var tileMap: TileMap
    var x1 :Float = 0.0f
    var x2 :Float = 0.0f
    var y1 :Float = 0.0f
    var y2 :Float = 0.0f
    val gridPaint = Paint().apply {
        color = Color.YELLOW
        strokeWidth = 2f
    }
    val tilePaint = Paint().apply {
        color = Color.RED
    }

    val timer = Timer()
    lateinit var task: TimerTask


    val mySnake = Snake(tag = Body.Companion.BodyType.ME, dir = Snake.Companion.Direction.DOWN).apply {
        addWidget(SnakeWidget(1,5,Block.Companion.Type.SNAKE_HEAD))
        addWidget(SnakeWidget(1,6,Block.Companion.Type.SNAKE_WIDGET))
        addWidget(SnakeWidget(1,7,Block.Companion.Type.SNAKE_WIDGET))
//        addWidget(SnakeWidget(1,8,Block.Companion.Type.SNAKE_WIDGET))
//        addWidget(SnakeWidget(2,8,Block.Companion.Type.SNAKE_WIDGET))
//        addWidget(SnakeWidget(3,8,Block.Companion.Type.SNAKE_WIDGET))
//        addWidget(SnakeWidget(3,9,Block.Companion.Type.SNAKE_WIDGET))
//        addWidget(SnakeWidget(3,10,Block.Companion.Type.SNAKE_WIDGET))
//        addWidget(SnakeWidget(3,11,Block.Companion.Type.SNAKE_WIDGET))
//        addWidget(SnakeWidget(2,11,Block.Companion.Type.SNAKE_WIDGET))
//        addWidget(SnakeWidget(2,12,Block.Companion.Type.SNAKE_WIDGET))
    }

    val food = Food().apply {
        addWidget(GameWidget(10,5,Block.Companion.Type.FOOD))
    }

    //--------------------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        surfaceView.holder.addCallback(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean { //Mickey的傑作

        val action: Int = MotionEventCompat.getActionMasked(event)
        return when (action) {
            MotionEvent.ACTION_DOWN -> {//手指點擊螢幕
                x1 = event.x
                y1 = event.y
                true
            }
            MotionEvent.ACTION_UP -> { //手指放開螢幕
                x2 = event.x
                y2 = event.y
                var X_move = x2 - x1
                var Y_move = y2 - y1
                if (abs(X_move)>abs(Y_move)){//移動距離：X軸>Y軸表示左右移動
                    if (X_move > 0){//表示向右
                        Toast.makeText(this, "向右滑 swipe ${x1} sub ${x2} = ${X_move}", Toast.LENGTH_SHORT).show()
                    }
                    else{//表示向左
                        Toast.makeText(this, "向左滑 swipe ${x1} sub ${x2} = ${X_move}", Toast.LENGTH_SHORT).show()
                    }
                }else{//移動距離：Y軸>X軸表示上下移動
                    if (Y_move > 0){//表示向上
                        Toast.makeText(this, "向下滑 swipe ${x1} sub ${x2} = ${Y_move}", Toast.LENGTH_SHORT).show()
                    }
                    else{//表示向下
                        Toast.makeText(this, "向上滑 swipe ${x1} sub ${x2} = ${Y_move}", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {

        tileMap = getTileMap(holder!!)

        val wall = Wall.initWall(tileMap)
        BodyContainer.add(mySnake)
        BodyContainer.add(food)
        BodyContainer.add(wall)

        task = object: TimerTask() {
            override fun run() {
                tileMap.init() //清除地圖
                setBodys2tileMap() //將身體元件放入地圖
                draw(holder) //依據地圖資料畫圖
                update() //更新Body資料
            }
        }

        gameStart()

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {}

    /*取得tileMap*/
    fun getTileMap(holder: SurfaceHolder?, gridRow: Int = 15, gridColumn: Int = 20): TileMap {

        val canvas = holder!!.lockCanvas()
        val screenWidth = canvas.width
        val screenHeight = canvas.height
        val tileWidth = screenWidth.toFloat() / gridRow
        val tileHeight = screenHeight.toFloat() / gridColumn

        val tileMap = TileMap(gridRow, gridColumn, tileWidth,tileHeight)
        var y = 0f

        for (c in 0 until gridColumn){
            var x = 0f
            for (r in 0 until gridRow) {
                tileMap.map[c][r]?.apply {
                    positionX = x
                    positionY = y
                }
                x += tileWidth
            }
            y += tileHeight
        }

        holder.unlockCanvasAndPost(canvas)
        return tileMap
    }

    /*繪圖*/
    fun draw(holder: SurfaceHolder){
        val canvas = holder.lockCanvas()
        drawTileMap(canvas)
        drawGridLine(canvas)
        holder.unlockCanvasAndPost(canvas)
    }

    /*畫格線*/
    fun drawGridLine(canvas: Canvas) {

        val screenWidth = canvas.width.toFloat()
        val screenHeight = canvas.height.toFloat()
        val tileWidth = tileMap.tileWidth
        val tileHeight = tileMap.tileHeight

        var pointX = 0f
        var pointY = 0f

        while (pointX < screenWidth){
            canvas.drawLine(pointX, 0f, pointX, screenHeight, gridPaint)
            pointX += tileWidth
        }
        while (pointY < screenHeight){
            canvas.drawLine(0f, pointY, screenWidth, pointY, gridPaint)
            pointY += tileHeight
        }

    }

    /*畫地圖*/
    fun drawTileMap(canvas: Canvas){

        tileMap.map.iterator().forEach {
            it.iterator().forEach {
                val x = it!!.positionX
                val y = it!!.positionY
                val w = tileMap.tileWidth
                val h = tileMap.tileHeight
                tilePaint.color = getColor(it.tag)
                canvas.drawRect(x, y ,(x+w), (y+h), tilePaint)
            }
        }

    }

    /*取得畫元件對應的顏色*/
    fun getColor(tag: Block.Companion.Type) =  when(tag){
        Block.Companion.Type.WALL -> Color.GRAY
        Block.Companion.Type.SNAKE_HEAD -> Color.RED
        Block.Companion.Type.SNAKE_WIDGET -> Color.WHITE
        Block.Companion.Type.FOOD -> Color.BLUE
        else -> {
            Color.BLACK
        }
    }

    /*更新*/
    fun update(){

        BodyContainer.bodys.iterator().forEach {
            when(it.tag){
                Body.Companion.BodyType.ME, Body.Companion.BodyType.ENEMY-> {
                    move(it as Snake)
                }
            }
        }


        var myHead = getHead(mySnake)
        if (isCollision(myHead)){

            when(getCollisionType(myHead)){
                Block.Companion.Type.FOOD -> {
                    val last = mySnake.widgets.last() as SnakeWidget
                    mySnake.addWidget(SnakeWidget(last.preR, last.preC, Block.Companion.Type.SNAKE_WIDGET))
                    food.removeAllWidget()
                }
                Block.Companion.Type.WALL -> {
                    gameStop()
                }
            }


        }

    }

    /*移動snake中的widget位置*/
    fun move(snake: Snake){

        val head = getHead(snake)

        var newR = head.r
        var newC = head.c
        when(snake.dir){
            Snake.Companion.Direction.UP->{
                newR -= 1
            }
            Snake.Companion.Direction.DOWN->{
                newR += 1
            }
            Snake.Companion.Direction.LEFT->{
                newC -= 1
            }
            Snake.Companion.Direction.RIGHT->{
                newC += 1
            }
        }

        head.apply {
            preR = r
            preC = c
            r = newR
            c = newC
        }

        for (point in snake.widgets.indices){
            //如果是蛇的身體，讓他們的r,c為上一個的preR, preC
            if (point != 0){
                val preWidgets = snake.widgets[point-1] as SnakeWidget
                (snake.widgets[point] as SnakeWidget).apply {
                    preR = r
                    preC = c
                    r = preWidgets.preR
                    c = preWidgets.preC
                }
            }
        }

    }

    /*將每個Body設置到地圖上*/
    fun setBodys2tileMap(){
        for (body in BodyContainer.bodys){
            tileMap.setTileTagByBody(body)
        }
    }

    /*取得蛇的頭部*/
    fun getHead(snake: Snake): SnakeWidget{
        val myHead = snake.findWidgetByTag(snake , Block.Companion.Type.SNAKE_HEAD)
        return myHead as SnakeWidget
    }

    /*碰撞檢測*/
    fun isCollision(myHead: GameWidget): Boolean{
        val r = myHead.r
        val c = myHead.c
        val flag = (tileMap.map[r][c]?.tag == Block.Companion.Type.WALL || tileMap.map[r][c]?.tag == Block.Companion.Type.FOOD)

        return flag
    }

    /*取得碰撞類型*/
    fun getCollisionType(myHead: GameWidget): Block.Companion.Type?{
        val r = myHead.r
        val c = myHead.c
        val tag = tileMap.map[r][c]?.tag

        return tag
    }

    /*遊戲開始*/
    fun gameStart(){
        timer.schedule(task, 0, 100)
    }

    /*遊戲停止*/
    fun gameStop(){
        task.cancel()
    }

    /*調整Bitmap比例*/
    fun adjustBitmap(bitmap: Bitmap, scale: Float) : Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val mat = Matrix()
        mat.setScale(scale,scale)

        return Bitmap.createBitmap(bitmap,0,0,width,height,mat,true)
    }

}