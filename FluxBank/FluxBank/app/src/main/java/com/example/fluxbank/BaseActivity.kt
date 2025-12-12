package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsCompat

open class BaseActivity : AppCompatActivity() {

    private fun enableImmersiveMode() {
        // Usa as APIs de compatibilidade (AndroidX) para esconder as barras de sistema
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableImmersiveMode()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) enableImmersiveMode()
    }

    protected fun setupBottomNavigation() {
        val navHome = findViewById<ImageView?>(R.id.nav_home)
        val navList = findViewById<ImageView?>(R.id.nav_list)
        val navQr = findViewById<ImageView?>(R.id.nav_qr)
        val navTransfer = findViewById<ImageView?>(R.id.nav_transfer)
        val navSettings = findViewById<ImageView?>(R.id.nav_settings)

        navHome?.setOnClickListener {
            if (javaClass != HomeActivity::class.java) {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
        navList?.setOnClickListener {
            if (javaClass != ExtratoActivity::class.java) {
                startActivity(Intent(this, ExtratoActivity::class.java))
            }
        }
        navQr?.setOnClickListener {
            if (javaClass != LeitorQrActivity::class.java) {
                startActivity(Intent(this, LeitorQrActivity::class.java))
            }
        }
        navTransfer?.setOnClickListener {
            if (javaClass != PixActivity::class.java) {
                startActivity(Intent(this, PixActivity::class.java))
            }
        }
        navSettings?.setOnClickListener {
            if (javaClass != ConfiguracoesActivity::class.java) {
                startActivity(Intent(this, ConfiguracoesActivity::class.java))
            }
        }
    }
}