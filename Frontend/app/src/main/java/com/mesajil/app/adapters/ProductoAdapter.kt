package com.mesajil.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemProductoBinding
import com.mesajil.app.models.Producto
import com.bumptech.glide.Glide
import com.mesajil.app.R

class ProductoAdapter(
    private var listaProductos: List<Producto>,
    private val onProductoClick: (Producto) -> Unit,
    private val onAgregarClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {
    inner class ProductoViewHolder(
        private val binding: ItemProductoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(producto: Producto) {
            binding.txtNombre.text = producto.nombre
            binding.txtPrecio.text = "S/. %.2f".format(producto.precio)
            binding.txtStock.text = when {
                producto.stock <= 0 ->
                    "Sin stock"

                producto.stock <= 5 ->
                    "¡Últimas ${producto.stock} unidades!"

                else ->
                    "Stock: ${producto.stock}"
            }
            binding.btnAgregar.isEnabled = producto.stock > 0
            binding.btnAgregar.text = if (producto.stock > 0) {
                "Agregar"
            } else {
                "Sin stock"
            }
            if (!producto.urlImagen.isNullOrBlank()) {
                Glide.with(binding.root.context)
                    .load(producto.urlImagen)
                    .placeholder(R.drawable.logomesajil)
                    .error(R.drawable.logomesajil)
                    .into(binding.imgProducto)
            } else {
                binding.imgProducto.setImageResource(R.drawable.logomesajil)
            }
            binding.root.setOnClickListener {
                onProductoClick(producto)
            }
            binding.btnAgregar.setOnClickListener {
                onAgregarClick(producto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductoViewHolder,
        position: Int
    ) {
        holder.bind(listaProductos[position])
    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }

    fun actualizarLista(nuevaLista: List<Producto>) {
        listaProductos = nuevaLista
        notifyDataSetChanged()
    }
}