package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class EsqueciSenhaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esquecisenha)


        val btn_envioemail = findViewById<ImageButton>(R.id.btn_envioemail)
        val emailrec = findViewById<EditText>(R.id.emailrec)


        btn_envioemail.setOnClickListener {

            val email = emailrec.text.toString().trim()

            when {

                email.isEmpty() -> emailrec.error = "Email é obrigatório"

                !email.contains("@") -> emailrec.error = "Email inválido: deve conter @"

                else -> {
                    Toast.makeText(this, "Mensagem enviada para seu email!!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
