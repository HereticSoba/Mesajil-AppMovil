package com.mesajil.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemProductoDemandaBinding
import com.mesajil.app.models.response.ProductoMayorDemandaResponse

class ProductoDemandaAdapter(
    private var lista: List<ProductoMayorDemandaResponse>
) : RecyclerView.Adapter<ProductoDemandaAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemProductoDemandaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(producto: ProductoMayorDemandaResponse) {
            binding.txtNombreProducto.text = producto.nombre
            binding.txtCantidadVendida.text = "${producto.cantidadVendida} unidades vendidas"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemProductoDemandaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<ProductoMayorDemandaResponse>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}