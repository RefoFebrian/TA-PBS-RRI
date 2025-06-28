package com.refo.lelego.data.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.refo.lelego.data.response.DataItem
import com.refo.lelego.databinding.ItemListWarungBinding

class ListWarungAdapter : RecyclerView.Adapter<ListWarungAdapter.MyViewHolder>() {

    private val listWarung = ArrayList<DataItem>()

    fun submitList(newList: List<DataItem>) {
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
    }

    override fun getItemCount(): Int = listWarung.size

    class MyViewHolder(private val binding: ItemListWarungBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItem) {
            binding.tvItemNamaWarung.text = item.nama
            binding.tvItemAlamatWarung.text = item.alamat
            binding.tvItemJamWarung.text = "${item.jamBuka} - ${item.jamTutup}"
            binding.tvItemContactWarung.text = item.noTelp
            Glide.with(itemView.context)
                .load(item.image)
                .into(binding.ivItemPhoto)

            itemView.setOnClickListener {
                // ... (kode onClickListener Anda jika ada)
            }
        }
    }

    class WarungDiffCallback(
        private val oldList: List<DataItem>,
        private val newList: List<DataItem>
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