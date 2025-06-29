package com.refo.lelego.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.refo.lelego.data.response.MenuItem
import com.refo.lelego.databinding.ItemListMenuBinding

class MenuListAdapter(
    private val onQuantityChangeListener: (MenuItem, Int) -> Unit
) : RecyclerView.Adapter<MenuListAdapter.MyViewHolder>() {

    private val listMenu = ArrayList<MenuItem>()
    private val orderedQuantities = mutableMapOf<String, Int>()

    fun submitList(newList: List<MenuItem?>?) {
        val filteredList = newList?.filterNotNull() ?: emptyList()
        val diffResult = DiffUtil.calculateDiff(MenuDiffCallback(this.listMenu, filteredList))
        this.listMenu.clear()
        this.listMenu.addAll(filteredList)
        diffResult.dispatchUpdatesTo(this)
        filteredList.forEach { item ->
            if (!orderedQuantities.containsKey(item.id.toString())) {
                orderedQuantities[item.id.toString()] = 0
            }
        }
    }

    fun updateQuantity(menuItemId: String, quantity: Int) {
        if (orderedQuantities.containsKey(menuItemId)) {
            orderedQuantities[menuItemId] = quantity
            val position = listMenu.indexOfFirst { it.id.toString() == menuItemId }
            if (position != -1) {
                notifyItemChanged(position)
            }
        }
    }

    fun getOrderedItems(): Map<String, Int> {
        return orderedQuantities.filter { it.value > 0 }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuItem = listMenu[position]
        holder.bind(menuItem, orderedQuantities[menuItem.id.toString()] ?: 0, onQuantityChangeListener)
    }

    override fun getItemCount(): Int = listMenu.size

    class MyViewHolder(private val binding: ItemListMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuItem, currentQuantity: Int, onQuantityChangeListener: (MenuItem, Int) -> Unit) {
            binding.tvMenuName.text = item.namaMenu
            binding.tvMenuPrice.text = "Rp ${item.harga ?: 0}"
            binding.tvMenuAvailability.text = if (item.tersedia == true) "Tersedia" else "Tidak Tersedia"

            Glide.with(itemView.context)
                .load(item.gambar)
                .into(binding.ivMenuPhoto)

            binding.tvMenuQuantity.text = currentQuantity.toString()

            binding.btnIncreaseQuantity.setOnClickListener {
                val newQuantity = currentQuantity + 1
                onQuantityChangeListener.invoke(item, newQuantity)
            }

            binding.btnDecreaseQuantity.setOnClickListener {
                val newQuantity = if (currentQuantity > 0) currentQuantity - 1 else 0
                onQuantityChangeListener.invoke(item, newQuantity)
            }

            if (currentQuantity > 0) {
                binding.tvMenuQuantity.visibility = View.VISIBLE
                binding.btnDecreaseQuantity.visibility = View.VISIBLE
            } else {
                binding.tvMenuQuantity.visibility = View.GONE
                binding.btnDecreaseQuantity.visibility = View.GONE
            }

            binding.btnIncreaseQuantity.setOnClickListener {
                val newQuantity = currentQuantity + 1
                onQuantityChangeListener.invoke(item, newQuantity)
            }

            binding.btnDecreaseQuantity.setOnClickListener {
                val newQuantity = if (currentQuantity > 0) currentQuantity - 1 else 0
                onQuantityChangeListener.invoke(item, newQuantity)
            }

            binding.btnIncreaseQuantity.isEnabled = item.tersedia == true
            binding.btnDecreaseQuantity.isEnabled = item.tersedia == true && currentQuantity > 0

        }
    }

    class MenuDiffCallback(
        private val oldList: List<MenuItem>,
        private val newList: List<MenuItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].namaMenu == newList[newItemPosition].namaMenu &&
                    oldList[oldItemPosition].harga == newList[newItemPosition].harga &&
                    oldList[oldItemPosition].tersedia == newList[newItemPosition].tersedia
        }
    }
}