package com.refo.lelego.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.refo.lelego.data.ResultAnalyze
import com.refo.lelego.data.adapter.MenuListAdapter
import com.refo.lelego.data.response.WarungDetailData
import com.refo.lelego.databinding.ActivityDetailWarungBinding
import com.refo.lelego.ui.ViewModelFactory
import android.widget.Toast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.refo.lelego.ui.payment.OrderPaymentActivity

class DetailWarungActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailWarungBinding
    private val viewModel by viewModels<DetailWarungViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var menuAdapter: MenuListAdapter
    private var currentWarungId: Int? = null
    private var currentWarungData: WarungDetailData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWarungBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Warung"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        menuAdapter = MenuListAdapter { menuItem, quantity ->
            viewModel.setMenuQuantity(menuItem, quantity)
        }

        binding.rvWarungMenu.layoutManager = LinearLayoutManager(this)
        binding.rvWarungMenu.adapter = menuAdapter


        val warungId = intent.getIntExtra(EXTRA_WARUNG_ID, -1)
        if (warungId != -1) {
            currentWarungId = warungId
            viewModel.getWarungDetail(warungId)
        } else {
            Toast.makeText(this, "ID warung tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.warungDetail.observe(this) { result ->
            when (result) {
                is ResultAnalyze.Loading -> {
                    binding.progressBarDetail.visibility = View.VISIBLE
                    binding.scrollView.visibility = View.GONE
                    binding.bottomOrderContainer.visibility = View.GONE
                }
                is ResultAnalyze.Success -> {
                    binding.progressBarDetail.visibility = View.GONE
                    binding.scrollView.visibility = View.VISIBLE
                    binding.bottomOrderContainer.visibility = View.VISIBLE

                    val warung = result.data
                    currentWarungData = warung
                    if (warung != null) {
                        Glide.with(this)
                            .load(warung.image)
                            .into(binding.ivWarungPhoto)

                        binding.tvWarungName.text = warung.nama
                        binding.tvWarungAddress.text = warung.alamat
                        binding.tvWarungHours.text = "${warung.jamBuka} - ${warung.jamTutup}"
                        binding.tvWarungContact.text = warung.noTelp

                        menuAdapter.submitList(warung.menu)
                    } else {
                        Toast.makeText(this, "Data warung tidak ditemukan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                is ResultAnalyze.Error -> {
                    binding.progressBarDetail.visibility = View.GONE
                    binding.scrollView.visibility = View.GONE
                    binding.bottomOrderContainer.visibility = View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        viewModel.menuQuantities.observe(this) { quantitiesMap ->
            quantitiesMap.forEach { (idKey, quantity) ->
                menuAdapter.updateQuantity(idKey, quantity)
            }
        }

        viewModel.totalPrice.observe(this) { total ->
            binding.tvTotalPrice.text = "Rp ${String.format("%,d", total)}"
            binding.btnCreateOrder.isEnabled = total > 0
        }

        binding.btnCreateOrder.setOnClickListener {
            lifecycleScope.launch {
                val userSession = viewModel.repository.getSession().first()
                val userId = userSession.userId

                if (userId.isNullOrEmpty()) {
                    Toast.makeText(this@DetailWarungActivity, "User ID tidak ditemukan. Harap login ulang.", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                if (currentWarungId == null) {
                    Toast.makeText(this@DetailWarungActivity, "ID Warung tidak valid.", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                if ((viewModel.totalPrice.value ?: 0L) <= 0) {
                    Toast.makeText(this@DetailWarungActivity, "Pilih menu yang akan dipesan.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val waktuAmbil = dateFormat.format(Date())

                viewModel.createOrder(userId, currentWarungId!!, waktuAmbil)
            }
        }

        viewModel.orderStatus.observe(this) { result ->
            when (result) {
                is ResultAnalyze.Loading -> {
                    binding.btnCreateOrder.isEnabled = false
                    binding.progressBarDetail.visibility = View.VISIBLE
                }
                is ResultAnalyze.Success -> {
                    binding.btnCreateOrder.isEnabled = true
                    val orderResponseData = result.data?.data
                    if (orderResponseData != null && currentWarungData != null) {
                        val intent = Intent(this@DetailWarungActivity, OrderPaymentActivity::class.java)
                        intent.putExtra(OrderPaymentActivity.EXTRA_ORDER_DATA, orderResponseData)
                        intent.putExtra(OrderPaymentActivity.EXTRA_WARUNG_DATA, currentWarungData)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal mendapatkan detail pesanan atau data warung.", Toast.LENGTH_LONG).show()
                    }
                    currentWarungData?.menu?.filterNotNull()?.forEach { menuItem ->
                        viewModel.setMenuQuantity(menuItem, 0)
                    }
                }
                is ResultAnalyze.Error -> {
                    binding.btnCreateOrder.isEnabled = true
                    Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                }
            }
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