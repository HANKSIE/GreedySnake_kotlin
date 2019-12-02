package com.example.greedysnake_kotlin

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), SurfaceHolder.Callback{

    lateinit var tileMap: TileMap
    var bodys = ArrayList<Body>()

    val gridPaint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.STROKE
    }
    val tilePaint = Paint().apply {
        color = Color.RED
    }

    val timer = Timer()

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

        val mySnake2 = Snake(tag = Body.Companion.BodyType.ME, dir = Snake.Companion.Direction.DOWN)

        mySnake2.addWidget(SnakeWidget(15,4,Block.Companion.Type.SNAKE_HEAD))
        mySnake2.addWidget(SnakeWidget(15,3,Block.Companion.Type.SNAKE_WIDGET))

        val wall = Wall.initWall(tileMap)


        bodys.add(mySnake)
        bodys.add(mySnake2)
        bodys.add(wall)

        val task = object: TimerTask() {
            override fun run() {
                tileMap.init()
                setBodys2tileMap()
                update()
                draw(holder)
            }
        }
        timer.schedule(task, 0, 500)

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {}

    fun getTileMap(holder: SurfaceHolder?, gridColumn: Int = 30, gridRow: Int = 30): TileMap {

        val canvas = holder!!.lockCanvas()
        val screenWidth = canvas.width
        val screenHeight = canvas.height
        val tileWidth = screenWidth / gridRow
        val tileHeight = screenHeight / gridColumn

//        Log.i("寬", screenWidth.toString())
//        Log.i("高", screenHeight.toString())
//        Log.i("單位寬", tileWidth.toString())
//        Log.i("單位高", tileHeight.toString())

        val tileMap = TileMap(gridColumn, gridRow, tileWidth,tileHeight)
        var count = 0
        var y = 0

        for (i in 0 until gridColumn){
            var x = 0
            for (j in 0 until gridRow) {
                tileMap.map[i][j]?.apply {
                    positionX = x
                    positionY = y
                }
                x += tileWidth
//                Log.i("X", x.toString())
//                Log.i("Y", y.toString())
//                Log.i("訊息", count++.toString())
            }
            y += tileHeight
        }

        holder.unlockCanvasAndPost(canvas)
        return tileMap
    }

    fun draw(holder: SurfaceHolder){
        val canvas = holder.lockCanvas()
        drawBodys(canvas)
        drawGridLine(canvas)
        holder.unlockCanvasAndPost(canvas)
    }

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
        Log.i("Count", count.toString())
        while (pointY < screenHeight){
            canvas.drawLine(0f, pointY, screenWidth, pointY, gridPaint)
            pointY += tileHeight
        }

    }

    fun drawBodys(canvas: Canvas){

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

    fun getColor(tag: Block.Companion.Type) =  when(tag){
        Block.Companion.Type.WALL -> Color.GRAY
        Block.Companion.Type.SNAKE_HEAD -> Color.RED
        Block.Companion.Type.SNAKE_WIDGET -> Color.WHITE
        Block.Companion.Type.FOOD -> Color.BLUE
        else -> {
            Color.BLACK
        }
    }

    fun findBodyByTag(tag: Body.Companion.BodyType): Body? {
        bodys.iterator().forEach {
            if (it.tag == tag){
                return it
            }
        }
        return null
    }

    fun findWidgetByTag(body: Body, tag: Block.Companion.Type): GameObjectWidget?{
        body.widgets.iterator().forEach {
            if (it.tag == tag){
                return it
            }
        }
        return null
    }

    fun gameStart(){

    }

    fun update(){
        bodys.iterator().forEach {
            when(it.tag){
                Body.Companion.BodyType.ME-> {
                    move(it as Snake)
                }
            }
        }


    }

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

    fun setBodys2tileMap(){
        for (body in bodys){
            tileMap.setTileTagByBody(body)
        }
    }

//    fun isCollision(): Boolean{
//
//    }

    fun gameStop(){

    }

    fun adjustBitmap(bitmap: Bitmap, scale: Float) : Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val mat = Matrix()
        mat.setScale(scale,scale)

        return Bitmap.createBitmap(bitmap,0,0,width,height,mat,true)
    }

}