package com.mesajil.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mesajil.app.ui.home.HomeFragment
import com.mesajil.app.ui.categoria.CategoriasFragment
import com.mesajil.app.ui.carrito.CarritoFragment
import com.mesajil.app.ui.perfil.PerfilFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentController, HomeFragment())
                .commit()
        }
        bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when(item.itemId){
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
    }
}