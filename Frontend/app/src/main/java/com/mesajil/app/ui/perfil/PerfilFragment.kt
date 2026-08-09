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
import com.mesajil.app.ui.producto.AdminProductosActivity
import com.mesajil.app.ui.producto.AdministrarProductosActivity

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
        } else {
            binding.cardAdministrarProductos.visibility = View.GONE
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