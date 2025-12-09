package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // Handler para esperar 3 segundos antes de ir para a próxima tela
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Finaliza a LoadingActivity para que o usuário não possa voltar para ela
        }, 3000) // 3000 milissegundos = 3 segundos
    }
}
