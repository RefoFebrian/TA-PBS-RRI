package com.refo.lelego.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.refo.lelego.databinding.ActivityLoginBinding
import com.refo.lelego.ui.ViewModelFactory
import kotlinx.coroutines.launch
import com.refo.lelego.data.ResultAnalyze
import com.refo.lelego.data.pref.UserModel
import com.refo.lelego.ui.main.MainActivity
import com.refo.lelego.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBarLogin.visibility = View.VISIBLE

            viewModel.login(username, password).observe(this) { result ->
                when (result) {
                    is ResultAnalyze.Loading -> {
                        binding.progressBarLogin.visibility = View.VISIBLE
                    }

                    is ResultAnalyze.Error -> {
                        binding.progressBarLogin.visibility = View.INVISIBLE
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultAnalyze.Success -> {
                        binding.progressBarLogin.visibility = View.INVISIBLE
                        AlertDialog.Builder(this).apply {
                            setTitle("Hore")
                            setMessage("Berhasil untuk login")
                            setPositiveButton("Ok") { _, _ ->
                                val token = result.data.data?.token ?: ""
                                val userId = result.data.data?.user?.id ?: ""
                                val usernameFromApi = result.data.data?.user?.username ?: username
                                saveSession(
                                    UserModel(
                                        username = usernameFromApi,
                                        token = token,
                                        password = password,
                                        isLogin = true,
                                        userId = userId
                                    )
                                )
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }
        binding.tvRegisterNow.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveSession(session: UserModel) {
        lifecycleScope.launch {
            viewModel.saveSession(session)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ViewModelFactory.clearInstance()
            startActivity(intent)
        }
    }
}