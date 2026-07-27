package com.mesajil.app.ui.carrito

data class CarritoItem(
    val idProducto: Int,
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)
{
    val subtotal: Double
        get() = precio * cantidad
}