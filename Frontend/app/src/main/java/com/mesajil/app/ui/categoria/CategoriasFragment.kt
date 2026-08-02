package com.mesajil.app.ui.categoria

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.R
import com.mesajil.app.adapters.CategoriaAdapter
import com.mesajil.app.adapters.ProductoAdapter
import com.mesajil.app.databinding.FragmentCategoriasBinding
import com.mesajil.app.models.Producto
import com.mesajil.app.models.request.DetalleCarritoRequest
import com.mesajil.app.preferences.SessionManager
import com.mesajil.app.repository.CategoriaRepository
import com.mesajil.app.repository.DetalleCarritoRepository
import com.mesajil.app.repository.ProductoRepository
import com.mesajil.app.ui.producto.DetalleProductoActivity
import kotlinx.coroutines.launch

class CategoriasFragment : Fragment() {
    private var _binding: FragmentCategoriasBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private val categoriaRepository = CategoriaRepository()
    private val productoRepository = ProductoRepository()
    private val detalleCarritoRepository = DetalleCarritoRepository()
    private var listaProductos: List<Producto> = emptyList()
    private lateinit var productoAdapter: ProductoAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriasBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())
        configurarProductos()
        return binding.root
    }

    private fun configurarProductos() {
        productoAdapter = ProductoAdapter(
            listaProductos = emptyList(),
            onProductoClick = { producto ->
                val intent = Intent(
                    requireContext(),
                    DetalleProductoActivity::class.java
                )
                intent.putExtra(DetalleProductoActivity.EXTRA_ID, producto.idProducto)
                intent.putExtra(DetalleProductoActivity.EXTRA_NOMBRE, producto.nombre)
                intent.putExtra(DetalleProductoActivity.EXTRA_DESCRIPCION, producto.descripcion)
                intent.putExtra(DetalleProductoActivity.EXTRA_PRECIO, producto.precio)
                intent.putExtra(DetalleProductoActivity.EXTRA_STOCK, producto.stock)
                intent.putExtra(DetalleProductoActivity.EXTRA_IMAGEN, producto.urlImagen)
                intent.putExtra(DetalleProductoActivity.EXTRA_MARCA, producto.marca)
                intent.putExtra(DetalleProductoActivity.EXTRA_MODELO, producto.modelo)
                startActivity(intent)
            },
            onAgregarClick = { producto ->
                agregarAlCarrito(producto)
            }
        )
        binding.rvProductosCategoria.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProductosCategoria.adapter = productoAdapter
        cargarDatos()
    }

    private fun cargarDatos() {
        viewLifecycleOwner.lifecycleScope.launch {
            val categorias = categoriaRepository.obtenerCategorias()
                .filter { it.estado }
            listaProductos = productoRepository.obtenerProductos()
            val categoriaAdapter = CategoriaAdapter(
                categorias = categorias,
                onCategoriaClick = { categoria ->
                    binding.txtTituloProductos.text = "${categoria.nombre}"
                    val productosFiltrados = listaProductos.filter { producto ->
                        producto.idCategoria == categoria.idCategoria
                    }
                    productoAdapter.actualizarLista(productosFiltrados)
                }
            )
            binding.rvCategorias.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.rvCategorias.adapter = categoriaAdapter
        }
    }

    private fun agregarAlCarrito(producto: Producto) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (producto.stock <= 0) {
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
                if (existente.cantidad >= producto.stock) {
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
                        requireContext(),
                        "Error al actualizar el carrito.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}