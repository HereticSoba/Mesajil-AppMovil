package com.mesajil.app.ui.login

import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.mesajil.app.databinding.ActivityLoginBinding
import com.mesajil.app.preferences.SessionManager
import com.mesajil.app.viewmodel.auth.LoginViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.mesajil.app.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        sessionManager = SessionManager(this)
        binding.edtCorreo.setText(sessionManager.obtenerUltimoCorreo())
        binding.btnLogin.setOnClickListener {
            val correo = binding.edtCorreo.text.toString().trim()
            val contrasena = binding.edtPassword.text.toString().trim()
            if (correo.isEmpty()) {
                binding.edtCorreo.error = "Ingrese su correo"
                binding.edtCorreo.requestFocus()
                return@setOnClickListener
            }
            if (contrasena.isEmpty()) {
                binding.edtPassword.error = "Ingrese su contraseña"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }
            viewModel.login(correo, contrasena)
        }
        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { response ->
                sessionManager.guardarSesion(
                    response.token,
                    response.idUsuario,
                    response.nombres,
                    response.correo,
                    response.idRol
                )
                sessionManager.guardarUltimoCorreo(response.correo)
                Toast.makeText(this, "Bienvenido ${response.nombres}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            result.onFailure {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}