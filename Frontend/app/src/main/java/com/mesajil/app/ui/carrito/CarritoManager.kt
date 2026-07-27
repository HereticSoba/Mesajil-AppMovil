package com.mesajil.app.ui.carrito

object CarritoManager {
    private val productos = mutableListOf<CarritoItem>()
    fun obtenerProductos(): List<CarritoItem>{
        return productos
    }

    fun agregarProducto(item: CarritoItem){
        val existente = productos.find{
            it.idProducto == item.idProducto
        }
        if(existente != null){
            existente.cantidad += item.cantidad
        }else{
            productos.add(item)
        }
    }

    fun eliminarProducto(idProducto: Int){
        productos.removeAll{
            it.idProducto == idProducto
        }
    }

    fun vaciarCarrito(){
        productos.clear()
    }

    fun calcularTotal(): Double{
        return productos.sumOf {
            it.subtotal
        }
    }
}