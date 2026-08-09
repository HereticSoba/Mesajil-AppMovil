package com.mesajil.app.ui.producto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemAdminProductoBinding
import com.mesajil.app.models.response.ProductoResponse
import com.bumptech.glide.Glide
import com.mesajil.app.R

class AdminProductoAdapter(
    private val lista: MutableList<ProductoResponse>,
    private val onProductoClick: (ProductoResponse) -> Unit
) : RecyclerView.Adapter<AdminProductoAdapter.ViewHolder>() {
    inner class ViewHolder(
        val binding: ItemAdminProductoBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminProductoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = lista[position]
        holder.binding.txtNombre.text = producto.nombre
        holder.binding.txtMarcaModelo.text = "${producto.marca} ${producto.modelo}"
        holder.binding.txtPrecio.text = "S/. %.2f".format(producto.precio)
        holder.binding.txtStock.text = "Stock: ${producto.stockActual}"
        if (!producto.urlImagen.isNullOrEmpty()) {
            val urlImagen ="https://mesajil-appmovil.onrender.com${producto.urlImagen}"
            Glide.with(holder.binding.imgProducto.context)
                .load(urlImagen)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
                .into(holder.binding.imgProducto)
        } else {
            holder.binding.imgProducto.setImageResource(
                R.drawable.ic_image
            )
        }
        holder.binding.root.setOnClickListener {
            onProductoClick(producto)
        }
    }

    override fun getItemCount(): Int = lista.size
    fun actualizarLista(nuevaLista: List<ProductoResponse>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}