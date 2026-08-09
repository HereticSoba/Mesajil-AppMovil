package com.mesajil.app.ui.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mesajil.app.databinding.FragmentPerfilBinding
import android.content.Intent
import com.mesajil.app.ui.login.LoginActivity
import com.mesajil.app.preferences.SessionManager
import android.widget.Toast
import com.mesajil.app.ui.pedidos.HistorialPedidosActivity
import com.mesajil.app.ui.categoria.AdminCategoriasActivity
import com.mesajil.app.ui.producto.AdministrarProductosActivity
import com.mesajil.app.ui.inventario.AdminInventarioActivity
import com.mesajil.app.ui.usuario.AdminUsuariosActivity
import com.mesajil.app.ui.pedidos.ProductosMayorDemandaActivity
import androidx.appcompat.app.AppCompatDelegate
import android.content.Context

class PerfilFragment : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())
        binding.txtNombre.text = sessionManager.obtenerNombre()
        binding.txtCorreo.text = sessionManager.obtenerCorreo()
        val preferencias = requireContext().getSharedPreferences(
            "preferencias_app",
            Context.MODE_PRIVATE
        )
        val modoOscuro = preferencias.getBoolean("modo_oscuro", false)
        binding.switchModoOscuro.isChecked = modoOscuro
        binding.switchModoOscuro.setOnCheckedChangeListener { _, activado ->
            preferencias.edit()
                .putBoolean("modo_oscuro", activado)
                .apply()
            if (activado) {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            } else {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }

        binding.cardMiInformacion.setOnClickListener {
            val intent = Intent(requireContext(), MiInformacionActivity::class.java)
            startActivity(intent)
        }
        binding.cardPedidos.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    HistorialPedidosActivity::class.java
                )
            )
        }
        if (sessionManager.obtenerIdRol() == 1) {
            binding.cardAdministrarProductos.visibility = View.VISIBLE
            binding.cardAdministrarProductos.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        AdministrarProductosActivity::class.java
                    )
                )
            }
            binding.cardAdministrarCategorias.visibility = View.VISIBLE
            binding.cardAdministrarCategorias.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        AdminCategoriasActivity::class.java
                    )
                )
            }
            binding.cardAdministrarInventario.visibility = View.VISIBLE
            binding.cardAdministrarInventario.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        AdminInventarioActivity::class.java
                    )
                )
            }
            binding.cardAdministrarUsuarios.visibility = View.VISIBLE
            binding.cardAdministrarUsuarios.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        AdminUsuariosActivity::class.java
                    )
                )
            }
            binding.cardProductosMayorDemanda.visibility = View.VISIBLE
            binding.cardProductosMayorDemanda.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        ProductosMayorDemandaActivity::class.java
                    )
                )
            }
        } else {
            binding.cardAdministrarProductos.visibility = View.GONE
            binding.cardAdministrarCategorias.visibility = View.GONE
            binding.cardAdministrarInventario.visibility = View.GONE
            binding.cardAdministrarUsuarios.visibility = View.GONE
            binding.cardProductosMayorDemanda.visibility = View.GONE
        }
        binding.cardAcerca.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "App Mesajil v1.0 Developed by HereticSoba",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnCerrarSesion.setOnClickListener {
            sessionManager.cerrarSesion()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            binding.txtNombre.text = sessionManager.obtenerNombre()
            binding.txtCorreo.text = sessionManager.obtenerCorreo()
        }
    }
}