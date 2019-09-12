package com.example.e_doctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class OlvidastesConfirmarPin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvidastes_confirmar_pin)
    }

    fun comprobarPin(view: View){
        val intent = Intent(applicationContext, MenuPrincipal::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        overridePendingTransition(R.anim.enter, R.anim.exit)
    }
}
