package com.example.greedysnake_kotlin


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.*
import android.os.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat
import com.example.greedysnake_kotlin.title.Title
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

    private lateinit var mainGame: Game
    private val self = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainGame = Game(surfaceView)
        Log.e("Msg", "Activity Created!")
    }

    override fun onPause() {
        super.onPause()
        Log.e("Msg", "Activity Paused!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Msg", "Activity Destroyed!")
    }


    inner class Game(surfaceView: SurfaceView): SurfaceHolder.Callback{


        // region variables

        // score
        private var score :Int = 0
        private var time :Int = 0

        // touch

        private var x1 :Float = 0.0f
        private var x2 :Float = 0.0f
        private var y1 :Float = 0.0f
        private var y2 :Float = 0.0f
        private var mode = 0
        private lateinit var touchDir: Snake.Companion.Direction

        // surfaceView
        private lateinit var tileMap: TileMap
        private lateinit var holder: SurfaceHolder
        private lateinit var game: AsyncTask<Void, Void, Boolean>
        private lateinit var gameTime: AsyncTask<Void, Void, Boolean>
        private var isStop = false
        private val gridPaint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 2f
        }
        private val tilePaint = Paint().apply {
            color = Color.RED
        }

        // snake
        private lateinit var mySnake: Snake
        private lateinit var food: Food
        private lateinit var wall: Wall

        private var isGameOver = false
        var hasDialog = false

        // endregion

        // 初始化
        init {
            intent?.extras?.let {
                mode = it.getInt("mode")
                when(mode){
                    0->time=60
                    1->time=0
                }
            }
            surfaceView.holder.addCallback(this)

            // 螢幕滑動
            snakeLayout.setOnTouchListener { _, event ->
                when (MotionEventCompat.getActionMasked(event)) {
                    MotionEvent.ACTION_DOWN -> {//swd
                        x1 = event.x
                        y1 = event.y
                        true
                    }
                    MotionEvent.ACTION_UP -> { //手指放開螢幕
                        x2 = event.x
                        y2 = event.y
                        val xMove = x2 - x1
                        val yMove = y2 - y1

                        touchDir = if (abs(xMove)>abs(yMove)){//移動距離：X軸>Y軸表示左右移動
                            if (xMove > 0){//表示向右
                                Snake.Companion.Direction.RIGHT
                            } else{//表示向左
                                Snake.Companion.Direction.LEFT
                            }
                        }else{//移動距離：Y軸>X軸表示上下移動
                            if (yMove > 0){//表示向下
                                Snake.Companion.Direction.DOWN
                            } else{//表示向上
                                Snake.Companion.Direction.UP
                            }
                        }
                        true
                    }
                    else->{surfaceView.onTouchEvent(event)}
                }
            }
        }

        // region surfaceView setting

        private fun resetAsyncTask() {
            game =  @SuppressLint("StaticFieldLeak")
            object : AsyncTask<Void, Void, Boolean>() {
                override fun doInBackground(vararg p0: Void?): Boolean {
                    while (!isCancelled) {
                        try {
                            tileMap.init() //清除地圖
                            Thread.sleep(100)
                            mySnake.setNewDir(touchDir)
                            tileMap.setBodys2tileMap() //將身體元件放入地圖
                            draw(holder) //依據地圖資料畫圖
                            update() //更新Body資料
                        } catch (e: InterruptedException) {

                        } catch (e: Exception) {

                        }
                    }
                    return true
                }
            }
            gameTime =  @SuppressLint("StaticFieldLeak")
            object : AsyncTask<Void, Void, Boolean>() {
                override fun doInBackground(vararg p0: Void?): Boolean {
                    while (!isCancelled) {
                        try {
                            when(mode){
                                0->time-=1
                                1->time+=1
                            }
                            setTimeText()
                            Thread.sleep(1000)
                            if (time<=0){
                                gameOver()
                            }
                        } catch (e: InterruptedException) {
//                            e.printStackTrace()
                        } catch (e: Exception) {
//                            e.printStackTrace()
                        }
                    }
                    return true
                }
            }
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            this.holder = holder!!
            Log.e("Msg", "Created!")

            if (isGameOver){
                draw(this.holder) //依據地圖資料畫圖
            }else if (isStop){
                tileMap.setBodys2tileMap()
                draw(this.holder)
                continueGameDialog()
            }else {
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

        /*繪圖*/
        private fun draw(holder: SurfaceHolder){
            val canvas = holder.lockCanvas()
            drawTileMap(canvas)
//            drawGridLine(canvas)
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

        // endregion

        // region gameSetting

        /*遊戲初始化*/
        private fun gameInit(){

            isGameOver = false
            score = 0
            scoreText.text = "Score:" + score.toString()

            when(mode){
                0->time=60
                1->time=0
            }

            BodyContainer.init()

            touchDir = Snake.Companion.Direction.DOWN
            tileMap = TileMap.getTileMap(holder)

            mySnake = Snake(tag = Body.Companion.BodyType.ME, dir = touchDir).apply {
                addWidget(SnakeWidget(1,5,Block.Companion.Type.SNAKE_HEAD))
                addWidget(SnakeWidget(1,6,Block.Companion.Type.SNAKE_WIDGET))
            }


            wall = Wall.initWall(tileMap)
            BodyContainer.add(mySnake)
            BodyContainer.add(wall)
            tileMap.setBodys2tileMap()

            food = Food()
            food.generateMuitipleFoods(tileMap, 6)

            BodyContainer.add(food)
            tileMap.setBodys2tileMap()

            resetAsyncTask()
            gameStart()
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
            val myHead = mySnake.getHead()
            if (isCollision(myHead)){
                Log.e("碰撞發生!!!，碰撞類型/位置","${getCollisionType(myHead)?.name}/${getCollisionRowAndColumn(myHead)}")

                when(getCollisionType(myHead)){
                    Block.Companion.Type.FOOD -> {
//                        Music.snakeMove(this@MainActivity)
                        val last = mySnake.widgets.last() as SnakeWidget
                        mySnake.addWidget(SnakeWidget(last.preR, last.preC, Block.Companion.Type.SNAKE_WIDGET))
                        food.removeWidget(getCollisionRowAndColumn(myHead))
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
        private fun getCollisionRowAndColumn(widget: GameWidget): Map<String, Int>{
            val r = widget.r
            val c = widget.c

            return mapOf(Pair("r",r), Pair("c",c))
        }

        /*遊戲開始*/
        private fun gameStart(){
            game.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            gameTime.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }

        /*遊戲停止*/
        private fun gameStop(){
            game.cancel(true)
            gameTime.cancel(true)
            isStop = true
        }

        /*遊戲結束處理*/
        private fun gameOver(){
            isGameOver = true
            gameStop()
            gameOverDialog()
        }

        // 遊戲暫停
        fun gamePause() {
            gameStop()
        }

        // 遊戲繼續
        fun gameResume() {
            if(!isGameOver){
                resetAsyncTask()
                gameStart()
            }
        }

        private fun gameOverDialog() {

            Handler(Looper.getMainLooper()).post {
                hasDialog = true
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setCancelable(false)
                builder.setTitle("遊戲結束")
                builder.setMessage("此局獲得 $score 分，是否重新開始遊戲?")

                builder.setNegativeButton("回到標題") { _, _ ->
                    hasDialog = false
                    finish()
                }
                builder.setPositiveButton("重新開始遊戲") { _, _ ->
                    hasDialog = false
                    gameInit()
                }

                builder.show()
            }

        }

        private fun continueGameDialog() {

            if (hasDialog){
                return
            }

            Handler(Looper.getMainLooper()).post {
                hasDialog = true
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setCancelable(false)
                builder.setMessage("繼續遊戲?")

                builder.setNegativeButton("繼續遊戲") { _, _ ->
                    hasDialog = false
                    gameResume()
                }
                builder.setPositiveButton("重新開始遊戲") { _, _ ->
                    hasDialog = false
                    gameInit()
                }

                builder.show()
            }

        }
        /*調整Bitmap比例*/
        private fun adjustBitmap(bitmap: Bitmap, scale: Float) : Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val mat = Matrix()
            mat.setScale(scale,scale)

            return Bitmap.createBitmap(bitmap,0,0,width,height,mat,true)
        }

        /*設置分數*/
        private fun setTimeText(){
            timeText.text = "time:" + (time/60).toString() + ":" + (time%60).toString()
        }

        // endregion
    }

    // 返回鍵
    override fun onBackPressed() {
        mainGame.gamePause()
        mainGame.hasDialog = true

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("暫停")
        builder.setMessage("是否繼續遊戲?")

        builder.setNegativeButton("離開遊戲") { _, _ ->

            mainGame.hasDialog = false
//            startActivity(Intent(self, Title::class.java))
            super.finish()
        }
        builder.setPositiveButton("繼續") { _, _ ->
            mainGame.hasDialog = false
            mainGame.gameResume()
        }

        builder.show()
    }

}

