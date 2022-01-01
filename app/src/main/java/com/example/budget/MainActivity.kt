package com.example.budget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.Intent
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private val mWaitHandler: Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mWaitHandler.postDelayed({
            //The following code will execute after the 5 seconds.
            try {

                //Go to next page i.e, start the next activity.
                val intent = Intent(applicationContext, Add_Expense::class.java)
                startActivity(intent)

                //Let's Finish Splash Activity since we don't want to show this when user press back button.
                finish()
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
        }, 2000) // Give a 5 seconds delay.
    }

    override fun onDestroy() {
        super.onDestroy()

        mWaitHandler.removeCallbacksAndMessages(null)
    }
}