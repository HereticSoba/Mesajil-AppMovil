package com.mesajil.app.ui.registro

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mesajil.app.databinding.ActivityRegistroBinding
import androidx.activity.viewModels
import com.mesajil.app.viewmodel.auth.RegistroViewModel
import androidx.appcompat.app.AlertDialog

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding
    private val viewModel: RegistroViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
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
        binding.txtLogin.setOnClickListener {
            finish()
        }
        binding.btnRegistrar.setOnClickListener {
            val nombres = binding.edtNombres.text.toString().trim()
            val apellidos = binding.edtApellidos.text.toString().trim()
            val correo = binding.edtCorreo.text.toString().trim()
            val telefono = binding.edtTelefono.text.toString().trim()
            val direccion = binding.edtDireccion.text.toString().trim()
            val contrasena = binding.edtPassword.text.toString().trim()
            val confirmarContrasena = binding.edtConfirmarPassword.text.toString().trim()

            if (nombres.isEmpty() ||
                apellidos.isEmpty() ||
                correo.isEmpty() ||
                contrasena.isEmpty() ||
                confirmarContrasena.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Complete todos los campos obligatorios.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(
                    this,
                    "Ingrese un correo electrónico válido.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (contrasena != confirmarContrasena) {
                Toast.makeText(
                    this,
                    "Las contraseñas no coinciden.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            binding.btnRegistrar.isEnabled = false
            binding.btnRegistrar.text = "Registrando..."
            viewModel.registrar(
                nombres,
                apellidos,
                correo,
                contrasena,
                telefono.ifBlank { null },
                direccion.ifBlank { null }
            )
        }
        viewModel.registroResult.observe(this) { result ->
            binding.btnRegistrar.isEnabled = true
            binding.btnRegistrar.text = "CREAR CUENTA"
            result.onSuccess { response ->
                Toast.makeText(
                    this,
                    response.mensaje,
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
            result.onFailure { error ->
                binding.btnRegistrar.isEnabled = true
                AlertDialog.Builder(this)
                    .setTitle("Registro no completado.")
                    .setMessage(
                        error.message ?: "Ocurrió un error al registrar."
                    )
                    .setPositiveButton("Aceptar", null)
                    .show()
            }
        }
    }
}