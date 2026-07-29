package com.mesajil.app.ui.carrito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemCarritoBinding

class CarritoAdapter(
    private val productos: MutableList<CarritoItem>,
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

    private fun mostrarProducto(holder: CarritoViewHolder, producto: CarritoItem) {
        with(holder.binding) {
            txtNombre.text = producto.nombre
            txtPrecio.text = "S/. %.2f".format(producto.precio)
            txtCantidad.text = producto.cantidad.toString()
            txtSubtotal.text = "Subtotal: S/. %.2f".format(producto.subtotal)
        }
    }

    private fun configurarEventos(holder: CarritoViewHolder, producto: CarritoItem) {
        with(holder.binding) {
            btnMas.setOnClickListener {
                aumentarCantidad(producto)
            }
            btnMenos.setOnClickListener {
                disminuirCantidad(producto)
            }
            btnEliminar.setOnClickListener {
                eliminarProducto(producto)
            }
        }
    }

    private fun aumentarCantidad(producto: CarritoItem) {
        producto.cantidad++
        notifyDataSetChanged()
        onCarritoActualizado()
    }

    private fun disminuirCantidad(producto: CarritoItem) {
        if (producto.cantidad > 1) {
            producto.cantidad--
            notifyDataSetChanged()
            onCarritoActualizado()
        }
    }

    private fun eliminarProducto(producto: CarritoItem) {
        productos.remove(producto)
        notifyDataSetChanged()
        onCarritoActualizado()
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = productos[position]
        mostrarProducto(holder, producto)
        configurarEventos(holder, producto)
    }
}
