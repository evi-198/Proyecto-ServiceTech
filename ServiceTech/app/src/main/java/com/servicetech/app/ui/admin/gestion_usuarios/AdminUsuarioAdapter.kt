package com.servicetech.app.ui.admin.gestion_usuarios

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.servicetech.app.R
import com.servicetech.app.data.model.Usuario

/**
 * Adapter para mostrar usuarios en la lista del panel administrativo.
 *
 * Cada elemento permite editar o eliminar la cuenta asociada.
 */
class AdminUsuarioAdapter(
    private val usuarios: MutableList<Usuario>,
    private val onEdit: (Usuario) -> Unit,
    private val onDelete: (Usuario) -> Unit
) : RecyclerView.Adapter<AdminUsuarioAdapter.ViewHolder>() {

    class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombreUsuario)
        val txtCorreo: TextView = view.findViewById(R.id.txtCorreoUsuario)
        val txtTelefono: TextView = view.findViewById(R.id.txtTelefonoUsuario)
        val txtRol: TextView = view.findViewById(R.id.txtRolUsuario)
        val btnEditar: MaterialButton = view.findViewById(R.id.btnEditarUsuario)
        val btnEliminar: MaterialButton = view.findViewById(R.id.btnEliminarUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_usuario_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.txtNombre.text = usuario.nombre
        holder.txtCorreo.text = usuario.correo
        holder.txtTelefono.text = usuario.telefono?.let { "Tel: $it" } ?: "Sin teléfono"
        holder.txtRol.text = usuario.rol
        holder.btnEditar.setOnClickListener { onEdit(usuario) }
        holder.btnEliminar.setOnClickListener { onDelete(usuario) }
    }

    override fun getItemCount(): Int = usuarios.size

    fun actualizarUsuarios(nuevaLista: List<Usuario>) {
        usuarios.clear()
        usuarios.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun agregarUsuario(usuario: Usuario) {
        usuarios.add(0, usuario)
        notifyItemInserted(0)
    }

    fun actualizarUsuario(usuario: Usuario) {
        val index = usuarios.indexOfFirst { it.idUsuario == usuario.idUsuario }
        if (index >= 0) {
            usuarios[index] = usuario
            notifyItemChanged(index)
        }
    }

    fun eliminarUsuario(usuario: Usuario) {
        val index = usuarios.indexOfFirst { it.idUsuario == usuario.idUsuario }
        if (index >= 0) {
            usuarios.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
