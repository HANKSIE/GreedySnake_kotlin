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

        // 切換模式左按鈕
        changeModeL_image.setOnClickListener {
            modeIndex = modeChange("left", modeIndex)
        }

        // 切換模式右按鈕
        changeModeR_image.setOnClickListener {
            modeIndex = modeChange("right", modeIndex)
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
    private fun modeChange(mode: String, modeIndex: Int) : Int {
        var index = modeIndex
        when (mode) {
            "left" -> index = if (index-1 < 0) { 2 } else { index-1 }
            "right" -> index = if (index+1 > 2) { 0 } else { index+1 }
        }
        return index
    }

    // 開始
    private fun btnStartSet() {
        Log.d("TAG", mode[modeIndex])
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("mode", mode[modeIndex])
        startActivity(intent)
    }

    // 說明
    private fun btnHelpSet() {

    }

}
