package com.halliSanthe.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.halliSanthe.R
import com.halliSanthe.data.model.Product
import com.halliSanthe.databinding.FragmentHomeBinding
import com.halliSanthe.ui.buyer.ProductAdapter
import com.halliSanthe.viewmodel.ProductViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by activityViewModels()
    private lateinit var productAdapter: ProductAdapter

    private val categories = listOf("All", "Toys", "Pottery", "Textiles", "Jewellery", "Food", "Crafts")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        setupCategoryChips()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter { product ->
            viewModel.selectProduct(product)
            findNavController().navigate(R.id.action_home_to_productDetail)
        }
        binding.recyclerViewProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchProducts(query ?: "")
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchProducts(newText ?: "")
                return true
            }
        })
    }

    private fun setupCategoryChips() {
        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                isChecked = category == "All"
                setChipBackgroundColorResource(R.color.chip_selector)
                setTextColor(resources.getColorStateList(R.color.chip_text_selector, null))
                setOnClickListener {
                    viewModel.filterByCategory(category)
                    // Uncheck others
                    binding.chipGroupCategories.clearCheck()
                    this.isChecked = true
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.submitList(products)
            updateEmptyState(products)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                com.google.android.material.snackbar.Snackbar.make(
                    binding.root, it, com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                ).show()
                viewModel.clearError()
            }
        }
    }

    private fun updateEmptyState(products: List<Product>) {
        val isEmpty = products.isEmpty()
        binding.emptyStateLayout.isVisible = isEmpty
        binding.recyclerViewProducts.isVisible = !isEmpty
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
