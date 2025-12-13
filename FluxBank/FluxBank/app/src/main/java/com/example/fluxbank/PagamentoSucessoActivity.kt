package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class PagamentoSucessoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento_sucesso)

        val pixKey = intent.getStringExtra("PIX_KEY") ?: ""
        val valorString = intent.getStringExtra("VALOR") ?: "0"
        val transacaoId = intent.getLongExtra("TRANSACAO_ID", 0L)
        val nomeDestinatario = intent.getStringExtra("NOME_DESTINATARIO") ?: "Destinatário"
        val documentoMascarado = intent.getStringExtra("DOCUMENTO_MASCARADO") ?: ""
        val tipoDocumento = intent.getStringExtra("TIPO_DOCUMENTO") ?: "CPF"
        val instituicao = intent.getStringExtra("INSTITUICAO") ?: "FluxBank"

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ComprovanteActivity::class.java)
            intent.putExtra("PIX_KEY", pixKey)
            intent.putExtra("VALOR", valorString)
            intent.putExtra("TRANSACAO_ID", transacaoId)
            intent.putExtra("NOME_DESTINATARIO", nomeDestinatario)
            intent.putExtra("DOCUMENTO_MASCARADO", documentoMascarado)
            intent.putExtra("TIPO_DOCUMENTO", tipoDocumento)
            intent.putExtra("INSTITUICAO", instituicao)
            startActivity(intent)
            finish()
        }, 2000)
    }
}