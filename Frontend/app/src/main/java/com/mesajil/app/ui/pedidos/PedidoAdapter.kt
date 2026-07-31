package com.mesajil.app.ui.pedidos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesajil.app.databinding.ItemPedidoBinding
import com.mesajil.app.models.response.PedidoResponse

class PedidoAdapter(
    private val lista: MutableList<PedidoResponse>
) : RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {
    inner class ViewHolder(
        val binding: ItemPedidoBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPedidoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pedido = lista[position]
        holder.binding.txtPedido.text = "Pedido #${pedido.idPedido}"
        holder.binding.txtFecha.text = pedido.fechaPedido
        holder.binding.txtEstado.text = pedido.estadoPedido
        holder.binding.txtTotal.text = "S/. %.2f".format(pedido.total)
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<PedidoResponse>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}