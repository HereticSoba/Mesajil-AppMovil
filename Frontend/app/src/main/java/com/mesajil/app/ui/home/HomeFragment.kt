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
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.mesajil.app.ui.producto.DetalleProductoActivity
import androidx.lifecycle.lifecycleScope
import com.mesajil.app.models.Producto
import com.mesajil.app.models.request.DetalleCarritoRequest
import com.mesajil.app.repository.ProductoRepository
import kotlinx.coroutines.launch
import com.mesajil.app.repository.DetalleCarritoRepository
import com.mesajil.app.repository.CategoriaRepository
import com.mesajil.app.models.response.CategoriaResponse

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private val productoRepository = ProductoRepository()
    private val detalleCarritoRepository = DetalleCarritoRepository()
    private val categoriaRepository = CategoriaRepository()
    private var listaProductos: List<Producto> = emptyList()
    private var listaCategorias: List<CategoriaResponse> = emptyList()
    private lateinit var productoAdapter: ProductoAdapter
    private var marcaSeleccionada: String? = null
    private var categoriaSeleccionada: Int? = null
    private var precioMinimo: Double? = null
    private var precioMaximo: Double? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.headerCategorias.txtTitulo.text = "Categorías"
        binding.headerProductos.txtTitulo.text = "Productos destacados"
        sessionManager = SessionManager(requireContext())
        val nombre = sessionManager.obtenerNombre()
        binding.txtSaludo.text = "Hola, $nombre"
        configurarRecycler()
        binding.btnFiltrar.setOnClickListener {
            mostrarDialogoFiltros()
        }
        return binding.root
    }

    private fun configurarRecycler() {
        viewLifecycleOwner.lifecycleScope.launch {
            listaProductos = productoRepository.obtenerProductos()
            listaCategorias = categoriaRepository.obtenerCategorias()
            productoAdapter = ProductoAdapter(
                listaProductos = listaProductos,
                onProductoClick = { producto ->
                    val intent = Intent(requireContext(), DetalleProductoActivity::class.java)
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
                                    requireContext(), "Error al actualizar el carrito.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            )
            binding.rvProductos.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvProductos.adapter = productoAdapter
            configurarBusqueda()
        }
    }

    private fun configurarBusqueda() {
        binding.edtBuscar.addTextChangedListener {
            aplicarFiltros()
        }
    }

    private fun aplicarFiltros() {
        val consulta = binding.edtBuscar.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val productosFiltrados = listaProductos.filter { producto ->
            val coincideBusqueda = consulta.isEmpty() ||
                    producto.nombre.contains(consulta, ignoreCase = true) ||
                    producto.modelo.contains(consulta, ignoreCase = true)
            val coincideMarca = marcaSeleccionada == null ||
                    producto.marca.equals(
                        marcaSeleccionada,
                        ignoreCase = true
                    )
            val coincideCategoria = categoriaSeleccionada == null ||
                    producto.idCategoria == categoriaSeleccionada
            val coincidePrecioMin = precioMinimo == null ||
                    producto.precio >= precioMinimo!!
            val coincidePrecioMax = precioMaximo == null ||
                    producto.precio <= precioMaximo!!

            coincideBusqueda && coincideMarca && coincideCategoria && coincidePrecioMin && coincidePrecioMax
        }
        productoAdapter.actualizarLista(productosFiltrados)
    }

    private fun mostrarDialogoFiltros() {
        val dialogBinding =
            com.mesajil.app.databinding.DialogFiltroProductosBinding.inflate(layoutInflater)
        val marcas = mutableListOf("Todas")
        marcas.addAll(
            listaProductos
                .map { it.marca }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()
        )
        val adapterMarcas = android.widget.ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            marcas
        )
        dialogBinding.autoMarca.setAdapter(adapterMarcas)
        val nombresCategorias = mutableListOf("Todas")
        nombresCategorias.addAll(
            listaCategorias
                .filter { it.estado }
                .map { it.nombre }
                .sorted()
        )
        val adapterCategorias = android.widget.ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            nombresCategorias
        )
        dialogBinding.autoCategoria.setAdapter(
            adapterCategorias
        )
        marcaSeleccionada?.let {
            dialogBinding.autoMarca.setText(marcaSeleccionada ?: "Todas", false)
            val nombreCategoriaSeleccionada =
                listaCategorias.find { it.idCategoria == categoriaSeleccionada }
                    ?.nombre
                    ?: "Todas"
            dialogBinding.autoCategoria.setText(
                nombreCategoriaSeleccionada,
                false
            )
        }
        precioMinimo?.let {
            dialogBinding.edtPrecioMin.setText(it.toString())
        }
        precioMaximo?.let {
            dialogBinding.edtPrecioMax.setText(it.toString())
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Filtrar productos")
            .setView(dialogBinding.root)
            .setNegativeButton("Cancelar", null)
            .setNeutralButton("Limpiar", null)
            .setPositiveButton("Aplicar", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val marca = dialogBinding.autoMarca.text.toString()
                marcaSeleccionada = if (marca.isBlank() || marca == "Todas") {
                    null
                } else {
                    marca
                }
                val nombreCategoria = dialogBinding.autoCategoria.text.toString()
                categoriaSeleccionada = if (
                    nombreCategoria.isBlank() ||
                    nombreCategoria == "Todas"
                ) {
                    null
                } else {
                    listaCategorias.find { it.nombre == nombreCategoria }
                        ?.idCategoria
                }
                precioMinimo = dialogBinding.edtPrecioMin.text
                    ?.toString()
                    ?.toDoubleOrNull()
                precioMaximo = dialogBinding.edtPrecioMax.text
                    ?.toString()
                    ?.toDoubleOrNull()

                if (
                    precioMinimo != null &&
                    precioMaximo != null &&
                    precioMinimo!! > precioMaximo!!
                ) {
                    Toast.makeText(
                        requireContext(),
                        "El precio mínimo no puede ser mayor al máximo.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                aplicarFiltros()
                dialog.dismiss()
            }
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                marcaSeleccionada = null
                categoriaSeleccionada = null
                precioMinimo = null
                precioMaximo = null

                dialogBinding.autoMarca.setText("Todas", false)
                dialogBinding.autoCategoria.setText("Todas", false)
                dialogBinding.edtPrecioMin.text?.clear()
                dialogBinding.edtPrecioMax.text?.clear()

                aplicarFiltros()
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}