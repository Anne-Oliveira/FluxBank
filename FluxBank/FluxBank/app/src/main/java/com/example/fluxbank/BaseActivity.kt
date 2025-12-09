package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    protected fun setupBottomNavigation() {
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navList = findViewById<ImageView>(R.id.nav_list)
        val navQr = findViewById<ImageView>(R.id.nav_qr)
        val navTransfer = findViewById<ImageView>(R.id.nav_transfer)
        val navSettings = findViewById<ImageView>(R.id.nav_settings)

        navHome.setOnClickListener {
            if (this !is HomeActivity) {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
        navList.setOnClickListener {
            if (this !is ExtratoActivity) {
                startActivity(Intent(this, ExtratoActivity::class.java))
            }
        }
        navQr.setOnClickListener {
            if (this !is LeitorQrActivity) {
                startActivity(Intent(this, LeitorQrActivity::class.java))
            }
        }
        navTransfer.setOnClickListener {
            if (this !is PixActivity) {
                startActivity(Intent(this, PixActivity::class.java))
            }
        }
        navSettings.setOnClickListener {
            if (this !is ConfiguracoesActivity) {
                startActivity(Intent(this, ConfiguracoesActivity::class.java))
            }
        }
    }
}