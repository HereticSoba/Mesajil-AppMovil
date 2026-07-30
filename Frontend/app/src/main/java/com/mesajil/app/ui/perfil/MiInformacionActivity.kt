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

class MiInformacionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMiInformacionBinding
    private lateinit var sessionManager: SessionManager
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiInformacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }
        sessionManager = SessionManager(this)

        binding.txtNombre.text = sessionManager.obtenerNombre()
        binding.txtCorreo.text = sessionManager.obtenerCorreo()
        binding.txtIdUsuario.text = sessionManager.obtenerIdUsuario().toString()
        binding.txtIdCarrito.text = sessionManager.obtenerIdCarrito().toString()
        val rol = when (sessionManager.obtenerIdRol()) {
            1 -> "Administrador"
            2 -> "Cliente"
            else -> "Usuario"
        }
        binding.txtRol.text = rol

        binding.btnEditar.setOnClickListener {
            Toast.makeText(
                this,
                "En desarrollo",
                Toast.LENGTH_SHORT
            ).show()
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
                }
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}