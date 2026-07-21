package com.mesajil.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemProductoBinding
import com.mesajil.app.models.Producto

class ProductoAdapter(
    private val listaProductos: List<Producto>,
    private val onProductoClick: (Producto) -> Unit,
    private val onAgregarClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {
    inner class ProductoViewHolder(
        private val binding: ItemProductoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(producto: Producto) {
            binding.txtNombreProducto.text = producto.nombre
            binding.txtPrecio.text = "S/. %.2f".format(producto.precio)
            binding.txtStock.text = "Stock: ${producto.stock}"
            binding.root.setOnClickListener {
                onProductoClick(producto)
            }
            binding.btnAgregar.setOnClickListener {
                onAgregarClick(producto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductoViewHolder,
        position: Int) {
        holder.bind(listaProductos[position])
    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }
}