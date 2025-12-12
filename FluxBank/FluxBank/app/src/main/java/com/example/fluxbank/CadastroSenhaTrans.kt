package com.example.fluxbank

import android.widget.TextView
import com.example.fluxbank.utils.TokenManager

class CadastroSenhaTrans {

    private lateinit var tokenManager: TokenManager
    private lateinit var regra6: TextView
    private lateinit var regraMaiuscula: TextView
    private lateinit var regraEspecial: TextView
    private lateinit var regraCoincide: TextView

    private fun validarRegras(senha: String, confirmar: String): Boolean {
        var valido = true

        if (senha.length == 6) regra6.setTextColor(0xFF00AA00.toInt())
        else { regra6.setTextColor(0xFFFF0000.toInt()); valido = false }

        return valido
    }
}