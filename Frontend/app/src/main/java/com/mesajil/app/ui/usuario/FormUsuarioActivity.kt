package com.mesajil.app.ui.usuario

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mesajil.app.databinding.ActivityFormUsuarioBinding
import com.mesajil.app.models.request.UsuarioCreateRequest
import com.mesajil.app.models.request.UsuarioUpdateRequest
import com.mesajil.app.models.response.UsuarioResponse
import com.mesajil.app.viewmodel.auth.UsuarioViewModel

class FormUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormUsuarioBinding
    private val viewModel: UsuarioViewModel by viewModels()
    private var idUsuario: Int = 0
    private var idRolSeleccionado: Int = 0
    private val roles = listOf(Pair(1, "Administrador"), Pair(2, "Cliente"))
    private val estados = listOf(Pair(true, "Activo"), Pair(false, "Inactivo"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idUsuario = intent.getIntExtra("ID_USUARIO", 0)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
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
        configurarRoles()
        configurarEstados()
        if (idUsuario > 0) {
            binding.toolbar.title = "Modificar usuario"
            binding.btnGuardar.text = "Guardar cambios"
            binding.btnDesactivar.visibility = View.VISIBLE
            binding.layoutContrasena.visibility = View.GONE
            binding.layoutEstado.visibility = View.VISIBLE
            cargarUsuario()
        } else {
            binding.toolbar.title = "Registrar usuario"
            binding.btnGuardar.text = "Registrar usuario"
            binding.btnDesactivar.visibility = View.GONE
            binding.layoutContrasena.visibility = View.VISIBLE
            binding.layoutEstado.visibility = View.GONE
        }
        binding.btnGuardar.setOnClickListener {
            guardarUsuario()
        }
        binding.btnDesactivar.setOnClickListener {
            desactivarUsuario()
        }
    }

    private fun configurarRoles() {
        val nombresRoles = roles.map { it.second }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombresRoles
        )
        binding.autoRol.setAdapter(adapter)
        binding.autoRol.setOnItemClickListener { _, _, position, _ ->
            idRolSeleccionado = roles[position].first
        }
    }

    private fun configurarEstados() {
        val nombresEstados = estados.map { it.second }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombresEstados
        )
        binding.autoEstado.setAdapter(adapter)
        binding.autoEstado.setOnItemClickListener { _, _, position, _ ->

            // El estado se obtiene directamente de la posición
            // al momento de actualizar.
        }
    }

    private fun cargarUsuario() {
        viewModel.obtenerUsuarios(
            onSuccess = { usuarios ->
                val usuario = usuarios.find {
                    it.idUsuario == idUsuario
                }
                runOnUiThread {
                    if (usuario != null) {
                        mostrarUsuario(usuario)
                    } else {
                        Toast.makeText(
                            this,
                            "Usuario no encontrado.",
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

    private fun mostrarUsuario(
        usuario: UsuarioResponse
    ) {
        binding.edtNombres.setText(usuario.nombres)
        binding.edtApellidos.setText(usuario.apellidos)
        binding.edtCorreo.setText(usuario.correo)
        binding.edtTelefono.setText(usuario.telefono ?: "")
        binding.edtDireccion.setText(usuario.direccion ?: "")
        idRolSeleccionado = usuario.idRol
        val posicionRol = roles.indexOfFirst {
            it.first == usuario.idRol
        }
        if (posicionRol >= 0) {
            binding.autoRol.setText(
                roles[posicionRol].second,
                false
            )
        }
        val posicionEstado = estados.indexOfFirst {
            it.first == usuario.estado
        }
        if (posicionEstado >= 0) {
            binding.autoEstado.setText(
                estados[posicionEstado].second,
                false
            )
        }
    }

    private fun guardarUsuario() {
        val nombres = binding.edtNombres.text
            ?.toString()
            ?.trim()
        val apellidos = binding.edtApellidos.text
            ?.toString()
            ?.trim()
        val correo = binding.edtCorreo.text
            ?.toString()
            ?.trim()
        val telefono = binding.edtTelefono.text
            ?.toString()
            ?.trim()
        val direccion = binding.edtDireccion.text
            ?.toString()
            ?.trim()
        if (nombres.isNullOrBlank()) {
            binding.edtNombres.error = "Ingresa los nombres."
            return
        }
        if (apellidos.isNullOrBlank()) {
            binding.edtApellidos.error = "Ingresa los apellidos."
            return
        }
        if (correo.isNullOrBlank()) {
            binding.edtCorreo.error = "Ingresa el correo."
            return
        }
        if (idRolSeleccionado == 0) {
            Toast.makeText(
                this,
                "Selecciona un rol.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (idUsuario == 0) {
            val contrasena = binding.edtContrasena.text
                ?.toString()
            if (contrasena.isNullOrBlank()) {
                binding.edtContrasena.error = "Ingresa una contraseña."
                return
            }
            val request = UsuarioCreateRequest(
                idRol = idRolSeleccionado,
                nombres = nombres,
                apellidos = apellidos,
                correo = correo,
                contrasena = contrasena,
                telefono = telefono?.ifBlank { null },
                direccion = direccion?.ifBlank { null }
            )
            viewModel.crearUsuario(
                request = request, onSuccess = {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Usuario registrado correctamente.",
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
            val estado = binding.autoEstado.text.toString() == "Activo"
            val request = UsuarioUpdateRequest(
                idUsuario = idUsuario,
                idRol = idRolSeleccionado,
                nombres = nombres,
                apellidos = apellidos,
                correo = correo,
                telefono = telefono?.ifBlank { null },
                direccion = direccion?.ifBlank { null },
                estado = estado
            )
            viewModel.actualizarUsuario(
                idUsuario = idUsuario,
                request = request,
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Usuario actualizado correctamente.",
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

    private fun desactivarUsuario() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Desactivar usuario")
            .setMessage("¿Estás seguro de desactivar este usuario?")
            .setNegativeButton("Cancelar", null).setPositiveButton("Desactivar") { _, _ ->
                viewModel.desactivarCuenta(
                    idUsuario = idUsuario,
                    onSuccess = {
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                "Usuario desactivado correctamente.",
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