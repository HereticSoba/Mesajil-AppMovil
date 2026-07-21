package com.mesajil.app.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.adapters.ProductoAdapter
import com.mesajil.app.databinding.FragmentHomeBinding
import com.mesajil.app.models.Producto
import com.mesajil.app.preferences.SessionManager

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
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
        val productos = listOf(
            Producto(
                1,
                "Laptop Lenovo ThinkPad",
                "Laptop empresarial",
                2999.90,
                10,
                1
            ),
            Producto(
                2,
                "Mouse Logitech G203",
                "Mouse gamer RGB",
                129.90,
                20,
                2
            ),
            Producto(
                3,
                "Monitor Samsung 24''",
                "Monitor Full HD",
                699.90,
                8,
                3
            )
        )
        val adapter = ProductoAdapter(
            listaProductos = productos,
            onProductoClick = { producto ->
                Toast.makeText(
                    requireContext(),
                    producto.nombre,
                    Toast.LENGTH_SHORT
                ).show()
            },
            onAgregarClick = { producto ->
                Toast.makeText(
                    requireContext(),
                    "${producto.nombre} agregado al carrito",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        binding.rvProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductos.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}