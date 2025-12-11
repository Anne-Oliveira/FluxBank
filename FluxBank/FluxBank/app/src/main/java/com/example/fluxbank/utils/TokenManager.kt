package com.example.fluxbank.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "FluxBankPrefs"
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_CPF = "user_cpf"
        private const val KEY_USER_CNPJ = "user_cnpj"
        private const val KEY_CONTA_ID = "conta_id"
        private const val KEY_NUMERO_CONTA = "numero_conta"
        private const val KEY_AGENCIA = "agencia"
        private const val KEY_SALDO = "saldo"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUserData(
        userId: Long,
        userName: String?,
        email: String,
        cpf: String? = null,
        cnpj: String? = null,
        contaId: Long? = null,
        numeroConta: String? = null,
        agencia: String? = null,
        saldo: String? = null
    ) {
        prefs.edit().apply {
            putLong(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName ?: "Usuário")
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_CPF, cpf)
            putString(KEY_USER_CNPJ, cnpj)
            contaId?.let { putLong(KEY_CONTA_ID, it) }
            putString(KEY_NUMERO_CONTA, numeroConta)
            putString(KEY_AGENCIA, agencia)
            putString(KEY_SALDO, saldo)
            apply()
        }
    }

    fun getUserId(): Long {
        return prefs.getLong(KEY_USER_ID, 0L)
    }

    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    fun getUserCpf(): String? {
        return prefs.getString(KEY_USER_CPF, null)
    }

    fun getUserCnpj(): String? {
        return prefs.getString(KEY_USER_CNPJ, null)
    }

    fun getContaId(): Long {
        return prefs.getLong(KEY_CONTA_ID, 0L)
    }

    fun getNumeroConta(): String? {
        return prefs.getString(KEY_NUMERO_CONTA, null)
    }

    fun getAgencia(): String? {
        return prefs.getString(KEY_AGENCIA, null)
    }

    fun getSaldo(): String? {
        return prefs.getString(KEY_SALDO, null)
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}