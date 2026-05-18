package com.halliSanthe.ui.artisan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.halliSanthe.R
import com.halliSanthe.data.model.Product
import com.halliSanthe.databinding.FragmentArtisanDashboardBinding
import com.halliSanthe.viewmodel.ProductViewModel

class ArtisanDashboardFragment : Fragment() {

    private var _binding: FragmentArtisanDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by activityViewModels()

    // For demo - in production, get from Firebase Auth
    private val currentArtisanId = "demo_artisan_001"

    private lateinit var artisanProductAdapter: ArtisanProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentArtisanDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        observeProducts()
    }

    private fun setupRecyclerView() {
        artisanProductAdapter = ArtisanProductAdapter(
            onEditClick = { product ->
                viewModel.selectProduct(product)
                findNavController().navigate(R.id.action_dashboard_to_addProduct)
            },
            onDeleteClick = { product ->
                showDeleteConfirmation(product)
            }
        )
        binding.rvArtisanProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = artisanProductAdapter
        }
    }

    private fun setupFab() {
        binding.fabAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_addProduct)
        }
    }

    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) { allProducts ->
            // Filter products by current artisan
            val myProducts = allProducts.filter { it.artisanId == currentArtisanId }
            artisanProductAdapter.submitList(myProducts)
            binding.tvProductCount.text = "${myProducts.size} Products Listed"
            binding.emptyArtisanLayout.visibility =
                if (myProducts.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showDeleteConfirmation(product: Product) {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to remove \"${product.name}\" from your listings?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteProduct(product)
                Snackbar.make(binding.root, "${product.name} removed.", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
