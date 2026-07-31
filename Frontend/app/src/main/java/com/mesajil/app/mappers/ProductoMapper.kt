package com.mesajil.app.mappers

import com.mesajil.app.models.Producto
import com.mesajil.app.models.response.ProductoResponse

object ProductoMapper {
    fun toModel(response: ProductoResponse): Producto {
        return Producto(
            idProducto = response.idProducto,
            nombre = response.nombre,
            descripcion = response.descripcion ?: "",
            precio = response.precio,
            stock = response.stockActual,
            idCategoria = response.idCategoria
        )
    }
}