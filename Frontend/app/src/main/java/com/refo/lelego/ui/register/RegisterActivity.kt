package com.refo.lelego.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.refo.lelego.R
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
        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val username = binding.etRegisterUsername.text.toString()
            val password = binding.etRegisterPassword.text.toString()
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
                        val apiMessage = result.data.metadata?.message ?: "Oke"
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
    }
}