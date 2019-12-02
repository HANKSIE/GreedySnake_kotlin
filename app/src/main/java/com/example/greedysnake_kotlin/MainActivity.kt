/*
 * 主類別
 */

package com.example.greedysnake_kotlin

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/*
 * tileMap: 存取tileMap
 * gridPaint: 畫格線的paint
 * tilePaint: 畫map元素的paint
 * timer: 畫面更新用的timer
 * task: 進行的任務(更新畫面)
 */

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback{

    lateinit var tileMap: TileMap

    val gridPaint = Paint().apply {
        color = Color.YELLOW
        strokeWidth = 2f
    }
    val tilePaint = Paint().apply {
        color = Color.RED
    }

    val timer = Timer()
    lateinit var task: TimerTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        surfaceView.holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {

        tileMap = getTileMap(holder!!)

        val mySnake = Snake(tag = Body.Companion.BodyType.ME, dir = Snake.Companion.Direction.RIGHT)

        mySnake.addWidget(SnakeWidget(7,11,Block.Companion.Type.SNAKE_HEAD))
        mySnake.addWidget(SnakeWidget(6,11,Block.Companion.Type.SNAKE_WIDGET))

        val wall = Wall.initWall(tileMap)

        BodyContainer.add(mySnake)
        BodyContainer.add(wall)

        task = object: TimerTask() {
            override fun run() {
                update()
                draw(holder)
            }
        }

        gameStart()

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {}

    /*取得tileMap*/
    fun getTileMap(holder: SurfaceHolder?, gridColumn: Int = 50, gridRow: Int = 50): TileMap {

        val canvas = holder!!.lockCanvas()
        val screenWidth = canvas.width
        val screenHeight = canvas.height
        val tileWidth = screenWidth.toFloat() / gridRow
        val tileHeight = screenHeight.toFloat() / gridColumn

//        Log.i("寬", screenWidth.toString())
//        Log.i("高", screenHeight.toString())
//        Log.i("單位寬", tileWidth.toString())
//        Log.i("單位高", tileHeight.toString())

        val tileMap = TileMap(gridColumn, gridRow, tileWidth,tileHeight)
        var count = 0
        var y = 0f

        for (i in 0 until gridColumn){
            var x = 0f
            for (j in 0 until gridRow) {
                tileMap.map[i][j]?.apply {
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

        var count = 0
        while (pointX < screenWidth){
            canvas.drawLine(pointX, 0f, pointX, screenHeight, gridPaint)
            pointX += tileWidth
            count++
        }
//        Log.i("Count", count.toString())
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
                canvas.drawRect(x.toFloat(), y.toFloat() ,(x+w).toFloat(), (y+h).toFloat(), tilePaint)
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

        tileMap.init()
        setBodys2tileMap()

        BodyContainer.bodys.iterator().forEach {
            when(it.tag){
                Body.Companion.BodyType.ME, Body.Companion.BodyType.ENEMY-> {
                    move(it as Snake)
                }
            }
        }

        if (isCollision()){
            gameStop()
        }

    }

    /*移動snake的位置(僅Snake.widget的位置，並非地圖上的位置)*/
    fun move(snake: Snake){
        snake.widgets.iterator().forEach {
            it as SnakeWidget

            when(snake.dir){
                Snake.Companion.Direction.UP->{
                    it.apply {
                        preX = x
                        preY = y
                        y -= 1
                    }
                }
                Snake.Companion.Direction.DOWN->{
                    it.apply {
                        preX = x
                        preY = y
                        y += 1
                    }
                }
                Snake.Companion.Direction.LEFT->{
                    it.apply {
                        preX = x
                        preY = y
                        x -= 1
                    }
                }
                Snake.Companion.Direction.RIGHT->{
                    it.apply {
                        preX = x
                        preY = y
                        x += 1
                    }
                }
            }

        }

    }

    /*將每個Body畫在地圖上*/
    fun setBodys2tileMap(){
        for (body in BodyContainer.bodys){
            tileMap.setTileTagByBody(body)
        }
    }

    /*取得蛇的頭部*/
    fun getMyHead(): SnakeWidget{
        val mySnake = BodyContainer.findBodyByTag(Body.Companion.BodyType.ME)
        val myHead = mySnake?.findWidgetByTag(mySnake , Block.Companion.Type.SNAKE_HEAD)
        return myHead as SnakeWidget
    }

    /*碰撞檢測*/
    fun isCollision(): Boolean{
        var myHead = getMyHead()
        val x = myHead.x
        val y = myHead.y
        val flag = (tileMap.map[x][y]?.tag == Block.Companion.Type.WALL)

        return flag
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