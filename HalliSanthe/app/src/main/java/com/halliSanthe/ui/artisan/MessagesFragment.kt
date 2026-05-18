package com.halliSanthe.ui.artisan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.halliSanthe.databinding.FragmentMessagesBinding
import com.halliSanthe.viewmodel.ProductViewModel

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by activityViewModels()

    private val currentArtisanId = "demo_artisan_001"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.loadMessagesForArtisan(currentArtisanId)
        observeMessages()
    }

    private fun setupRecyclerView() {
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = MessageAdapter()
        }
    }

    private fun observeMessages() {
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            (binding.rvMessages.adapter as MessageAdapter).submitList(messages)
            binding.emptyMessagesLayout.isVisible = messages.isEmpty()
            binding.rvMessages.isVisible = messages.isNotEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
