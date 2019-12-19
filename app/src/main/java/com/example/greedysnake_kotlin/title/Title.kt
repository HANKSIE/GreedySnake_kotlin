package com.example.greedysnake_kotlin.title

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
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
//        startActivity(startIntent)
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
        val leftButton = view.findViewById<ImageButton>(R.id.leftButton)
        val rightButton = view.findViewById<ImageButton>(R.id.rightButton)

        // 點擊幫助視窗外面
        outside.setOnClickListener {
            popupWindow.dismiss()
        }

        // 幫助左側按鈕
        leftButton.setOnClickListener {
//            leftButton.setImageResource(R.drawable.ic_launcher_background)
        }

        // 幫助右側按鈕
        rightButton.setOnClickListener {
//            rightButton.setImageResource(R.drawable.ic_launcher_foreground)
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
