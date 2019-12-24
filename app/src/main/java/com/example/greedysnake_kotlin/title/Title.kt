package com.example.greedysnake_kotlin.title

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.greedysnake_kotlin.MainActivity
import com.example.greedysnake_kotlin.R
import kotlinx.android.synthetic.main.activity_title.*

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

        init()
        initMusic()

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
        index = if (index+1 == 3) { 0 } else { index+1 }

        when (index) {
            0 -> {mode_button.setImageResource(R.drawable.ic_title_mode_limited); mode_background.setImageResource(R.drawable.ic_title_button_white)}
            1 -> {mode_button.setImageResource(R.drawable.ic_title_mode_unlimited); mode_background.setImageResource(R.drawable.ic_title_button_blue)}
            2 -> {mode_button.setImageResource(R.drawable.ic_title_mode_props); mode_background.setImageResource(R.drawable.ic_title_button_yellow)}
        }

        modeIndex = index
    }

    // 開始
    private fun btnStartSet() {
        Log.d("TAG", mode[modeIndex])
        val startIntent = Intent(this, MainActivity::class.java)
        startIntent.putExtra("mode", modeIndex)
        startActivityForResult(startIntent,1)
    }

    // 說明
    private fun btnHelpSet() {

        // region set and call help popupWindow

        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.help_view, null)

        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        // region set in help window detail

        val outside = view.findViewById<ConstraintLayout>(R.id.help_layout)
        val picture = view.findViewById<TextView>(R.id.helpContent)

        // 點擊幫助視窗外面
        outside.setOnClickListener {
            popupWindow.dismiss()
        }

        // endregion

        // 當 PopupWindow 結束的提示語
        popupWindow.setOnDismissListener {
            Toast.makeText(applicationContext, "Popup closed", Toast.LENGTH_SHORT).show()
        }

        TransitionManager.beginDelayedTransition(title_layout)

        // 顯示
        popupWindow.showAtLocation(
            title_layout,
            Gravity.CENTER,
            0,
            0)

        // endregion
    }


}
