package com.mesajil.app.ui.pago

import android.os.Bundle
import android.util.Log
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
import com.mesajil.app.models.request.PagoRequest
import com.mesajil.app.preferences.SessionProvider
import com.mesajil.app.repository.PagoRepository

class PagoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPagoBinding
    private var tarjetaValida = false
    private var vencimientoValido = false
    private var cvvValido = false
    private var tokenTarjeta = ""
    private var metodoPago = ""
    private var tipoMetodoPago = ""
    private var cuotas = 1
    private val pagoRepository = PagoRepository()
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

                    Log.d(
                        "PagoValidacion",
                        "Número tarjeta -> IsValid=${event.isValid}"
                    )

                    actualizarEstadoBotonPago()
                }
                is CardNumberTextFieldEvent.OnBinChanged -> {
                    event.cardBin?.let { bin ->
                        lifecycleScope.launch {
                            val coreMethods = MercadoPagoSDK.getInstance().coreMethods
                            when (val result = coreMethods.getPaymentMethods(bin)) {
                                is Result.Success -> {
                                    val metodo = result.data.firstOrNull()
                                    metodoPago = metodo?.id.orEmpty()
                                    tipoMetodoPago = metodo?.paymentTypeId.orEmpty()
                                    Log.d(
                                        "MercadoPago",
                                        "Método: ${metodo?.id} - Tipo: ${metodo?.paymentTypeId}"
                                    )
                                }

                                is Result.Error -> {
                                    metodoPago = ""
                                    tipoMetodoPago = ""
                                }
                            }
                        }
                    }
                }
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
        Log.d(
            "PagoValidacion",
            "Tarjeta=$tarjetaValida | Vencimiento=$vencimientoValido | CVV=$cvvValido | Metodo=$metodoPago | Tipo=$tipoMetodoPago"
        )
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
                    tokenTarjeta = result.data.token
                    procesarPago(tokenTarjeta)
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

    private suspend fun procesarPago(token: String) {
        if (metodoPago.isBlank() || tipoMetodoPago.isBlank()) {
            Toast.makeText(
                this@PagoActivity,
                "No se pude identificar el método de pago",
                Toast.LENGTH_LONG
            ).show()
            actualizarEstadoBotonPago()
            return
        }
        val request = PagoRequest(
            email = "comprador@testuser.com",
            token = token,
            metodoPago = metodoPago,
            tipoMetodoPago = tipoMetodoPago,
            cuotas = 1,
            tipoDocumento = "DNI",
            numeroDocumento = "12345678",
            tipoEntrega = intent.getStringExtra("TIPO_ENTREGA").orEmpty(),
            direccionEntrega = intent.getStringExtra("DIRECCION_ENTREGA")
        )
        try {
            val response = pagoRepository.procesarPago(request)
            if (response.isSuccessful) {
                val pago = response.body()
                Toast.makeText(
                    this@PagoActivity,
                    "Pago procesado: ${pago?.estado}",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val error = response.errorBody()?.string()
                Toast.makeText(
                    this@PagoActivity,
                    "Error al procesar pago: $error",
                    Toast.LENGTH_LONG
                ).show()
                actualizarEstadoBotonPago()
            }
        } catch (e: Exception) {
            Toast.makeText(
                this@PagoActivity,
                "Error de conexión: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            actualizarEstadoBotonPago()
        }
    }
}