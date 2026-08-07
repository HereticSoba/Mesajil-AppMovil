package com.mesajil.app.ui.pedidos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemDetallePedidoBinding
import com.mesajil.app.models.response.DetallePedidoResponse

class DetallePedidoAdapter(
    private val lista: MutableList<DetallePedidoResponse>
) : RecyclerView.Adapter<DetallePedidoAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemDetallePedidoBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ItemDetallePedidoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val producto = lista[position]

        holder.binding.txtProducto.text = producto.producto
        holder.binding.txtCantidad.text = "Cantidad: ${producto.cantidad}"
        holder.binding.txtPrecio.text =
            "Precio: S/. %.2f".format(producto.precioUnitario)

        holder.binding.txtSubtotal.text =
            "Subtotal: S/. %.2f".format(producto.subtotal)
    }

    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<DetallePedidoResponse>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}