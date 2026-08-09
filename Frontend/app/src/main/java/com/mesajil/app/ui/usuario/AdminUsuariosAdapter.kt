package com.mesajil.app.ui.usuario

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemAdminUsuarioBinding
import com.mesajil.app.models.response.UsuarioResponse

class AdminUsuariosAdapter(
    private val lista: MutableList<UsuarioResponse>,
    private val onUsuarioClick: (UsuarioResponse) -> Unit
) : RecyclerView.Adapter<AdminUsuariosAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemAdminUsuarioBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemAdminUsuarioBinding.inflate(
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
        val usuario = lista[position]
        holder.binding.txtNombre.text = "${usuario.nombres} ${usuario.apellidos}"
        holder.binding.txtCorreo.text = usuario.correo
        holder.binding.txtEstado.text = if (usuario.estado) {
            "Activo"
        } else {
            "Inactivo"
        }
        holder.binding.root.setOnClickListener {
            onUsuarioClick(usuario)
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    fun actualizarLista(nuevaLista: List<UsuarioResponse>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}