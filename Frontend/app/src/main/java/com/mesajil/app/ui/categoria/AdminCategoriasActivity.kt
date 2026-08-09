package com.mesajil.app.ui.categoria

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesajil.app.databinding.ActivityAdminCategoriasBinding
import com.mesajil.app.viewmodel.CategoriaViewModel

class AdminCategoriasActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAdminCategoriasBinding
    private val viewModel: CategoriaViewModel by viewModels()
    private lateinit var adapter: AdminCategoriaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
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
        adapter = AdminCategoriaAdapter(
            mutableListOf()
        ) { categoria ->
            val intent = Intent(
                this,
                FormCategoriaActivity::class.java
            )
            intent.putExtra(
                "ID_CATEGORIA",
                categoria.idCategoria
            )
            startActivity(intent)
        }
        binding.rvCategorias.layoutManager =
            LinearLayoutManager(this)
        binding.rvCategorias.adapter = adapter
        binding.cardAgregarCategoria.setOnClickListener {
            val intent = Intent(
                this,
                FormCategoriaActivity::class.java
            )
            startActivity(intent)
        }
        cargarCategorias()
    }

    private fun cargarCategorias() {
        viewModel.obtenerCategorias(
            onSuccess = { categorias ->
                runOnUiThread {
                    adapter.actualizarLista(categorias)
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
            cargarCategorias()
        }
    }
}