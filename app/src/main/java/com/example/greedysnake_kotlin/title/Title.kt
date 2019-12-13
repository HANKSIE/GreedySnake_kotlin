package com.example.greedysnake_kotlin.title

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.greedysnake_kotlin.MainActivity
import com.example.greedysnake_kotlin.R
import kotlinx.android.synthetic.main.activity_title.*

private const val NUM_PAGES = 4

class Title : AppCompatActivity() {

    // region global variables

    private var modeIndex = 0
    private var mode = listOf("normal", "unlimited", "prop")

    private lateinit var hPager: ViewPager
    private lateinit var hPopupWindow: PopupWindow

    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)

//        hPager = findViewById(R.id.help_pager)
//        hPager.adapter = HelpPagerAdapter(supportFragmentManager)

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
        val startIntent = Intent(this, MainActivity::class.java)
        startIntent.putExtra("mode", mode[modeIndex])
        startActivity(startIntent)
    }

    // 說明
    private fun btnHelpSet() {
        hPopupWindow = PopupWindow(hPager, 900, 900)
        hPopupWindow.isOutsideTouchable = true
        TransitionManager.beginDelayedTransition(title_layout)
        hPopupWindow.showAtLocation(title_layout, Gravity.CENTER, 0, 0)
    }

    private inner class HelpPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment = when (position) {
            0 -> HelpFragment_1()
            1 -> HelpFragment_2()
            2 -> HelpFragment_3()
            else -> HelpFragment_4()
        }

        override fun getCount(): Int = NUM_PAGES

    }

}
