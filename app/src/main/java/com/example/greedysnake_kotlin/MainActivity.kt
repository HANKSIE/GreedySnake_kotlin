package com.example.greedysnake_kotlin

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), SurfaceHolder.Callback{

    lateinit var tileMap: TileMap

    val gridPaint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.STROKE
    }

    val tilePaint = Paint().apply {
        color = Color.RED
    }

    val timer = Timer()
    val task = object: TimerTask() {
        override fun run() {
            update()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        surfaceView.holder.addCallback(this)

        timer.schedule(task, 0, 100)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {

        tileMap = getTileMap(holder!!,30,30)

        val mySnake = Snake(tag = Body.BodyType.ME)

        mySnake.addWidget(5, 4, Tile.Companion.Type.SNAKE_HEAD, tileMap)
        mySnake.addWidget(6, 4, Tile.Companion.Type.SNAKE_WIDGET, tileMap)

        val wall = Wall.initWall(tileMap)

        GameManager.add(mySnake)
        GameManager.add(wall)
        drawBodys(GameManager.bodys, holder!!)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {}

    private fun tryDrawing(holder: SurfaceHolder) {
        Log.i("訊息", "Trying to draw...")
        val canvas: Canvas? = holder.lockCanvas()
        if (canvas == null) {
            Log.e("訊息", "Cannot draw onto the canvas as it's null")
        } else {
//            draw(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun adjustBitmap(bitmap: Bitmap, scale: Float) : Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val mat = Matrix()
        mat.setScale(scale,scale)

        return Bitmap.createBitmap(bitmap,0,0,width,height,mat,true)
    }

    fun getTileMap(holder: SurfaceHolder?, gridColumn: Int = 20, gridRow: Int = 20): TileMap {

        val canvas = holder!!.lockCanvas()
        val screenWidth = canvas.width
        val screenHeight = canvas.height
        val tileWidth = screenWidth / gridRow
        val tileHeight = screenHeight / gridColumn

        Log.i("寬", screenWidth.toString())
        Log.i("高", screenHeight.toString())
        Log.i("單位寬", tileWidth.toString())
        Log.i("單位高", tileHeight.toString())

        val tileMap = TileMap(gridColumn, gridRow, tileWidth,tileHeight)
        var count = 0
        var y = 0
        for (i in 0 until gridColumn){
            var x = 0
            for (j in 0 until gridRow) {
                tileMap.setTile(i,j,x,y)
                x += tileWidth
                Log.i("X", x.toString())
                Log.i("Y", y.toString())
                Log.i("訊息", count++.toString())
            }
            y += tileHeight
        }

        holder.unlockCanvasAndPost(canvas)
        return tileMap
    }

    fun drawGridLine(canvas: Canvas, tileMap :TileMap) {

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

    fun drawBodys(bodys: ArrayList<Body>,  holder: SurfaceHolder){

        val canvas = holder.lockCanvas()

        for (body in bodys){
            for (widget in body.widgets){
                val x = widget.x.toFloat()
                val y = widget.y.toFloat()
                val w = tileMap.tileWidth.toFloat()
                val h = tileMap.tileHeight.toFloat()
                tilePaint.color = getColor(widget.tag)
                canvas.drawRect(x, y ,x+w, y+h, tilePaint)
            }
        }

        drawGridLine(canvas, tileMap)

        holder.unlockCanvasAndPost(canvas)
    }

    fun update(){

    }

    fun getColor(tag: Tile.Companion.Type) =  when(tag){
        Tile.Companion.Type.WALL -> Color.GRAY
        Tile.Companion.Type.SNAKE_HEAD -> Color.RED
        Tile.Companion.Type.SNAKE_WIDGET -> Color.WHITE
        Tile.Companion.Type.FOOD -> Color.BLUE
        else -> {
            Color.BLACK
        }
    }

}