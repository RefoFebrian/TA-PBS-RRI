package com.refo.lelego.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.refo.lelego.data.response.MenuItem
import com.refo.lelego.databinding.ItemListMenuBinding

class MenuListAdapter : RecyclerView.Adapter<MenuListAdapter.MyViewHolder>() {

    private val listMenu = ArrayList<MenuItem>()

    fun submitList(newList: List<MenuItem?>?) {
        val filteredList = newList?.filterNotNull() ?: emptyList()
        val diffResult = DiffUtil.calculateDiff(MenuDiffCallback(this.listMenu, filteredList))
        this.listMenu.clear()
        this.listMenu.addAll(filteredList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuItem = listMenu[position]
        holder.bind(menuItem)
    }

    override fun getItemCount(): Int = listMenu.size

    class MyViewHolder(private val binding: ItemListMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuItem) {
            binding.tvMenuName.text = item.namaMenu
            binding.tvMenuPrice.text = "Rp ${item.harga ?: 0}"
            binding.tvMenuAvailability.text = if (item.tersedia == true) "Tersedia" else "Tidak Tersedia"

            Glide.with(itemView.context)
                .load(item.gambar)
                .into(binding.ivMenuPhoto)
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
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}