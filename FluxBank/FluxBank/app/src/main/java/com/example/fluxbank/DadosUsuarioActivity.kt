package com.example.fluxbank


import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DadosUsuarioActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados_usuario)   // coloque o nome do seu XML aqui

        // ---------- SETA DE VOLTAR ----------
        val ivVoltar = findViewById<ImageView>(R.id.ivVoltar)
        ivVoltar.setOnClickListener {
            finish()
        }

        // ---------- CAMPOS DA TELA ----------
        val tvNome = findViewById<TextView>(R.id.Nome)
        val tvNomeTitulo = findViewById<TextView>(R.id.Nometitulo)

        val tvCpf = findViewById<TextView>(R.id.Cpf)
        val tvCpfTitulo = findViewById<TextView>(R.id.Cpfvalor)

        val tvEndereco = findViewById<TextView>(R.id.Endereco)
        val tvEnderecoTitulo = findViewById<TextView>(R.id.Enderecotitulo)

        val tvOcupacao = findViewById<TextView>(R.id.Ocupacao)
        val tvOcupacaoTitulo = findViewById<TextView>(R.id.Ocupacaotitulo)

        val tvEmail = findViewById<TextView>(R.id.email)
        val tvEmailTitulo = findViewById<TextView>(R.id.emailtitulo)

        val tvSenha = findViewById<TextView>(R.id.senha)
        val tvSenhaTitulo = findViewById<TextView>(R.id.senhatitulo)

        val tvCelular = findViewById<TextView>(R.id.celular)
        val tvCelularTitulo = findViewById<TextView>(R.id.celulartitulo)

        // ---------- SETAS (CLIQUE) ----------
        val setaNome = findViewById<ImageView>(R.id.SetaNome)
        val setaCpf = findViewById<ImageView>(R.id.SetaCpf)
        val setaEndereco = findViewById<ImageView>(R.id.SetaEndereco)
        val setaOcupacao = findViewById<ImageView>(R.id.SetaOcupacao)
        val setaEmail = findViewById<ImageView>(R.id.Setaemail)
        val setaSenha = findViewById<ImageView>(R.id.Setasenha)
        val setaCelular = findViewById<ImageView>(R.id.Setacelular)
        // ---------- CLIQUES DAS SETAS (CHAMANDO O POPUP) ----------

        setaNome.setOnClickListener {
            abrirPopupEdicao(
                titulo = "Alterar nome",
                valorAtual = tvNome.text.toString()
            ) { novoValor ->
                tvNome.text = novoValor
            }
        }

        setaCpf.setOnClickListener {
            abrirPopupEdicao(
                titulo = "Alterar CPF",
                valorAtual = tvCpf.text.toString()
            ) { novoValor ->
                tvCpf.text = novoValor
            }
        }

        setaEndereco.setOnClickListener {
            abrirPopupEdicao(
                titulo = "Alterar endereço",
                valorAtual = tvEndereco.text.toString()
            ) { novoValor ->
                tvEndereco.text = novoValor
            }
        }

        setaOcupacao.setOnClickListener {
            abrirPopupEdicao(
                titulo = "Alterar ocupação",
                valorAtual = tvOcupacao.text.toString()
            ) { novoValor ->
                tvOcupacao.text = novoValor
            }
        }

        setaEmail.setOnClickListener {
            abrirPopupEdicao(
                titulo = "Alterar e-mail",
                valorAtual = tvEmail.text.toString()
            ) { novoValor ->
                tvEmail.text = novoValor
            }
        }

        setaSenha.setOnClickListener {
            abrirPopupEdicao(
                titulo = "Alterar senha",
                valorAtual = tvSenha.text.toString()
            ) { novoValor ->
                tvSenha.text = novoValor
            }
        }

        setaCelular.setOnClickListener {
            abrirPopupEdicao(
                titulo = "Alterar celular",
                valorAtual = tvCelular.text.toString()
            ) { novoValor ->
                tvCelular.text = novoValor
            }
        }
    }

    private fun abrirPopupEdicao(
        titulo: String,
        valorAtual: String,
        onSalvar: (String) -> Unit
    ) {
        val view = layoutInflater.inflate(R.layout.dialog_editar_campo, null)

        val tvTitulo = view.findViewById<TextView>(R.id.tvTituloPopup)
        val etNovoValor = view.findViewById<EditText>(R.id.etNovoValor)

        tvTitulo.text = titulo
        etNovoValor.setText(valorAtual)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("Salvar") { _, _ ->
                val novoValor = etNovoValor.text.toString()
                onSalvar(novoValor)

                // Mensagem de sucesso
                Toast.makeText(this, "Campo alterado com sucesso", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
}


