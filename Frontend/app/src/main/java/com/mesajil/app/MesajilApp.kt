package com.mesajil.app

import android.app.Application
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
    }
}