package com.mesajil.app.ui.carrito

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.databinding.FragmentCarritoBinding
import com.mesajil.app.repository.DetalleCarritoRepository
import androidx.lifecycle.lifecycleScope
import com.mesajil.app.models.response.DetalleCarritoResponse
import com.mesajil.app.preferences.SessionManager
import kotlinx.coroutines.launch
import android.util.Log

class CarritoFragment : Fragment() {
    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!
    private lateinit var carritoAdapter: CarritoAdapter
    private val detalleCarritoRepository = DetalleCarritoRepository()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        cargarCarrito()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cargarCarrito() {
        val sessionManager = SessionManager(requireContext())
        val idCarrito = sessionManager.obtenerIdCarrito()
        Log.d("CarritoFragment", "IdCarrito: $idCarrito")
        viewLifecycleOwner.lifecycleScope.launch {
            val detalles = detalleCarritoRepository.obtenerPorCarrito(idCarrito)
            Log.d("CarritoFragment", "Respuesta: $detalles")
            if (detalles != null) {
                Log.d("CarritoFragment", "Cantidad de productos: ${detalles.size}")
                carritoAdapter = CarritoAdapter(detalles.toMutableList()) {
                    actualizarTotal(detalles)
                }
                binding.rvCarrito.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = carritoAdapter
                }
                actualizarTotal(detalles)
            }else{
                binding.txtTotal.text = "S/. 0.00"
            }
        }
    }

    private fun actualizarTotal(detalles: List<DetalleCarritoResponse>) {
        val total = detalles.sumOf { it.subtotal }
        binding.txtTotal.text = "S/. %.2f".format(total)
    }

    override fun onResume() {
        super.onResume()
        cargarCarrito()
    }
}