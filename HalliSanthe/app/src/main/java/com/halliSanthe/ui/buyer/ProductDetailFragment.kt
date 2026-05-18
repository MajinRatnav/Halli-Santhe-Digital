package com.halliSanthe.ui.buyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.halliSanthe.R
import com.halliSanthe.data.model.Message
import com.halliSanthe.data.model.MessageStatus
import com.halliSanthe.databinding.FragmentProductDetailBinding
import com.halliSanthe.data.repository.Result
import com.halliSanthe.viewmodel.ProductViewModel

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedProduct.observe(viewLifecycleOwner) { product ->
            product ?: return@observe
            bindProduct(product)

            binding.btnCheckAvailability.setOnClickListener {
                showInquiryDialog(product.id, product.name, product.artisanId)
            }
        }

        viewModel.operationResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success<*> -> {
                    Snackbar.make(binding.root, "✅ Inquiry sent! The artisan will get back to you.", Snackbar.LENGTH_LONG).show()
                }
                is Result.Error -> {
                    Snackbar.make(binding.root, "❌ Failed: ${result.exception.message}", Snackbar.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    private fun bindProduct(product: com.halliSanthe.data.model.Product) {
        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "₹${String.format("%.0f", product.price)}"
            tvProductDescription.text = product.description
            tvArtisanName.text = product.artisanName
            tvVillage.text = "📍 ${product.village}"
            tvCategory.text = product.category

            chipAvailability.text = if (product.isAvailable) "Available" else "Sold Out"
            chipAvailability.setChipBackgroundColorResource(
                if (product.isAvailable) R.color.green_light else R.color.red_light
            )

            btnCheckAvailability.isEnabled = product.isAvailable
            btnCheckAvailability.text = if (product.isAvailable) "Check Availability / Enquire" else "Currently Unavailable"

            Glide.with(requireContext())
                .load(product.imageUrl)
                .placeholder(R.drawable.ic_product_placeholder)
                .error(R.drawable.ic_product_placeholder)
                .into(ivProductDetail)
        }
    }

    private fun showInquiryDialog(productId: String, productName: String, artisanId: String) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_inquiry, null)

        val etBuyerName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etBuyerName)
        val etBuyerPhone = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etBuyerPhone)
        val etMessage = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etMessage)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Send Inquiry")
            .setView(dialogView)
            .setPositiveButton("Send") { _, _ ->
                val buyerName = etBuyerName.text.toString().trim()
                val buyerPhone = etBuyerPhone.text.toString().trim()
                val messageText = etMessage.text.toString().trim()

                if (buyerName.isBlank() || buyerPhone.isBlank()) {
                    Snackbar.make(binding.root, "Please fill in your name and phone number.", Snackbar.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val message = Message(
                    productId = productId,
                    productName = productName,
                    buyerName = buyerName,
                    buyerPhone = buyerPhone,
                    message = messageText.ifBlank { "I am interested in this product. Please contact me." },
                    artisanId = artisanId,
                    status = MessageStatus.PENDING
                )
                viewModel.sendInquiry(message)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
