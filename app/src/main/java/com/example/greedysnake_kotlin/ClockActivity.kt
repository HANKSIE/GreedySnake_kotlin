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
    private lateinit var countDown: CountDownTimer



    companion object{
        private var nowTime: Long = 4000
    }

    override fun onStop() {
        super.onStop()
        countDown.cancel()
        countDown = getCountDown(nowTime)
        Log.e("Msg", "CountDown 畫面被Stop!!!")
    }

    override fun onResume() {
        super.onResume()
        countDown.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)
        Log.e("Msg", "CountDown 畫面被創建!!!")

        test()

        // 倒數計時
        countDown = getCountDown(nowTime)
        countDown.start()

    }

    private fun getCountDown(nowTime: Long): CountDownTimer{
        return (object: CountDownTimer(nowTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                ClockActivity.nowTime = millisUntilFinished
                Log.e("倒數",(millisUntilFinished / 1000).toInt().toString())
                when ((millisUntilFinished / 1000).toInt()) {
                    3 -> countImageView.setImageResource(R.drawable.ic_count_3)
                    2 -> countImageView.setImageResource(R.drawable.ic_count_2)
                    1 -> countImageView.setImageResource(R.drawable.ic_count_1)
                    0 -> countImageView.setImageResource(R.drawable.ic_count_go)
                }
            }
            override fun onFinish() {
                ClockActivity.nowTime = 4000
                startActivityForResult(startIntent,1)
            }
        })
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
