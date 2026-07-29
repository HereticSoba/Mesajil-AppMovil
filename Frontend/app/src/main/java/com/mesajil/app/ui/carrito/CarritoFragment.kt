package com.mesajil.app.ui.carrito

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.databinding.FragmentCarritoBinding

class CarritoFragment : Fragment() {
    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!
    private lateinit var carritoAdapter: CarritoAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        configurarRecyclerView()
        actualizarTotal()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configurarRecyclerView() {
        carritoAdapter = CarritoAdapter(CarritoManager.obtenerProductos()) {
            actualizarTotal()
        }
        binding.rvCarrito.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carritoAdapter
        }
    }

    private fun actualizarTotal() {
        binding.txtTotal.text = "S/. %.2f".format(CarritoManager.calcularTotal())
    }
}