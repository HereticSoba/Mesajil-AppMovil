package com.mesajil.app.ui.producto

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mesajil.app.databinding.ActivityNuevoProductoBinding
import com.mesajil.app.models.request.ProductoCreateRequest
import com.mesajil.app.viewmodel.ProductoViewModel
import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mesajil.app.models.request.ProductoUpdateRequest
import com.mesajil.app.viewmodel.ImagenProductoViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.mesajil.app.repository.CategoriaRepository
import com.mesajil.app.models.response.CategoriaResponse
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class NuevoProductoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevoProductoBinding
    private val viewModel: ProductoViewModel by viewModels()
    private val imagenViewModel: ImagenProductoViewModel by viewModels()
    private var imagenSeleccionada: Uri? = null
    private var idProductoEditar: Int = 0
    private val categoriaRepository = CategoriaRepository()
    private var idCategoriaSeleccionada = 0
    private var categorias = emptyList<CategoriaResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarCategorias()
        idProductoEditar = intent.getIntExtra("ID_PRODUCTO", 0)
        if (idProductoEditar > 0) {
            binding.toolbar.title = "Modificar producto"
            binding.btnGuardar.text = "Guardar cambios"
            binding.btnSeleccionarImagen.visibility = View.GONE
            binding.btnEliminarImagen.visibility = View.GONE
            binding.imgProducto.visibility = View.GONE
            cargarProducto(idProductoEditar)
        } else {
            binding.toolbar.title = "Nuevo producto"
            binding.btnGuardar.text = "Registrar producto"
        }
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
        binding.btnGuardar.setOnClickListener {
            registrarProducto()
        }
        binding.btnSeleccionarImagen.setOnClickListener {
            seleccionarImagen.launch("image/*")
        }
        binding.btnEliminarImagen.setOnClickListener {
            imagenSeleccionada = null
            binding.imgProducto.setImageDrawable(null)
            binding.imgProducto.visibility = android.view.View.GONE
            binding.btnEliminarImagen.visibility = android.view.View.GONE
        }
    }

    private fun cargarCategorias() {

        lifecycleScope.launch {

            val resultado =
                categoriaRepository.obtenerCategorias()
                    .filter { it.estado }

            categorias = resultado

            val nombres =
                categorias.map { it.nombre }

            val adapter = ArrayAdapter(
                this@NuevoProductoActivity,
                android.R.layout.simple_dropdown_item_1line,
                nombres
            )

            binding.autoCategoria.setAdapter(adapter)

            binding.autoCategoria.setOnItemClickListener { _, _, position, _ ->

                idCategoriaSeleccionada =
                    categorias[position].idCategoria
            }

            if (idProductoEditar > 0) {
                cargarProducto(idProductoEditar)
            }
        }
    }

    private fun registrarProducto() {
        val nombre = binding.edtNombre.text.toString().trim()
        val descripcion = binding.edtDescripcion.text.toString().trim().ifEmpty { null }
        val marca = binding.edtMarca.text.toString().trim()
        val modelo = binding.edtModelo.text.toString().trim()
        val precio = binding.edtPrecio.text.toString().toDoubleOrNull()
        if (idCategoriaSeleccionada == 0 || nombre.isEmpty() || marca.isEmpty() || modelo.isEmpty() || precio == null) {
            Toast.makeText(
                this,
                "Completa los campos obligatorios.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val request = ProductoCreateRequest(
            idCategoria = idCategoriaSeleccionada,
            nombre = nombre,
            descripcion = descripcion,
            marca = marca,
            modelo = modelo,
            precio = precio
        )
        if (idProductoEditar > 0) {
            actualizarProducto()
        } else {
            crearProducto(request)
        }
    }

    private fun crearProducto(request: ProductoCreateRequest) {
        viewModel.crearProducto(
            request = request,
            onSuccess = { producto ->
                runOnUiThread {
                    if (imagenSeleccionada != null) {
                        subirImagenProducto(producto.idProducto)
                    } else {
                        Toast.makeText(
                            this,
                            "Producto registrado correctamente.",
                            Toast.LENGTH_SHORT
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

    private fun actualizarProducto() {
        val request = ProductoUpdateRequest(
            idProducto = idProductoEditar,
            idCategoria = idCategoriaSeleccionada,
            nombre = binding.edtNombre.text.toString().trim(),
            descripcion = binding.edtDescripcion.text.toString().trim(),
            marca = binding.edtMarca.text.toString().trim(),
            modelo = binding.edtModelo.text.toString().trim(),
            precio = binding.edtPrecio.text.toString().toDouble()
        )
        viewModel.actualizarProducto(
            idProducto = idProductoEditar, request = request,
            onSuccess = {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Producto actualizado correctamente.",
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

    private fun cargarProducto(idProducto: Int) {
        viewModel.obtenerProductoPorId(
            idProducto = idProducto, onSuccess = { producto ->
                runOnUiThread {
                    idCategoriaSeleccionada = producto.idCategoria
                    val posicion = categorias.indexOfFirst {
                        it.idCategoria == producto.idCategoria
                    }
                    if (posicion >= 0) {
                        binding.autoCategoria.setText(
                            categorias[posicion].nombre,
                            false
                        )
                    }
                    binding.edtNombre.setText(
                        producto.nombre
                    )
                    binding.edtDescripcion.setText(
                        producto.descripcion
                    )
                    binding.edtMarca.setText(
                        producto.marca
                    )
                    binding.edtModelo.setText(
                        producto.modelo
                    )
                    binding.edtPrecio.setText(
                        producto.precio.toString()
                    )
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

    private fun subirImagenProducto(idProducto: Int) {
        val uri = imagenSeleccionada ?: return
        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream == null) {
            Toast.makeText(
                this,
                "No se pudo leer la imagen.",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val archivo = File(
            cacheDir,
            "producto_${System.currentTimeMillis()}.jpg"
        )
        inputStream.use { input ->
            archivo.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val requestFile = archivo.asRequestBody("image/*".toMediaType())
        val imagenPart = MultipartBody.Part.createFormData(
            "Imagen",
            archivo.name,
            requestFile
        )
        val idProductoBody = idProducto.toString().toRequestBody("text/plain".toMediaType())
        val principalBody = "true".toRequestBody("text/plain".toMediaType())

        imagenViewModel.subirImagen(
            idProducto = idProductoBody,
            principal = principalBody,
            imagen = imagenPart,

            onSuccess = {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Producto e imagen registrados correctamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            },
            onError = { mensaje ->
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Producto registrado, pero no se pudo subir la imagen.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        )
    }

    private val seleccionarImagen =
        registerForActivityResult(ActivityResultContracts.GetContent())
        { uri ->
            if (uri != null) {
                imagenSeleccionada = uri
                binding.imgProducto.setImageURI(uri)
                binding.imgProducto.visibility = android.view.View.VISIBLE
                binding.btnEliminarImagen.visibility = android.view.View.VISIBLE
            }
        }
}