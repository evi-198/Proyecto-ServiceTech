package com.servicetech.app.ui.admin.gestion_usuarios

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.Usuario
import com.servicetech.app.data.model.UsuarioRequest
import com.servicetech.app.repository.UsuarioRepository
import com.servicetech.app.ui.admin.gestion_servicios.AdminServiciosActivity
import com.servicetech.app.ui.admin.ordenes.AdminOrdenesActivity
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Pantalla de administración de usuarios del sistema.
 *
 * Permite listar, crear, editar y eliminar cuentas de usuario, así como navegar
 * hacia otras secciones del panel administrativo.
 */
class AdminUsuariosActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var recyclerUsuarios: RecyclerView
    private lateinit var txtUsuariosVacio: TextView
    private lateinit var btnInicio: LinearLayout
    private lateinit var btnLogout: LinearLayout
    private lateinit var txtTitulo: TextView
    private lateinit var btnAgregarUsuario: Button

    private val usuarioRepository = UsuarioRepository()
    private val usuarios = mutableListOf<Usuario>()
    private lateinit var usuarioAdapter: AdminUsuarioAdapter
    private val roles = listOf("ADMINISTRADOR", "TECNICO", "CLIENTE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se inicializa la vista principal y se configuran los listeners de navegación.
        enableEdgeToEdge()
        setContentView(R.layout.admin_usuarios_activity)

        sessionManager = SessionManager(this)

        txtTitulo = findViewById(R.id.txtUsuariosTitulo)
        recyclerUsuarios = findViewById(R.id.recyclerUsuarios)
        txtUsuariosVacio = findViewById(R.id.txtUsuariosVacio)
        btnInicio = findViewById(R.id.btnInicio)
        btnLogout = findViewById(R.id.btnLogout)
        btnAgregarUsuario = findViewById(R.id.btnAgregarUsuario)

        recyclerUsuarios.layoutManager = LinearLayoutManager(this)
        usuarioAdapter = AdminUsuarioAdapter(
            usuarios,
            onEdit = { usuario -> mostrarDialogoUsuario(usuario) },
            onDelete = { usuario -> confirmarEliminarUsuario(usuario) }
        )
        recyclerUsuarios.adapter = usuarioAdapter

        txtTitulo.text = "Usuarios del sistema"

        btnInicio.setOnClickListener { finish() }
        btnLogout.setOnClickListener { sessionManager.logout() }
        btnAgregarUsuario.setOnClickListener { mostrarDialogoUsuario(null) }

        cargarUsuarios()

        findViewById<android.view.View>(R.id.btnOrdenes).setOnClickListener {
            startActivity(Intent(this, AdminOrdenesActivity::class.java))
            finish()
        }

        findViewById<android.view.View>(R.id.btnServicios).setOnClickListener {
            startActivity(Intent(this, AdminServiciosActivity::class.java))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * Carga la lista de usuarios desde el backend y la muestra en el RecyclerView.
     */
    private fun cargarUsuarios() {
        mostrarCargando()

        usuarioRepository.listarUsuarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    val nuevosUsuarios = response.body().orEmpty()
                    if (nuevosUsuarios.isEmpty()) {
                        mostrarVacio("No hay usuarios registrados")
                    } else {
                        usuarioAdapter.actualizarUsuarios(nuevosUsuarios)
                        mostrarLista()
                    }
                } else {
                    mostrarVacio("Error al obtener usuarios (${response.code()})")
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                mostrarVacio("Error de conexión con el servidor")
            }
        })
    }

    /**
     * Muestra un diálogo para crear o editar un usuario.
     */
    private fun mostrarDialogoUsuario(usuario: Usuario?) {
        val view = layoutInflater.inflate(R.layout.dialog_usuario_form, null)
        val edtNombre = view.findViewById<EditText>(R.id.edtNombreUsuarioDialog)
        val edtCorreo = view.findViewById<EditText>(R.id.edtCorreoUsuarioDialog)
        val edtPassword = view.findViewById<EditText>(R.id.edtPasswordUsuarioDialog)
        val edtTelefono = view.findViewById<EditText>(R.id.edtTelefonoUsuarioDialog)
        val spinnerRol = view.findViewById<Spinner>(R.id.spinnerRolUsuarioDialog)

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        spinnerRol.adapter = spinnerAdapter

        val esEdicion = usuario != null
        if (esEdicion) {
            edtNombre.setText(usuario!!.nombre)
            edtCorreo.setText(usuario.correo)
            edtTelefono.setText(usuario.telefono.orEmpty())
            val rolIndex = roles.indexOfFirst { it.equals(usuario.rol, ignoreCase = true) }
            spinnerRol.setSelection(if (rolIndex >= 0) rolIndex else 0)
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(if (esEdicion) "Editar usuario" else "Agregar usuario")
            .setView(view)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton(if (esEdicion) "Actualizar" else "Crear", null)
            .create()

        dialog.setOnShowListener {
            val btnGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnGuardar.setOnClickListener {
                val nombre = edtNombre.text.toString().trim()
                val correo = edtCorreo.text.toString().trim()
                val password = edtPassword.text.toString().trim()
                val telefono = edtTelefono.text.toString().trim().ifEmpty { null }
                val rol = spinnerRol.selectedItem?.toString().orEmpty()

                when {
                    nombre.isEmpty() -> edtNombre.error = "Ingresa el nombre"
                    correo.isEmpty() -> edtCorreo.error = "Ingresa el correo"
                    password.isEmpty() -> edtPassword.error = "Ingresa la contraseña"
                    rol.isEmpty() -> Toast.makeText(this, "Selecciona un rol", Toast.LENGTH_SHORT).show()
                    else -> {
                        val request = UsuarioRequest(
                            nombre = nombre,
                            correo = correo,
                            password = password,
                            telefono = telefono,
                            rol = rol
                        )

                        btnGuardar.isEnabled = false
                        if (esEdicion) {
                            actualizarUsuario(usuario!!.idUsuario, request, dialog, btnGuardar)
                        } else {
                            crearUsuario(request, dialog, btnGuardar)
                        }
                    }
                }
            }
        }

        dialog.show()
    }

    /**
     * Envía la petición para crear un usuario nuevo.
     */
    private fun crearUsuario(request: UsuarioRequest, dialog: AlertDialog, btnGuardar: Button) {
        usuarioRepository.crearUsuario(request).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                btnGuardar.isEnabled = true
                if (response.isSuccessful && response.body() != null) {
                    val usuarioCreado = response.body()!!
                    usuarioAdapter.agregarUsuario(usuarioCreado)
                    mostrarLista()
                    dialog.dismiss()
                    Toast.makeText(this@AdminUsuariosActivity, "Usuario creado", Toast.LENGTH_SHORT).show()
                    return
                }
                Toast.makeText(
                    this@AdminUsuariosActivity,
                    "No se pudo crear el usuario (${response.code()})",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                btnGuardar.isEnabled = true
                Toast.makeText(this@AdminUsuariosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Envía la petición para actualizar un usuario existente.
     */
    private fun actualizarUsuario(
        id: Int,
        request: UsuarioRequest,
        dialog: AlertDialog,
        btnGuardar: Button
    ) {
        usuarioRepository.actualizarUsuario(id, request).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                btnGuardar.isEnabled = true
                if (response.isSuccessful && response.body() != null) {
                    val usuarioActualizado = response.body()!!
                    usuarioAdapter.actualizarUsuario(usuarioActualizado)
                    dialog.dismiss()
                    Toast.makeText(this@AdminUsuariosActivity, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                    return
                }
                Toast.makeText(
                    this@AdminUsuariosActivity,
                    "No se pudo actualizar el usuario (${response.code()})",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                btnGuardar.isEnabled = true
                Toast.makeText(this@AdminUsuariosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Solicita confirmación antes de eliminar un usuario.
     */
    private fun confirmarEliminarUsuario(usuario: Usuario) {
        if (usuario.idUsuario == sessionManager.getUserId()) {
            Toast.makeText(this, "No puedes eliminar tu propia cuenta", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Eliminar usuario")
            .setMessage("¿Deseas eliminar a ${usuario.nombre}?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                dialog.dismiss()
                eliminarUsuario(usuario)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Elimina un usuario desde el backend y actualiza la lista local.
     */
    private fun eliminarUsuario(usuario: Usuario) {
        usuarioRepository.eliminarUsuario(usuario.idUsuario).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    usuarioAdapter.eliminarUsuario(usuario)
                    if (usuarios.isEmpty()) {
                        mostrarVacio("No hay usuarios registrados")
                    }
                    Toast.makeText(this@AdminUsuariosActivity, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                    return
                }
                Toast.makeText(
                    this@AdminUsuariosActivity,
                    "No se pudo eliminar el usuario (${response.code()})",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AdminUsuariosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Muestra el estado de carga mientras se obtienen los usuarios.
     */
    private fun mostrarCargando() {
        recyclerUsuarios.visibility = View.GONE
        txtUsuariosVacio.visibility = View.VISIBLE
        txtUsuariosVacio.text = "Cargando usuarios..."
    }

    /**
     * Muestra la lista de usuarios cuando hay datos disponibles.
     */
    private fun mostrarLista() {
        txtUsuariosVacio.visibility = View.GONE
        recyclerUsuarios.visibility = View.VISIBLE
    }

    /**
     * Muestra un mensaje vacío cuando no hay usuarios o hubo un error.
     */
    private fun mostrarVacio(texto: String) {
        recyclerUsuarios.visibility = View.GONE
        txtUsuariosVacio.visibility = View.VISIBLE
        txtUsuariosVacio.text = texto
    }
}
