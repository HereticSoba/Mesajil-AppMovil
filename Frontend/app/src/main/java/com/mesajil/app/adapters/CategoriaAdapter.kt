package com.mesajil.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.R
import com.mesajil.app.databinding.ItemCategoriaBinding
import com.mesajil.app.models.response.CategoriaResponse

class CategoriaAdapter(
    private val categorias: List<CategoriaResponse>,
    private val onCategoriaClick: (CategoriaResponse) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    inner class CategoriaViewHolder(
        private val binding: ItemCategoriaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categoria: CategoriaResponse) {
            binding.txtCategoria.text = categoria.nombre
            val imagenCategoria = when (categoria.nombre.lowercase()) {
                "laptops" -> R.drawable.cat_laptop
                "monitores" -> R.drawable.cat_monitor
                "periféricos", "perifericos" -> R.drawable.cat_perifericos
                "almacenamiento" -> R.drawable.cat_almacenamiento
                "componentes" -> R.drawable.cat_componentes
                else -> R.drawable.ic_producto
            }
            binding.imgCategoria.setImageResource(imagenCategoria)
            binding.root.setOnClickListener {
                onCategoriaClick(categoria)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriaViewHolder {
        val binding = ItemCategoriaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoriaViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CategoriaViewHolder,
        position: Int
    ) {
        holder.bind(categorias[position])
    }

    override fun getItemCount(): Int {
        return categorias.size
    }
}