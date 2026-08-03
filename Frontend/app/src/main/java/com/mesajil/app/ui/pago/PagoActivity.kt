package com.mesajil.app.ui.pago

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mesajil.app.databinding.ActivityPagoBinding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import androidx.lifecycle.lifecycleScope
import android.widget.Toast
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.launch

class PagoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPagoBinding
    private var tarjetaValida = false
    private var vencimientoValido = false
    private var cvvValido = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarCamposTarjeta()
        configurarValidacionesTarjeta()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val total = intent.getDoubleExtra(
            "TOTAL",
            0.0
        )
        binding.txtTotal.text = "S/. %.2f".format(total)
        binding.btnPagar.setOnClickListener {
            generarTokenTarjeta()
        }
    }

    private fun configurarValidacionesTarjeta() {
        binding.cardNumberField.onEvent = { event ->
            when (event) {
                is CardNumberTextFieldEvent.IsValid -> {
                    tarjetaValida = event.isValid
                    actualizarEstadoBotonPago()
                }

                else -> Unit
            }
        }
        binding.expirationDateField.onEvent = { event ->
            when (event) {
                is ExpirationDateTextFieldEvent.IsValid -> {
                    vencimientoValido = event.isValid
                    actualizarEstadoBotonPago()
                }

                else -> Unit
            }
        }
        binding.securityCodeField.onEvent = { event ->
            when (event) {
                is SecurityCodeTextFieldEvent.IsValid -> {
                    cvvValido = event.isValid
                    actualizarEstadoBotonPago()
                }

                else -> Unit
            }
        }
    }

    private fun actualizarEstadoBotonPago() {
        binding.btnPagar.isEnabled = tarjetaValida && vencimientoValido && cvvValido
    }

    private fun configurarCamposTarjeta() {
        binding.cardNumberField.textStyle = TextStyle(
            color = Color.Black,
            fontSize = 18.sp
        )
        binding.expirationDateField.textStyle = TextStyle(
            color = Color.Black,
            fontSize = 18.sp
        )
        binding.securityCodeField.textStyle = TextStyle(
            color = Color.Black,
            fontSize = 18.sp
        )
    }

    private fun generarTokenTarjeta() {
        binding.btnPagar.isEnabled = false
        lifecycleScope.launch {
            val coreMethods = MercadoPagoSDK.getInstance().coreMethods
            val buyerIdentification = BuyerIdentification(
                name = "APRO",
                number = "12345678",
                type = "DNI"
            )
            when (
                val result = coreMethods.generateCardToken(
                    cardNumberState = binding.cardNumberField.state,
                    expirationDateState = binding.expirationDateField.state,
                    securityCodeState = binding.securityCodeField.state,
                    buyerIdentification = buyerIdentification
                )
            ) {
                is Result.Success -> {
                    val token = result.data.token
                    Toast.makeText(
                        this@PagoActivity,
                        "Token generado correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is Result.Error -> {
                    Toast.makeText(
                        this@PagoActivity,
                        "Error al generar token: ${result.error}",
                        Toast.LENGTH_LONG
                    ).show()
                    actualizarEstadoBotonPago()
                }
            }
        }
    }
}