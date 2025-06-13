package com.refo.lelego.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.refo.lelego.data.ResultAnalyze
import com.refo.lelego.databinding.ActivityRegisterBinding
import com.refo.lelego.ui.ViewModelFactory
import com.refo.lelego.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setupAction() {
        binding.etRegisterEmail.addTextChangedListener(ClearErrorTextWatcher(binding.tilRegisterEmail))
        binding.etRegisterUsername.addTextChangedListener(ClearErrorTextWatcher(binding.tilRegisterUsername))
        binding.etRegisterPassword.addTextChangedListener(ClearErrorTextWatcher(binding.tilRegisterPassword))
        binding.etRegisterConfirmPassword.addTextChangedListener(ClearErrorTextWatcher(binding.tilRegisterConfirmPassword))

        binding.btnRegister.setOnClickListener {
            if (!validateForm()) {
                return@setOnClickListener
            }

            val email = binding.etRegisterEmail.text.toString().trim()
            val username = binding.etRegisterUsername.text.toString().trim()
            val password = binding.etRegisterPassword.text.toString().trim()
            val confirmPassword = binding.etRegisterConfirmPassword.text.toString().trim() // Meskipun tidak digunakan di register API, ini sudah divalidasi
            val role = "pembeli"

            viewModel.register(email, username, password, role).observe(this) { result ->
                when (result) {
                    is ResultAnalyze.Error -> {
                        binding.progressBarSignup.visibility = View.INVISIBLE
                        val error = result.error
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultAnalyze.Loading -> {
                        binding.progressBarSignup.visibility = View.VISIBLE
                    }

                    is ResultAnalyze.Success -> {
                        binding.progressBarSignup.visibility = View.INVISIBLE
                        val apiMessage = result.data.metadata?.message ?: "Registrasi Berhasil"
                        AlertDialog.Builder(this).apply {
                            setTitle("Hore")
                            setMessage(apiMessage)
                            setPositiveButton("OK") { _, _ ->
                                val intent = Intent(context, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }

        binding.tvLoginNow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateForm(): Boolean {
        val email = binding.etRegisterEmail.text.toString().trim()
        val username = binding.etRegisterUsername.text.toString().trim()
        val password = binding.etRegisterPassword.text.toString().trim()
        val confirmPassword = binding.etRegisterConfirmPassword.text.toString().trim()

        var isValid = true

        if (email.isEmpty()) {
            binding.tilRegisterEmail.error = "Email tidak boleh kosong"
            isValid = false
        } else if (!email.endsWith("@gmail.com")) {
            binding.tilRegisterEmail.error = "Email harus mengandung @gmail.com"
            isValid = false
        } else {
            binding.tilRegisterEmail.error = null
        }

        if (username.isEmpty()) {
            binding.tilRegisterUsername.error = "Username tidak boleh kosong"
            isValid = false
        } else {
            binding.tilRegisterUsername.error = null
        }

        if (password.isEmpty()) {
            binding.tilRegisterPassword.error = "Password tidak boleh kosong"
            isValid = false
        } else if (password.length < 8) {
            binding.tilRegisterPassword.error = "Password minimal 8 karakter"
            isValid = false
        } else {
            binding.tilRegisterPassword.error = null
        }

        if (confirmPassword.isEmpty()) {
            binding.tilRegisterConfirmPassword.error = "Konfirmasi Password tidak boleh kosong"
            isValid = false
        } else if (password != confirmPassword) {
            binding.tilRegisterConfirmPassword.error = "Password tidak cocok"
            isValid = false
        } else {
            binding.tilRegisterConfirmPassword.error = null
        }
        return isValid
    }

    private class ClearErrorTextWatcher(private val textInputLayout: com.google.android.material.textfield.TextInputLayout) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (textInputLayout.error != null) {
                textInputLayout.error = null
            }
        }
        override fun afterTextChanged(s: Editable?) {}
    }
}