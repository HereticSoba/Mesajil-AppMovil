package com.mesajil.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mesajil.app.ui.home.HomeFragment
import com.mesajil.app.ui.categoria.CategoriasFragment
import com.mesajil.app.ui.carrito.CarritoFragment
import com.mesajil.app.ui.perfil.PerfilFragment
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentContainer = findViewById<FragmentContainerView>(R.id.fragmentController)
        ViewCompat.setOnApplyWindowInsetsListener(fragmentContainer){view, insets -> val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(0, systemBars.top, 0, 0)
            insets}
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