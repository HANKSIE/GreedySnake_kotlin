package com.example.greedysnake_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import kotlinx.android.synthetic.main.activity_clock.*

class ClockActivity : AppCompatActivity() {

    private var mode = 0
    private lateinit var startIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)

        test()

        // 倒數計時
        object: CountDownTimer(4000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                when ((millisUntilFinished / 1000).toInt()) {
                    3 -> countImageView.setImageResource(R.drawable.ic_count_3)
                    2 -> countImageView.setImageResource(R.drawable.ic_count_2)
                    1 -> countImageView.setImageResource(R.drawable.ic_count_1)
                    0 -> countImageView.setImageResource(R.drawable.ic_count_go)
                }
            }
            override fun onFinish() {
                startActivityForResult(startIntent,1)
            }
        }.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        finish()
    }

    private fun test(){
        // 取得傳過來的模式
        intent?.extras?.apply {
            mode = getInt("mode")
            startIntent = Intent(this@ClockActivity, MainActivity::class.java)
            startIntent.putExtra("mode", mode)
        }
        Log.d("TAG", mode.toString())
    }
}
