package com.refo.lelego.ui.payment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.refo.lelego.data.response.OrderResponseData
import com.refo.lelego.data.response.WarungDetailData
import com.refo.lelego.databinding.ActivityOrderPaymentBinding
import com.refo.lelego.ui.main.MainActivity

class OrderPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val orderData = intent.getParcelableExtra<OrderResponseData>(EXTRA_ORDER_DATA)
        val warungData = intent.getParcelableExtra<WarungDetailData>(EXTRA_WARUNG_DATA)

        if (orderData != null && warungData != null) {
            binding.tvTransactionId.text = "ID Transaksi: ${orderData.transactionId ?: "N/A"}"
            binding.tvTotalBill.text = "Total Tagihan: Rp ${String.format("%,d", orderData.totalHarga?.toLong() ?: 0L)}"

            val penjualContactNumber = warungData.noTelp

            binding.btnContactWhatsapp.setOnClickListener {
                if (!penjualContactNumber.isNullOrEmpty()) {
                    val formattedNumber = penjualContactNumber.replace("[^\\d]".toRegex(), "")
                    val message = "Halo, saya telah membuat pesanan di warung Anda dengan ID Transaksi: ${orderData.transactionId}. Total tagihan saya Rp ${String.format("%,d", orderData.totalHarga?.toLong() ?: 0L)}. Mohon info langkah selanjutnya untuk pembayaran."

                    try {
                        val url = "https://api.whatsapp.com/send?phone=$formattedNumber&text=${Uri.encode(message)}"
                        val whatsappIntent = Intent(Intent.ACTION_VIEW)
                        whatsappIntent.data = Uri.parse(url)
                        startActivity(whatsappIntent)
                    } catch (e: Exception) {
                        Toast.makeText(this, "Aplikasi WhatsApp tidak ditemukan atau terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                    binding.btnBackToHome.setOnClickListener {
                        val backToMainIntent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                        startActivity(backToMainIntent)
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Nomor kontak penjual tidak tersedia.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Detail pesanan tidak ditemukan.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    companion object {
        const val EXTRA_ORDER_DATA = "extra_order_data"
        const val EXTRA_WARUNG_DATA = "extra_warung_data"
    }
}