package com.mesajil.app.ui.carrito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemCarritoBinding
import com.mesajil.app.models.response.DetalleCarritoResponse

class CarritoAdapter(
    private val productos: MutableList<DetalleCarritoResponse>,
    private val onAumentar: (DetalleCarritoResponse) -> Unit,
    private val onDisminuir: (DetalleCarritoResponse) -> Unit,
    private val onEliminar: (DetalleCarritoResponse) -> Unit,
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(
        val binding: ItemCarritoBinding
    ) : RecyclerView.ViewHolder(binding.root)

    fun actualizarProductos(nuevosProductos: List<DetalleCarritoResponse>) {
        productos.clear()
        productos.addAll(nuevosProductos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val binding = ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarritoViewHolder(binding)
    }

    override fun getItemCount() = productos.size

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = productos[position]
        with(holder.binding) {
            txtNombre.text = producto.nombreProducto
            txtPrecio.text = "S/. %.2f".format(producto.precioUnitario)
            txtCantidad.text = producto.cantidad.toString()
            txtSubtotal.text = "Subtotal: S/. %.2f".format(producto.subtotal)

            btnMas.setOnClickListener {
                onAumentar(producto)
            }

            btnMenos.setOnClickListener {
                onDisminuir(producto)
            }

            btnEliminar.setOnClickListener {
                onEliminar(producto)
            }
        }
    }
}
