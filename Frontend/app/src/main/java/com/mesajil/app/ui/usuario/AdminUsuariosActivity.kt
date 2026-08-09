package com.mesajil.app.ui.usuario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.databinding.ActivityAdminUsuariosBinding
import com.mesajil.app.viewmodel.auth.UsuarioViewModel

class AdminUsuariosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUsuariosBinding
    private val viewModel: UsuarioViewModel by viewModels()
    private lateinit var adapter: AdminUsuariosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUsuariosBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(
            binding.root
        ) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                systemBars.bottom
            )
            insets
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        adapter = AdminUsuariosAdapter(
            mutableListOf()
        ) { usuario ->
            val intent = Intent(
                this,
                FormUsuarioActivity::class.java
            )
            intent.putExtra(
                "ID_USUARIO",
                usuario.idUsuario
            )
            startActivity(intent)
        }
        binding.rvUsuarios.layoutManager =
            LinearLayoutManager(this)
        binding.rvUsuarios.adapter = adapter
        binding.cardAgregarUsuario.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    FormUsuarioActivity::class.java
                )
            )
        }
        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        viewModel.obtenerUsuarios(
            onSuccess = { usuarios ->
                runOnUiThread {
                    adapter.actualizarLista(usuarios)
                }
            },
            onError = { mensaje ->
                runOnUiThread {
                    Toast.makeText(
                        this,
                        mensaje,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            cargarUsuarios()
        }
    }
}