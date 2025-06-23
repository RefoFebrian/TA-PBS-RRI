package com.refo.lelego.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.refo.lelego.R
import com.refo.lelego.databinding.ActivityMainBinding
import com.refo.lelego.ui.ViewModelFactory
import com.refo.lelego.ui.home.HomeFragment
import com.refo.lelego.ui.login.LoginActivity
import com.refo.lelego.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) {
            if (!it.isLogin) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        setSupportActionBar(binding.mainToolbar)

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    binding.mainToolbar.title = ""
                    binding.appBarLayout.visibility = View.GONE
                    true
                }

                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    binding.mainToolbar.title = "Profile"
                    binding.appBarLayout.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_home
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}