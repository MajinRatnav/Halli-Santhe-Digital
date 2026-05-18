package com.halliSanthe.ui.buyer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.halliSanthe.R
import com.halliSanthe.data.model.Product
import com.halliSanthe.databinding.ItemProductGridBinding

class ProductAdapter(
    private val onItemClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductGridBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(
        private val binding: ItemProductGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                tvProductName.text = product.name
                tvProductPrice.text = "₹${String.format("%.0f", product.price)}"
                tvArtisanName.text = product.artisanName
                tvVillage.text = product.village
                tvCategory.text = product.category

                Glide.with(itemView.context)
                    .load(product.imageUrl)
                    .placeholder(R.drawable.ic_product_placeholder)
                    .error(R.drawable.ic_product_placeholder)
                    .centerCrop()
                    .into(ivProduct)

                chipAvailability.text = if (product.isAvailable) "Available" else "Sold Out"
                chipAvailability.setChipBackgroundColorResource(
                    if (product.isAvailable) R.color.green_light else R.color.red_light
                )

                root.setOnClickListener { onItemClick(product) }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
    }
}
