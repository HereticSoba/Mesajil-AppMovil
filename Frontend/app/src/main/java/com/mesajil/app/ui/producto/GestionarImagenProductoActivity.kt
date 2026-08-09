package com.mesajil.app.ui.producto

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.mesajil.app.R
import com.mesajil.app.databinding.ActivityGestionarImagenProductoBinding
import com.mesajil.app.viewmodel.ImagenProductoViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class GestionarImagenProductoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGestionarImagenProductoBinding
    private val viewModel: ImagenProductoViewModel by viewModels()
    private var idProducto: Int = 0
    private var idImagen: Int = 0
    private var imagenSeleccionada: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionarImagenProductoBinding.inflate(layoutInflater)
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
        idProducto = intent.getIntExtra(
            "ID_PRODUCTO",
            0
        )
        if (idProducto == 0) {
            Toast.makeText(
                this,
                "Producto no válido.",
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }
        binding.btnSeleccionarNuevaImagen.setOnClickListener {
            seleccionarNuevaImagen.launch("image/*")
        }
        binding.btnReemplazarImagen.setOnClickListener {
            reemplazarImagen()
        }
        binding.btnEliminarImagen.setOnClickListener {
            confirmarEliminarImagen()
        }
        cargarImagen()
    }

    private fun confirmarEliminarImagen() {
        if (idImagen == 0) {
            Toast.makeText(
                this,
                "Este producto no tiene una imagen para eliminar.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Eliminar imagen")
            .setMessage(
                "¿Estás seguro que deseas eliminar la imagen de este producto?"
            )
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarImagen()
            }.show()
    }

    private fun eliminarImagen() {
        viewModel.eliminarImagen(
            idImagen = idImagen, onSuccess = {
                runOnUiThread {
                    binding.imgProducto.setImageResource(
                        R.drawable.ic_image
                    )
                    binding.txtEstado.text = "Este producto no tiene imagen."
                    binding.btnReemplazarImagen.visibility = View.GONE
                    binding.btnEliminarImagen.visibility = View.GONE
                    imagenSeleccionada = null
                    idImagen = 0
                    Toast.makeText(
                        this,
                        "Imagen eliminada correctamente.",
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun cargarImagen() {
        viewModel.obtenerImagenProducto(
            idProducto = idProducto,
            onSuccess = { imagen ->
                runOnUiThread {
                    if (imagen != null) {
                        idImagen = imagen.idImagen
                        val urlImagen =
                            "http://192.168.100.54:5228${imagen.urlImagen}"
                        Glide.with(this)
                            .load(urlImagen)
                            .placeholder(R.drawable.ic_image)
                            .error(R.drawable.ic_image)
                            .into(binding.imgProducto)
                        binding.txtEstado.text = "Imagen principal registrada"
                        binding.btnReemplazarImagen.visibility = View.GONE
                        binding.btnEliminarImagen.visibility = View.VISIBLE
                        binding.btnReemplazarImagen.text = "Reemplazar imagen"
                    } else {
                        idImagen = 0
                        imagenSeleccionada = null
                        binding.imgProducto.setImageResource(R.drawable.ic_image)
                        binding.txtEstado.text = "Este producto no tiene imagen."
                        binding.btnReemplazarImagen.visibility = View.GONE
                        binding.btnEliminarImagen.visibility = View.GONE
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

    private val seleccionarNuevaImagen =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imagenSeleccionada = uri
                binding.imgProducto.setImageURI(uri)
                binding.txtEstado.text =
                    "Nueva imagen seleccionada"
                binding.btnReemplazarImagen.visibility = View.VISIBLE
                if (idImagen > 0) {
                    binding.btnReemplazarImagen.text = "Reemplazar imagen"
                } else {
                    binding.btnReemplazarImagen.text = "Agregar imagen"
                }
            }
        }

    private fun reemplazarImagen() {
        val uri = imagenSeleccionada ?: return
        val inputStream =
            contentResolver.openInputStream(uri)
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
        val requestFile =
            archivo.asRequestBody(
                "image/*".toMediaType()
            )
        val imagenPart =
            MultipartBody.Part.createFormData(
                "Imagen",
                archivo.name,
                requestFile
            )
        if (idImagen > 0) {
            viewModel.actualizarImagen(
                idImagen = idImagen,
                idProducto = idProducto,
                imagen = imagenPart,
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Imagen reemplazada correctamente.",
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
            val idProductoBody =
                idProducto.toString().toRequestBody("text/plain".toMediaType())
            val principalBody =
                "true".toRequestBody("text/plain".toMediaType())
            viewModel.subirImagen(
                idProducto = idProductoBody,
                principal = principalBody,
                imagen = imagenPart,
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Imagen agregada correctamente.",
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
}