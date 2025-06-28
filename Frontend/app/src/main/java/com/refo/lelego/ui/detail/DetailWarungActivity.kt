package com.refo.lelego.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.refo.lelego.data.ResultAnalyze
import com.refo.lelego.databinding.ActivityDetailWarungBinding
import com.refo.lelego.ui.ViewModelFactory
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.refo.lelego.data.adapter.MenuListAdapter

class DetailWarungActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailWarungBinding
    private val viewModel by viewModels<DetailWarungViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var menuAdapter: MenuListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWarungBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Warung"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        menuAdapter = MenuListAdapter()
        binding.rvWarungMenu.layoutManager = LinearLayoutManager(this)
        binding.rvWarungMenu.adapter = menuAdapter

        val warungId = intent.getIntExtra(EXTRA_WARUNG_ID, -1)

        if (warungId != -1) {
            viewModel.getWarungDetail(warungId).observe(this) { result ->
                when (result) {
                    is ResultAnalyze.Loading -> {
                        binding.progressBarDetail.visibility = View.VISIBLE
                    }
                    is ResultAnalyze.Success -> {
                        val warung = result.data
                        binding.progressBarDetail.visibility = View.GONE
                        if (warung != null) {
                            binding.tvWarungName.text = warung.nama
                            binding.tvWarungAddress.text = warung.alamat
                            binding.tvWarungHours.text = "${warung.jamBuka} - ${warung.jamTutup}"
                            binding.tvWarungContact.text = warung.noTelp
                            Glide.with(this)
                                .load(warung.image)
                                .into(binding.ivWarungPhoto)

                            menuAdapter.submitList(warung.menu)
                        } else {
                            Toast.makeText(this, "Data warung tidak ditemukan", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    is ResultAnalyze.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                        binding.progressBarDetail.visibility = View.GONE
                        finish()
                    }
                }
            }
        } else {
            Toast.makeText(this, "ID warung tidak valid", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_WARUNG_ID = "extra_warung_id"
    }
}