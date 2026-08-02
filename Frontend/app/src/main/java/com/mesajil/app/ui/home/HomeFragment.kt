package com.mesajil.app.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mesajil.app.adapters.ProductoAdapter
import com.mesajil.app.databinding.FragmentHomeBinding
import com.mesajil.app.preferences.SessionManager
import androidx.recyclerview.widget.GridLayoutManager
import android.content.Intent
import com.mesajil.app.ui.producto.DetalleProductoActivity
import androidx.lifecycle.lifecycleScope
import com.mesajil.app.models.request.DetalleCarritoRequest
import com.mesajil.app.repository.ProductoRepository
import kotlinx.coroutines.launch
import com.mesajil.app.repository.DetalleCarritoRepository

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private val productoRepository = ProductoRepository()
    private val detalleCarritoRepository = DetalleCarritoRepository()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())
        val nombre = sessionManager.obtenerNombre()
        binding.txtSaludo.text = "Hola, $nombre"
        configurarRecycler()
        return binding.root
    }

    private fun configurarRecycler() {
        viewLifecycleOwner.lifecycleScope.launch {
            val productos = productoRepository.obtenerProductos()
            val adapter = ProductoAdapter(
                listaProductos = productos,
                onProductoClick = { producto ->
                    val intent = Intent(requireContext(), DetalleProductoActivity::class.java)
                    intent.putExtra(DetalleProductoActivity.EXTRA_ID, producto.idProducto)
                    intent.putExtra(DetalleProductoActivity.EXTRA_NOMBRE, producto.nombre)
                    intent.putExtra(DetalleProductoActivity.EXTRA_DESCRIPCION, producto.descripcion)
                    intent.putExtra(DetalleProductoActivity.EXTRA_PRECIO, producto.precio)
                    intent.putExtra(DetalleProductoActivity.EXTRA_STOCK, producto.stock)
                    startActivity(intent)
                },
                onAgregarClick = { producto ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        if(producto.stock <= 0){
                            Toast.makeText(
                                requireContext(),
                                "Producto sin stock.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        }
                        val idCarrito = sessionManager.obtenerIdCarrito()
                        if (idCarrito == 0) {
                            Toast.makeText(
                                requireContext(),
                                "No se encontró el carrito.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        }
                        val existente = detalleCarritoRepository.obtenerPorCarritoYProducto(
                            idCarrito,
                            producto.idProducto
                        )
                        if (existente == null) {
                            val request = DetalleCarritoRequest(
                                idCarrito = idCarrito,
                                idProducto = producto.idProducto,
                                cantidad = 1,
                                precioUnitario = producto.precio
                            )
                            val resultado = detalleCarritoRepository.crear(request)
                            if (resultado != null) {
                                Toast.makeText(
                                    requireContext(),
                                    "${producto.nombre} agregado al carrito.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Error al agregar el producto.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            if(existente.cantidad >= producto.stock){
                                Toast.makeText(
                                    requireContext(),
                                    "Stock máximo alcanzado.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@launch
                            }
                            val request = DetalleCarritoRequest(
                                idCarrito = existente.idCarrito,
                                idProducto = existente.idProducto,
                                cantidad = existente.cantidad + 1,
                                precioUnitario = existente.precioUnitario
                            )
                            val actualizado = detalleCarritoRepository.actualizar(
                                existente.idDetalleCarrito,
                                request
                            )
                            if (actualizado) {
                                Toast.makeText(
                                    requireContext(),
                                    "Cantidad actualizada.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(), "Error al actualizar el carrito.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            )
            binding.rvProductos.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvProductos.adapter = adapter
        }
    }
}