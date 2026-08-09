package com.mesajil.app

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mesajil.app.ui.home.HomeFragment
import com.mesajil.app.ui.categoria.CategoriasFragment
import com.mesajil.app.ui.carrito.CarritoFragment
import com.mesajil.app.ui.perfil.PerfilFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val preferencias = getSharedPreferences(
            "preferencias_app",
            Context.MODE_PRIVATE
        )
        val modoOscuro = preferencias.getBoolean("modo_oscuro", false)

        if (modoOscuro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentContainer = findViewById<android.view.View>(
            R.id.fragmentController
        )
        val bottomNavigation =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottomNavigation
            )
        ViewCompat.setOnApplyWindowInsetsListener(fragmentContainer) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                0,
                systemBars.top,
                0,
                0
            )
            insets
        }
        bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_categorias -> CategoriasFragment()
                R.id.nav_carrito -> CarritoFragment()
                R.id.nav_perfil -> PerfilFragment()
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentController, fragment)
                .commit()
            true
        }
        if (savedInstanceState == null) {
            val abrirCarrito =
                intent.getBooleanExtra("abrir_carrito", false)
            if (abrirCarrito) {
                bottomNavigation.selectedItemId = R.id.nav_carrito
            } else {
                bottomNavigation.selectedItemId = R.id.nav_home
            }
        }
    }
}