package com.halliSanthe.ui.artisan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.halliSanthe.R
import com.halliSanthe.data.model.Product
import com.halliSanthe.databinding.ItemArtisanProductBinding

class ArtisanProductAdapter(
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : ListAdapter<Product, ArtisanProductAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArtisanProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemArtisanProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                tvItemName.text = product.name
                tvItemPrice.text = "₹${String.format("%.0f", product.price)}"
                tvItemCategory.text = product.category
                tvItemVillage.text = product.village
                switchAvailable.isChecked = product.isAvailable

                Glide.with(itemView.context)
                    .load(product.imageUrl)
                    .placeholder(R.drawable.ic_product_placeholder)
                    .centerCrop()
                    .into(ivItemThumb)

                btnEdit.setOnClickListener { onEditClick(product) }
                btnDelete.setOnClickListener { onDeleteClick(product) }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(old: Product, new: Product) = old.id == new.id
        override fun areContentsTheSame(old: Product, new: Product) = old == new
    }
}
