package com.example.e_doctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.e_doctor.core.Preferencias

class MainActivity : AppCompatActivity() {
    //lateinit var timerTask: TimerTask
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
    //lateinit var mPreferencias: Preferencias


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // mPreferencias = Preferencias(this@MainActivity)
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
       // initializeTimerTask()
    }
//    private fun initializeTimerTask() {
//        timerTask = object : TimerTask() {
//            override fun run() {
//                UtilidadesHelper.llamarPantallaPrincipal()
//                finish()
//            }
//        }
//    }
    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }
}
