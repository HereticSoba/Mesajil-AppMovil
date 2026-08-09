package com.mesajil.app.ui.categoria

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemAdminCategoriaBinding
import com.mesajil.app.models.response.CategoriaResponse

class AdminCategoriaAdapter(
    private val lista: MutableList<CategoriaResponse>,
    private val onCategoriaClick: (CategoriaResponse) -> Unit
) : RecyclerView.Adapter<AdminCategoriaAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemAdminCategoriaBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAdminCategoriaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val categoria = lista[position]
        holder.binding.txtNombre.text =
            categoria.nombre
        holder.binding.txtDescripcion.text =
            categoria.descripcion ?: "Sin descripción"
        holder.binding.txtEstado.text =
            if (categoria.estado) {
                "Activa"
            } else {
                "Inactiva"
            }
        holder.binding.root.setOnClickListener {
            onCategoriaClick(categoria)
        }
    }

    override fun getItemCount(): Int =
        lista.size

    fun actualizarLista(
        nuevaLista: List<CategoriaResponse>
    ) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}