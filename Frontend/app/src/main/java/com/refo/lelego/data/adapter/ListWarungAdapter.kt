package com.refo.lelego.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.refo.lelego.data.response.WarungDetailData
import com.refo.lelego.databinding.ItemListWarungBinding

class ListWarungAdapter : RecyclerView.Adapter<ListWarungAdapter.MyViewHolder>() {

    private val listWarung = ArrayList<WarungDetailData>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun submitList(newList: List<WarungDetailData>) {
        val diffResult = DiffUtil.calculateDiff(WarungDiffCallback(this.listWarung, newList))
        this.listWarung.clear()
        this.listWarung.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListWarungBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val warung = listWarung[position]
        holder.bind(warung)

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(warung)
        }
    }

    override fun getItemCount(): Int = listWarung.size

    class MyViewHolder(private val binding: ItemListWarungBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WarungDetailData) {
            binding.tvItemNamaWarung.text = item.nama
            binding.tvItemAlamatWarung.text = item.alamat
            binding.tvItemJamWarung.text = "${item.jamBuka} - ${item.jamTutup}"
            binding.tvItemContactWarung.text = item.noTelp
            Glide.with(itemView.context)
                .load(item.image)
                .into(binding.ivItemPhoto)
        }
    }

    class WarungDiffCallback(
        private val oldList: List<WarungDetailData>,
        private val newList: List<WarungDetailData>
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

    interface OnItemClickCallback {
        fun onItemClicked(data: WarungDetailData)
    }
}