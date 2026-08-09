package com.mesajil.app.ui.inventario

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemAdminInventarioBinding
import com.mesajil.app.models.Producto
import com.mesajil.app.models.response.InventarioResponse

class AdminInventarioAdapter(
    private val lista: MutableList<InventarioResponse>,
    private val productos: MutableList<Producto>,
    private val onInventarioClick: (InventarioResponse) -> Unit
) : RecyclerView.Adapter<AdminInventarioAdapter.ViewHolder>() {
    inner class ViewHolder(
        val binding: ItemAdminInventarioBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAdminInventarioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val inventario = lista[position]
        val producto = productos.find {
            it.idProducto == inventario.idProducto
        }
        holder.binding.txtProducto.text = producto?.nombre ?: "Producto #${inventario.idProducto}"
        holder.binding.txtInventario.text = "Inventario #${inventario.idInventario}"
        holder.binding.txtStockActual.text = "Stock actual: ${inventario.stockActual}"
        holder.binding.txtStockMinimo.text = "Stock mínimo: ${inventario.stockMinimo}"
        holder.binding.txtEstado.text =
            if (inventario.stockActual <= inventario.stockMinimo) {
                "Stock bajo"
            } else {
                "Stock disponible"
            }

        holder.binding.root.setOnClickListener {
            onInventarioClick(inventario)
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    fun actualizarLista(nuevaLista: List<InventarioResponse>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun actualizarProductos(nuevaLista: List<Producto>) {
        productos.clear()
        productos.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}