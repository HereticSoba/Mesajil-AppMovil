package com.mesajil.app.ui.perfil

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mesajil.app.databinding.ActivityMiInformacionBinding
import com.mesajil.app.preferences.SessionManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.mesajil.app.viewmodel.auth.UsuarioViewModel
import com.mesajil.app.ui.login.LoginActivity
import com.mesajil.app.models.request.ActualizarPerfilRequest

class MiInformacionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMiInformacionBinding
    private lateinit var sessionManager: SessionManager
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var modoEdicion = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiInformacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                0,
                systemBars.top,
                0,
                systemBars.bottom
            )
            insets
        }
        sessionManager = SessionManager(this)
        cargarPerfil()

        binding.btnEditar.setOnClickListener {
            if (modoEdicion) {
                guardarCambios()
            } else {
                habilitarEdicion()
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.btnDesactivarCuenta.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Desactivar cuenta")
                .setMessage(
                    "¿Estás seguro de que deseas desactivar tu cuenta?\n\n" +
                            "Esta acción cerrará la sesión actual y no podrá volver a iniciar sesión."
                )
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Desactivar") { _, _ ->
                    usuarioViewModel.desactivarCuenta(
                        idUsuario = sessionManager.obtenerIdUsuario(),
                        onSuccess = {
                            runOnUiThread {
                                Toast.makeText(
                                    this,
                                    "Cuenta desactivada correctamente.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                sessionManager.cerrarSesion()
                                val intent = Intent(
                                    this,
                                    LoginActivity::class.java
                                )
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finishAffinity()
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

    private fun cargarPerfil() {
        usuarioViewModel.obtenerMiPerfil(
            onSuccess = { usuario ->
                runOnUiThread {
                    binding.edtNombres.setText(usuario.nombres)
                    binding.edtApellidos.setText(usuario.apellidos)
                    binding.edtCorreo.setText(usuario.correo)
                    binding.edtTelefono.setText(usuario.telefono.orEmpty())
                    binding.edtDireccion.setText(usuario.direccion.orEmpty())
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

    private fun habilitarEdicion() {
        modoEdicion = true

        binding.edtNombres.isEnabled = true
        binding.edtApellidos.isEnabled = true
        binding.edtCorreo.isEnabled = true
        binding.edtTelefono.isEnabled = true
        binding.edtDireccion.isEnabled = true

        binding.btnEditar.text = "Guardar cambios"
        binding.edtNombres.requestFocus()
    }

    private fun deshabilitarEdicion() {
        modoEdicion = false

        binding.edtNombres.isEnabled = false
        binding.edtApellidos.isEnabled = false
        binding.edtCorreo.isEnabled = false
        binding.edtTelefono.isEnabled = false
        binding.edtDireccion.isEnabled = false

        binding.btnEditar.text = "Editar información"
    }

    private fun guardarCambios() {
        val nombres = binding.edtNombres.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val apellidos = binding.edtApellidos.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val correo = binding.edtCorreo.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val telefono = binding.edtTelefono.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val direccion = binding.edtDireccion.text
            ?.toString()
            ?.trim()
            .orEmpty()

        if (nombres.isBlank()) {
            binding.layoutNombres.error = "Ingrese sus nombres."
            return
        }
        binding.layoutNombres.error = null
        if (apellidos.isBlank()) {
            binding.layoutApellidos.error = "Ingrese sus apellidos."
            return
        }
        binding.layoutApellidos.error = null
        if (correo.isBlank()) {
            binding.layoutCorreo.error = "Ingrese su correo electrónico."
            return
        }
        binding.layoutCorreo.error = null

        val request = ActualizarPerfilRequest(
            nombres = nombres,
            apellidos = apellidos,
            correo = correo,
            telefono = telefono.ifBlank { null },
            direccion = direccion.ifBlank { null }
        )
        binding.btnEditar.isEnabled = false

        usuarioViewModel.actualizarPerfil(
            request = request,
            onSuccess = {
                runOnUiThread {
                    sessionManager.actualizarDatosPerfil(
                        nombres = nombres,
                        correo = correo
                    )
                    binding.btnEditar.isEnabled = true
                    deshabilitarEdicion()
                    Toast.makeText(
                        this,
                        "Información actualizada correctamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onError = { mensaje ->
                runOnUiThread {
                    binding.btnEditar.isEnabled = true
                    Toast.makeText(
                        this,
                        mensaje,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}