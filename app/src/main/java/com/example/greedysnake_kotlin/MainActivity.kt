package com.example.greedysnake_kotlin


import android.graphics.*
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.math.abs
import java.lang.Thread as Thread

/*
 * tileMap: 存取tileMap
 * gridPaint: 畫格線的paint
 * tilePaint: 畫map元素的paint
 *
 *
 * mySnake: 玩家操做的蛇
 * wall: 牆壁
 * food: 食物
 *
 * x1,y1,x2,y2: 取得使用者點與放的座標來判斷手勢滑動方向
 *
 * 主要流程位於surfaceCreated()內
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Game(surfaceView)
    }

    inner class Game(surfaceView: SurfaceView): SurfaceHolder.Callback{
        private var score :Int = 0
        private var time :Int = 0
        private var x1 :Float = 0.0f
        private var x2 :Float = 0.0f
        private var y1 :Float = 0.0f
        private var y2 :Float = 0.0f
        private var mode = 0
        private var touchDir = Snake.Companion.Direction.DOWN

        private lateinit var tileMap: TileMap
        private lateinit var holder: SurfaceHolder

        private var isStop = false

        private val gridPaint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 2f
        }

        private val tilePaint = Paint().apply {
            color = Color.RED
        }

        private lateinit var mySnake: Snake

        private lateinit var food: Food

        private lateinit var wall: Wall

        private lateinit var game: AsyncTask<Void, Void, Boolean>
        private lateinit var gametime: AsyncTask<Void, Void, Boolean>

        //--------------------------------------------------------------------------------------------------------

        init {
            intent?.extras?.let {
                mode = it.getInt("mode")
                when(mode){
                    0->time=60
                    1->time=0
                }
            }
            surfaceView.holder.addCallback(this)

            surfaceView.setOnTouchListener { v, event ->
                val action: Int = MotionEventCompat.getActionMasked(event)
                when (action) {
                    MotionEvent.ACTION_DOWN -> {//swd
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
                                touchDir = Snake.Companion.Direction.RIGHT
//                        Toast.makeText(this, "向右滑 swipe ${x1} sub ${x2} = ${X_move}", Toast.LENGTH_SHORT).show()
                            }
                            else{//表示向左
                                touchDir = Snake.Companion.Direction.LEFT
//                        Toast.makeText(this, "向左滑 swipe ${x1} sub ${x2} = ${X_move}", Toast.LENGTH_SHORT).show()
                            }
                        }else{//移動距離：Y軸>X軸表示上下移動
                            if (Y_move > 0){//表示向下
                                touchDir = Snake.Companion.Direction.DOWN
//                        Toast.makeText(this, "向下滑 swipe ${x1} sub ${x2} = ${Y_move}", Toast.LENGTH_SHORT).show()
                            }
                            else{//表示向上
                                touchDir = Snake.Companion.Direction.UP
//                        Toast.makeText(this, "向上滑 swipe ${x1} sub ${x2} = ${Y_move}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        true
                    }
                    else->{surfaceView.onTouchEvent(event)}
                }
            }
        }

        private fun resetAsyncTask() {
            game =  object : AsyncTask<Void, Void, Boolean>() {
                override fun doInBackground(vararg p0: Void?): Boolean {
                    while (!isCancelled) {
                        try {
                            tileMap.init() //清除地圖
                            Thread.sleep(80)
                            mySnake.setNewDir(touchDir)
                            tileMap.setBodys2tileMap() //將身體元件放入地圖
                            draw(holder) //依據地圖資料畫圖
                            update() //更新Body資料
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    return true
                }
            }
            gametime =  object : AsyncTask<Void, Void, Boolean>() {
                override fun doInBackground(vararg p0: Void?): Boolean {
                    while (!isCancelled) {
                        try {
                            when(mode){
                                0->time-=1
                                1->time+=1
                            }
                            setTimetext()
                            Thread.sleep(1000)
                            if (time<=0){
                                gameOver()
                            }
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    return true
                }
            }
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            Log.e("Msg", "Created!")
            this.holder = holder!!
            if (isStop){
                Log.e("Msg", "Game Resumed!")
                resetAsyncTask()
                gameStart()
            }else{
                gameInit()
            }

        }

        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            Log.e("Msg", "Changed!")
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            gameStop()
            Log.e("Msg", "Destroyed!")
        }

        /*遊戲初始化*/
        private fun gameInit(){

            BodyContainer.bodys = BodyContainer.bodysInit()

            tileMap = TileMap.getTileMap(holder)

            mySnake = Snake(tag = Body.Companion.BodyType.ME, dir = Snake.Companion.Direction.DOWN).apply {
                addWidget(SnakeWidget(1,5,Block.Companion.Type.SNAKE_HEAD))
                addWidget(SnakeWidget(1,6,Block.Companion.Type.SNAKE_WIDGET))
            }

            food = Food().apply {
                addWidget(GameWidget(10,5,Block.Companion.Type.FOOD))
                addWidget(GameWidget(11,5,Block.Companion.Type.FOOD))
                addWidget(GameWidget(12,5,Block.Companion.Type.FOOD))
                addWidget(GameWidget(13,5,Block.Companion.Type.FOOD))
                addWidget(GameWidget(14,5,Block.Companion.Type.FOOD))
                addWidget(GameWidget(15,5,Block.Companion.Type.FOOD))
            }
            wall = Wall.initWall(tileMap)

            BodyContainer.add(mySnake)
            BodyContainer.add(food)
            BodyContainer.add(wall)

            resetAsyncTask()
            gameStart()
        }

        /*繪圖*/
        private fun draw(holder: SurfaceHolder){
            val canvas = holder.lockCanvas()
            drawTileMap(canvas)
            drawGridLine(canvas)
            holder.unlockCanvasAndPost(canvas)
        }

        /*畫格線*/
        private fun drawGridLine(canvas: Canvas) {

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
        private fun drawTileMap(canvas: Canvas){

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
        private fun getColor(tag: Block.Companion.Type) =  when(tag){
            Block.Companion.Type.WALL -> Color.GRAY
            Block.Companion.Type.SNAKE_HEAD -> Color.RED
            Block.Companion.Type.SNAKE_WIDGET -> Color.WHITE
            Block.Companion.Type.FOOD -> Color.BLUE
            else -> {
                Color.BLACK
            }
        }

        /*資料更新(Body們的資料)*/
        private fun update(){

            /*走訪所有的Body，如果是可移動的(ME, ENEMY)就移動*/
            BodyContainer.bodys.iterator().forEach {
                when(it.tag){
                    Body.Companion.BodyType.ME, Body.Companion.BodyType.ENEMY-> {
                        move(it as Snake)
                    }
                }
            }

            collisionSolve()

        }

        /*移動snake中的widget位置*/
        private fun move(snake: Snake){
            val head = mySnake.getHead()

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

        /*碰撞後的處理*/
        private fun collisionSolve(){
            var myHead = mySnake.getHead()
            if (isCollision(myHead)){
                Log.e("碰撞發生!!!，碰撞類型",getCollisionType(myHead)?.name.toString())
                when(getCollisionType(myHead)){
                    Block.Companion.Type.FOOD -> {
                        val last = mySnake.widgets.last() as SnakeWidget
                        mySnake.addWidget(SnakeWidget(last.preR, last.preC, Block.Companion.Type.SNAKE_WIDGET))
                        food.removeWidget(getCollision_R_C(myHead))
                        food.generateMuitipleFoods(tileMap,1)
                        score+=100
                        scoreText.text = "Score:" + score.toString()
                    }
                    Block.Companion.Type.WALL, Block.Companion.Type.SNAKE_WIDGET -> {
                        gameOver()
                    }
                }
            }
        }

        /*碰撞檢測*/
        private fun isCollision(myHead: GameWidget): Boolean{
            val r = myHead.r
            val c = myHead.c
            val flag = (tileMap.map[r][c]?.tag == Block.Companion.Type.WALL ||
                    tileMap.map[r][c]?.tag == Block.Companion.Type.FOOD ||
                    tileMap.map[r][c]?.tag == Block.Companion.Type.SNAKE_WIDGET)

            return flag
        }

        /*取得和元件碰撞的元件的類型*/
        private fun getCollisionType(widget: GameWidget): Block.Companion.Type?{
            val r = widget.r
            val c = widget.c
            val tag = tileMap.map[r][c]?.tag

            return tag
        }

        /*取得和元件碰撞的元件的Row和Column*/
        private fun getCollision_R_C(widget: GameWidget): Map<String, Int>{
            val r = widget.r
            val c = widget.c
            val result = mapOf(Pair("r",r), Pair("c",c))

            return result
        }

        /*遊戲開始*/
        private fun gameStart(){
            game.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            gametime.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }

        /*遊戲停止*/
        private fun gameStop(){
            game.cancel(true)
            gametime.cancel(true)
            isStop = true
        }

        /*遊戲結束處理*/
        private fun gameOver(){
            gameStop()
            finish()
        }

        /*調整Bitmap比例*/
        private fun adjustBitmap(bitmap: Bitmap, scale: Float) : Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val mat = Matrix()
            mat.setScale(scale,scale)

            return Bitmap.createBitmap(bitmap,0,0,width,height,mat,true)
        }
        private fun setTimetext(){
            timeText.text = "time:" + (time/60).toString() + ":" + (time%60).toString()
        }
    }
}

