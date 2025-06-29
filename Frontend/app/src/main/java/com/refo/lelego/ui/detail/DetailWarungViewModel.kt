package com.refo.lelego.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.refo.lelego.data.ResultAnalyze
import com.refo.lelego.data.UserRepository
import com.refo.lelego.data.response.MenuItem
import com.refo.lelego.data.response.OrderRequest
import com.refo.lelego.data.response.OrderResponse
import com.refo.lelego.data.response.WarungDetailData
import kotlinx.coroutines.launch

class DetailWarungViewModel(val repository: UserRepository) : ViewModel() {

    private val _warungDetail = MutableLiveData<ResultAnalyze<WarungDetailData>>()
    val warungDetail: LiveData<ResultAnalyze<WarungDetailData>> = _warungDetail

    private val _menuQuantities = MutableLiveData<MutableMap<String, Int>>(mutableMapOf())
    val menuQuantities: LiveData<MutableMap<String, Int>> = _menuQuantities


    private val _orderStatus = MutableLiveData<ResultAnalyze<OrderResponse>>()
    val orderStatus: LiveData<ResultAnalyze<OrderResponse>> = _orderStatus


    val orderedMenuItems: LiveData<List<Pair<MenuItem, Int>>> = menuQuantities.map { quantitiesMap ->
        val currentWarung = (warungDetail.value as? ResultAnalyze.Success)?.data
        val allMenuItems = currentWarung?.menu?.filterNotNull() ?: emptyList()

        allMenuItems.mapNotNull { menuItem ->
            val quantity = quantitiesMap[menuItem.id.toString()] ?: 0
            if (quantity > 0) menuItem to quantity else null
        }
    }

    val totalPrice: LiveData<Long> = orderedMenuItems.map { orderedList ->
        orderedList.sumOf { (menuItem, quantity) ->
            (menuItem.harga?.toLong() ?: 0L) * quantity
        }
    }


    fun getWarungDetail(id: Int) {
        viewModelScope.launch {
            repository.getWarungDetail(id).collect { result ->
                _warungDetail.postValue(result)
                if (result is ResultAnalyze.Success) {
                    val initialQuantities = mutableMapOf<String, Int>()
                    result.data?.menu?.filterNotNull()?.forEach { menuItem ->
                        initialQuantities[menuItem.id.toString()] = 0
                    }
                    _menuQuantities.postValue(initialQuantities)
                }
            }
        }
    }


    fun setMenuQuantity(menuItem: MenuItem, quantity: Int) {
        val currentQuantities = _menuQuantities.value ?: mutableMapOf()
        currentQuantities[menuItem.id.toString()] = quantity
        _menuQuantities.postValue(currentQuantities)
    }

    fun createOrder(userId: String, warungId: Int, waktuAmbil: String) {
        _orderStatus.postValue(ResultAnalyze.Loading)
        viewModelScope.launch {
            try {
                val items = orderedMenuItems.value?.map { (menuItem, quantity) ->
                    OrderRequest.Item(menuId = menuItem.id, jumlah = quantity)
                } ?: emptyList()

                if (items.isEmpty()) {
                    _orderStatus.postValue(ResultAnalyze.Error("Tidak ada menu yang dipesan."))
                    return@launch
                }

                val orderRequest = OrderRequest(
                    userId = userId,
                    warungId = warungId,
                    waktuAmbil = waktuAmbil,
                    items = items
                )
                val responseResult = repository.createOrder(orderRequest)
                _orderStatus.postValue(responseResult)
            } catch (e: Exception) {
                _orderStatus.postValue(ResultAnalyze.Error(e.message ?: "Gagal membuat pesanan."))
            }
        }
    }
}