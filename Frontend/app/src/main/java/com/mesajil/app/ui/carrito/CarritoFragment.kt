package com.mesajil.app.ui.carrito

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mesajil.app.databinding.FragmentCarritoBinding
import com.mesajil.app.repository.DetalleCarritoRepository
import androidx.lifecycle.lifecycleScope
import com.mesajil.app.models.response.DetalleCarritoResponse
import com.mesajil.app.preferences.SessionManager
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.models.request.DetalleCarritoRequest
import android.content.Intent
import com.mesajil.app.ui.checkout.CheckoutActivity

class CarritoFragment : Fragment() {
    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!
    private val carritoAdapter by lazy {
        CarritoAdapter(
            productos = mutableListOf(),
            onAumentar = { producto ->
                aumentarCantidad(producto)
            },
            onDisminuir = { producto ->
                disminuirCantidad(producto)
            },
            onEliminar = { producto ->
                eliminarProducto(producto)
            }
        )
    }

    private val detalleCarritoRepository = DetalleCarritoRepository()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        binding.rvCarrito.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carritoAdapter
        }
        cargarCarrito()
        binding.btnProcederPago.setOnClickListener {
            val intent = Intent(
                requireContext(),
                CheckoutActivity::class.java
            )
            startActivity(intent)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun aumentarCantidad(producto: DetalleCarritoResponse) {
        if(producto.cantidad >= producto.stock){
            Toast.makeText(
                requireContext(),
                "Stock máximo alcanzado.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        viewLifecycleOwner.lifecycleScope.launch {
            val request = DetalleCarritoRequest(
                idCarrito = producto.idCarrito,
                idProducto = producto.idProducto,
                cantidad = producto.cantidad + 1,
                precioUnitario = producto.precioUnitario
            )
            val actualizado = detalleCarritoRepository.actualizar(
                producto.idDetalleCarrito,
                request
            )
            if (actualizado) {
                cargarCarrito()
            } else {
                Toast.makeText(
                    requireContext(),
                    "No se pudo aumentar la cantidad.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun disminuirCantidad(producto: DetalleCarritoResponse) {
        if (producto.cantidad <= 1) {
            eliminarProducto(producto)
            return
        }
        viewLifecycleOwner.lifecycleScope.launch {
            val request = DetalleCarritoRequest(
                idCarrito = producto.idCarrito,
                idProducto = producto.idProducto,
                cantidad = producto.cantidad - 1,
                precioUnitario = producto.precioUnitario
            )
            val actualizado = detalleCarritoRepository.actualizar(
                producto.idDetalleCarrito,
                request
            )
            if (actualizado) {
                cargarCarrito()
            } else {
                Toast.makeText(
                    requireContext(),
                    "No se pudo disminuir la cantidad.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun cargarCarrito() {
        val sessionManager = SessionManager(requireContext())
        val idCarrito = sessionManager.obtenerIdCarrito()
        viewLifecycleOwner.lifecycleScope.launch {
            val detalles = detalleCarritoRepository.obtenerPorCarrito(idCarrito)
            if (!detalles.isNullOrEmpty()) {
                carritoAdapter.actualizarProductos(detalles)
                actualizarTotal(detalles)
                binding.btnProcederPago.isEnabled = true
            } else {
                carritoAdapter.actualizarProductos(emptyList())
                binding.btnProcederPago.isEnabled = false
                binding.txtTotal.text = "S/. 0.00"
            }
        }
    }

    private fun actualizarTotal(detalles: List<DetalleCarritoResponse>) {
        val total = detalles.sumOf { it.subtotal }
        binding.txtTotal.text = "S/. %.2f".format(total)
    }

    private fun eliminarProducto(
        producto: DetalleCarritoResponse
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar producto")
            .setMessage("¿Desea eliminar este producto del carrito?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    val eliminado = detalleCarritoRepository.eliminar(producto.idDetalleCarrito)
                    if (eliminado) {
                        cargarCarrito()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No se pudo eliminar el producto.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        cargarCarrito()
    }
}