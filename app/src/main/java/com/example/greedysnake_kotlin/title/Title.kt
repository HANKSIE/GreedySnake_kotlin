package com.example.greedysnake_kotlin.title

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MotionEventCompat
import com.example.greedysnake_kotlin.ClockActivity
import com.example.greedysnake_kotlin.R
import kotlinx.android.synthetic.main.activity_title.*
import kotlin.math.abs

class Title : AppCompatActivity() {

    // region global variables

    private var modeIndex = 0
    private var mode = listOf("normal", "unlimited", "prop")
    private var lastTime: Long = 0

    //region Sound and Music
    private lateinit var titleBGM: MediaPlayer
    private lateinit var btnSound: MediaPlayer
    private lateinit var btnChangeSound: MediaPlayer
    private lateinit var settingSound: MediaPlayer
    //endregion

    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)

        initMusic()
        init()
        // region button

        // 模式按鈕
        mode_button.setOnClickListener {
            btnModeSet()
        }

        // 開始按鈕
        start_button.setOnClickListener {
            btnStartSet()
        }

        // 說明按鈕
        help_button.setOnClickListener {
            btnHelpSet()
        }

        // endregion
    }

    // 視窗取得焦點
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    // 隱藏UI
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // 返回鍵
    override fun onBackPressed() {

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastTime > 2* 1000) {
            lastTime = currentTime
            Toast.makeText(this, "再點一次以離開", Toast.LENGTH_SHORT).show()
        } else {
            super.finish()
        }
    }

    // 遊戲初始化
    private fun init() {
        titleBGM.start()
    }

    // 初始化音樂
    private fun initMusic() {
        // 標題
        titleBGM = MediaPlayer.create(this, R.raw.title_background_music)
        titleBGM.isLooping = true

        // 模式按鈕
        btnChangeSound = MediaPlayer.create(this, R.raw.mode_changing_sound)

        // 其他按鈕
        btnSound = MediaPlayer.create(this, R.raw.button_sound)

        // 設定
        settingSound = MediaPlayer.create(this, R.raw.setting_sound)
    }

    // 切換模式
    private fun btnModeSet() {
        var index = modeIndex
        index = if (index+1 == 2) { 0 } else { index+1 }

        when (index) {
            0 -> {mode_button.setImageResource(R.drawable.ic_title_mode_limited); mode_background.setImageResource(R.drawable.ic_title_button_white)}
            1 -> {mode_button.setImageResource(R.drawable.ic_title_mode_unlimited); mode_background.setImageResource(R.drawable.ic_title_button_blue)}
//            2 -> {mode_button.setImageResource(R.drawable.ic_title_mode_props); mode_background.setImageResource(R.drawable.ic_title_button_yellow)}
        }

        modeIndex = index
    }

    // 開始
    private fun btnStartSet() {
        Log.d("TAG", mode[modeIndex])
        val startIntent = Intent(this, ClockActivity::class.java)
        startIntent.putExtra("mode", modeIndex)
        startActivityForResult(startIntent,1)
    }

    // 說明
    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun btnHelpSet() {

//        title_layout.background.alpha = 80

        // region set and call help popupWindow

        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.help_view, null)

        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // region set in help window detail

        val helpLayout = view.findViewById<ConstraintLayout>(R.id.help_layout)
        val icon = view.findViewById<ImageView>(R.id.icon)
        val content = view.findViewById<TextView>(R.id.contentText)
        var nowPage = 1

        var x1 = 0.0f
        var x2: Float

        // 更改文字、圖示
        helpLayout.setOnTouchListener { _, event ->
            when (MotionEventCompat.getActionMasked(event)) {
                MotionEvent.ACTION_DOWN -> {//swd
                    x1 = event.x
                    return@setOnTouchListener true
                }

                MotionEvent.ACTION_UP -> { //手指放開螢幕
                    x2 = event.x
                    val xMove = x2 - x1

                    if (xMove < 0 && nowPage < 3) {
                        nowPage++
                    } else if (xMove > 0 && nowPage > 1) {
                        nowPage--
                    }

                    when (nowPage) {
                        1 -> {content.text = HelpPage1.text; icon.setImageResource(R.drawable.ic_help_image_1)}
                        2 -> {content.text = HelpPage2.text; icon.setImageResource(R.drawable.ic_help_image_2)}
                        3 -> {content.text = HelpPage3.text; icon.setImageResource(R.drawable.ic_help_image_3)}
                    }

                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener true
            }
        }

        // endregion

        // 點擊幫助視窗外面
        popupWindow.isOutsideTouchable = true

        TransitionManager.beginDelayedTransition(title_layout)

        // 顯示
        popupWindow.showAtLocation(title_layout, Gravity.CENTER, 0, 0)

        // endregion
    }
}
