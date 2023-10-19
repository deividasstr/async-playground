package com.deividasstr.asyncplayground

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.deividasstr.asyncplayground.databinding.FragmentSecondBinding
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by viewModels<FirstViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiStateFlow.collectInViewLifecycle(::handleProduct)
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun handleProduct(product: Product) {
        println(product)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun <T : Any> StateFlow<T>.collectInViewLifecycle(consumer: FlowCollector<T>) {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                this@with.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    collect(consumer)
                }
            }
        }
    }
}