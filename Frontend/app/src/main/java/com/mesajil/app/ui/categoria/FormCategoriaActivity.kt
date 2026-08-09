package com.mesajil.app.ui.categoria

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mesajil.app.databinding.ActivityFormCategoriaBinding
import com.mesajil.app.models.request.CategoriaCreateRequest
import com.mesajil.app.models.request.CategoriaUpdateRequest
import com.mesajil.app.viewmodel.CategoriaViewModel

class FormCategoriaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormCategoriaBinding
    private val viewModel: CategoriaViewModel by viewModels()
    private var idCategoriaEditar: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCategoriaBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        idCategoriaEditar = intent.getIntExtra(
            "ID_CATEGORIA",
            0
        )
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
        if (idCategoriaEditar > 0) {
            binding.toolbar.title =
                "Modificar categoría"
            binding.btnGuardar.text =
                "Guardar cambios"
            binding.btnEliminar.visibility =
                android.view.View.VISIBLE
            cargarCategoria()
        } else {
            binding.toolbar.title =
                "Nueva categoría"
            binding.btnGuardar.text =
                "Registrar categoría"
        }
        binding.btnGuardar.setOnClickListener {
            guardarCategoria()
        }
        binding.btnEliminar.setOnClickListener {
            eliminarCategoria()
        }
    }

    private fun cargarCategoria() {
        viewModel.obtenerCategorias(
            onSuccess = { categorias ->
                val categoria = categorias.find {
                    it.idCategoria == idCategoriaEditar
                }
                runOnUiThread {
                    if (categoria != null) {
                        binding.edtNombre.setText(
                            categoria.nombre
                        )
                        binding.edtDescripcion.setText(
                            categoria.descripcion ?: ""
                        )
                    } else {
                        Toast.makeText(
                            this,
                            "No se encontró la categoría.",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
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

    private fun guardarCategoria() {
        val nombre = binding.edtNombre
            .text
            .toString()
            .trim()
        val descripcion = binding.edtDescripcion
            .text
            .toString()
            .trim()

        if (nombre.isEmpty()) {
            Toast.makeText(
                this,
                "Ingresa el nombre de la categoría.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (idCategoriaEditar > 0) {
            val request = CategoriaUpdateRequest(
                idCategoria = idCategoriaEditar,
                nombre = nombre,
                descripcion = descripcion.ifEmpty {
                    null
                }
            )

            viewModel.actualizarCategoria(
                idCategoria = idCategoriaEditar,
                request = request,
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Categoría actualizada correctamente.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
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
        } else {
            val request = CategoriaCreateRequest(
                nombre = nombre,
                descripcion = descripcion.ifEmpty {
                    null
                }
            )

            viewModel.crearCategoria(
                request = request,
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Categoría registrada correctamente.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
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
    }

    private fun eliminarCategoria() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Eliminar categoría")
            .setMessage("¿Estás seguro de eliminar esta categoría?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->

                viewModel.eliminarCategoria(
                    idCategoria = idCategoriaEditar,
                    onSuccess = {
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                "Categoría eliminada correctamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
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
            }.show()
    }
}