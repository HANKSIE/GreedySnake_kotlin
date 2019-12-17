package com.example.greedysnake_kotlin.title

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.greedysnake_kotlin.MainActivity
import com.example.greedysnake_kotlin.R
import kotlinx.android.synthetic.main.activity_title.*

class Title : AppCompatActivity() {

    // region global variables

    private var modeIndex = 0
    private var mode = listOf("normal", "unlimited", "prop")

    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)

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
        startIntent.putExtra("mode", mode[modeIndex])
        startActivity(startIntent)
    }

    // 說明
    private fun btnHelpSet() {

    }


}
