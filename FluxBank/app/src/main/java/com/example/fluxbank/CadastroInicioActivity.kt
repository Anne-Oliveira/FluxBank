package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class CadastroInicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_inicio)

        val btnNext = findViewById<ImageButton>(R.id.btnNext)
        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupAccountType)

        btnClose.setOnClickListener {
            finish()
        }

        btnNext.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == R.id.radioPf) {
                val intent = Intent(this, CadastroCpfActivity::class.java)
                startActivity(intent)
            } else if (selectedId == R.id.radioPj) {
                val intent = Intent(this, CadastroCnpjActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
