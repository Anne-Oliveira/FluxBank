package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class LoadingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val documento = intent.getStringExtra("documento")

        Handler(Looper.getMainLooper()).postDelayed({
            val intentHome = Intent(this, HomeActivity::class.java)
            intentHome.putExtra("documento", documento)
            startActivity(intentHome)
            finish()
        }, 3000)
    }
}
