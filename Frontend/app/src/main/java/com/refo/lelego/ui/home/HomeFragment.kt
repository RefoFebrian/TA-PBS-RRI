package com.refo.lelego.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.refo.lelego.data.ResultAnalyze
import com.refo.lelego.data.adapter.ListWarungAdapter
import com.refo.lelego.data.response.WarungDetailData // <-- UBAH IMPORT DARI DataItem KE WarungDetailData
// Hapus import DataItem jika masih ada
import com.refo.lelego.databinding.FragmentHomeBinding
import com.refo.lelego.ui.ViewModelFactory
import com.refo.lelego.ui.detail.DetailWarungActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var warungAdapter: ListWarungAdapter

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        warungAdapter = ListWarungAdapter()
        binding.rvWarung.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWarung.adapter = warungAdapter

        warungAdapter.setOnItemClickCallback(object : ListWarungAdapter.OnItemClickCallback {
            override fun onItemClicked(data: WarungDetailData) { // <-- UBAH TIPE PARAMETER DI SINI
                val intent = Intent(requireContext(), DetailWarungActivity::class.java)
                intent.putExtra(DetailWarungActivity.EXTRA_WARUNG_ID, data.id) // Kirim ID saja
                startActivity(intent)
            }
        })

        getData()
    }

    private fun getData() {
        viewModel.warung.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultAnalyze.Loading -> {
                    binding.progressBarHome.visibility = View.VISIBLE
                }
                is ResultAnalyze.Success -> {
                    val warungList = result.data
                    binding.progressBarHome.visibility = View.GONE
                    warungAdapter.submitList(warungList)
                }
                is ResultAnalyze.Error -> {
                    Toast.makeText(requireContext(), "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    binding.progressBarHome.visibility = View.GONE
                }
            }
        }
    }
}