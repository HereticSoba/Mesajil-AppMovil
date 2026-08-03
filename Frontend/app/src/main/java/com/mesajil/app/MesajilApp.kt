package com.mesajil.app

import android.app.Application
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import com.mesajil.app.preferences.SessionProvider

class MesajilApp : Application() {
    companion object {
        lateinit var instance: MesajilApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SessionProvider.init(this)

        MercadoPagoSDK.initialize(
            context = this,
            publicKey = BuildConfig.MERCADO_PAGO_PUBLIC_KEY,
            countryCode = CountryCode.PER
        )
    }
}