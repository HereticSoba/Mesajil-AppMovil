package com.mesajil.app.ui.carrito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemCarritoBinding
import com.mesajil.app.models.response.DetalleCarritoResponse

class CarritoAdapter(
    private val productos: MutableList<DetalleCarritoResponse>,
    private val onCarritoActualizado: () -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(
        val binding: ItemCarritoBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val binding = ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarritoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    private fun mostrarProducto(holder: CarritoViewHolder, producto: DetalleCarritoResponse) {
        with(holder.binding) {
            txtNombre.text = producto.nombreProducto
            txtPrecio.text = "S/. %.2f".format(producto.precioUnitario)
            txtCantidad.text = producto.cantidad.toString()
            txtSubtotal.text = "Subtotal: S/. %.2f".format(producto.subtotal)
        }
    }

    private fun configurarEventos(holder: CarritoViewHolder, producto: DetalleCarritoResponse) {
//        with(holder.binding) {
//            btnMas.setOnClickListener {
//                aumentarCantidad(producto)
//            }
//            btnMenos.setOnClickListener {
//                disminuirCantidad(producto)
//            }
//            btnEliminar.setOnClickListener {
//                eliminarProducto(producto)
//            }
//        }
    }

//    private fun aumentarCantidad(producto: DetalleCarritoResponse) {
//        producto.cantidad++
//        notifyDataSetChanged()
//        onCarritoActualizado()
//    }
//
//    private fun disminuirCantidad(producto: DetalleCarritoResponse) {
//        if (producto.cantidad > 1) {
//            producto.cantidad--
//            notifyDataSetChanged()
//            onCarritoActualizado()
//        }
//    }
//
//    private fun eliminarProducto(producto: DetalleCarritoResponse) {
//        productos.remove(producto)
//        notifyDataSetChanged()
//        onCarritoActualizado()
//    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = productos[position]
        mostrarProducto(holder, producto)
        configurarEventos(holder, producto)
    }
}
