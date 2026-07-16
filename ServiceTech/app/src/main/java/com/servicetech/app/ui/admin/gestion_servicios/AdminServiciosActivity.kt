package com.servicetech.app.ui.admin.gestion_servicios

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.servicetech.app.R
import com.servicetech.app.data.model.TipoServicio
import com.servicetech.app.data.model.TipoServicioRequest
import com.servicetech.app.repository.TipoServicioRepository
import com.servicetech.app.ui.admin.ordenes.AdminOrdenesActivity
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Pantalla de administración de tipos de servicio.
 *
 * Permite listar, crear, editar y eliminar categorías de servicio desde el panel admin.
 */
class AdminServiciosActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var recyclerServicios: RecyclerView
    private lateinit var txtServiciosVacio: TextView
    private lateinit var btnAgregarTipo: Button

    private val tipoServicioRepository = TipoServicioRepository()
    private val tipos = mutableListOf<TipoServicio>()
    private lateinit var servicioAdapter: AdminServicioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se inicializa la vista y los listeners de navegación del módulo de servicios.
        enableEdgeToEdge()
        setContentView(R.layout.admin_servicios_activity)

        sessionManager = SessionManager(this)

        recyclerServicios = findViewById(R.id.recyclerServicios)
        txtServiciosVacio = findViewById(R.id.txtServiciosVacio)
        btnAgregarTipo = findViewById(R.id.btnAgregarTipo)

        recyclerServicios.layoutManager = LinearLayoutManager(this)
        servicioAdapter = AdminServicioAdapter(
            tipos,
            onEdit = { tipo -> mostrarDialogoTipo(tipo) },
            onDelete = { tipo -> confirmarEliminarTipo(tipo) }
        )
        recyclerServicios.adapter = servicioAdapter

        findViewById<android.view.View>(R.id.btnInicio).setOnClickListener { finish() }
        findViewById<android.view.View>(R.id.btnLogout).setOnClickListener { sessionManager.logout() }
        btnAgregarTipo.setOnClickListener { mostrarDialogoTipo(null) }

        cargarTipos()

        findViewById<android.view.View>(R.id.btnOrdenes).setOnClickListener {
            startActivity(Intent(this, AdminOrdenesActivity::class.java))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * Consulta los tipos de servicio disponibles en el backend.
     */
    private fun cargarTipos() {
        mostrarCargando()

        tipoServicioRepository.listarTipos().enqueue(object : Callback<List<TipoServicio>> {
            override fun onResponse(call: Call<List<TipoServicio>>, response: Response<List<TipoServicio>>) {
                if (response.isSuccessful) {
                    val nuevaLista = response.body().orEmpty()
                    if (nuevaLista.isEmpty()) {
                        mostrarVacio("No hay tipos de servicio registrados")
                    } else {
                        servicioAdapter.actualizarLista(nuevaLista)
                        mostrarLista()
                    }
                } else {
                    mostrarVacio("Error al obtener tipos de servicio (${response.code()})")
                }
            }

            override fun onFailure(call: Call<List<TipoServicio>>, t: Throwable) {
                mostrarVacio("Error de conexión con el servidor")
            }
        })
    }

    /**
     * Muestra un diálogo para crear o editar un tipo de servicio.
     */
    private fun mostrarDialogoTipo(tipo: TipoServicio?) {
        val view = layoutInflater.inflate(R.layout.dialog_tipo_servicio_form, null)
        val edtNombre = view.findViewById<TextInputEditText>(R.id.edtNombreTipoDialog)
        val edtDescripcion = view.findViewById<TextInputEditText>(R.id.edtDescripcionTipoDialog)

        val esEdicion = tipo != null
        if (esEdicion) {
            edtNombre.setText(tipo!!.nombre)
            edtDescripcion.setText(tipo.descripcion.orEmpty())
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(if (esEdicion) "Editar tipo de servicio" else "Agregar tipo de servicio")
            .setView(view)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton(if (esEdicion) "Actualizar" else "Crear", null)
            .create()

        dialog.setOnShowListener {
            val btnGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnGuardar.setOnClickListener {
                val nombre = edtNombre.text.toString().trim()
                val descripcion = edtDescripcion.text.toString().trim()

                if (nombre.isEmpty()) {
                    edtNombre.error = "Ingresa el nombre"
                    return@setOnClickListener
                }

                val request = TipoServicioRequest(nombre = nombre, descripcion = descripcion.ifEmpty { null })

                btnGuardar.isEnabled = false
                if (esEdicion) {
                    actualizarTipo(tipo!!.idTipoServicio, request, dialog, btnGuardar)
                } else {
                    crearTipo(request, dialog, btnGuardar)
                }
            }
        }

        dialog.show()
    }

    /**
     * Envía la petición para crear un nuevo tipo de servicio.
     */
    private fun crearTipo(request: TipoServicioRequest, dialog: AlertDialog, btnGuardar: Button) {
        tipoServicioRepository.crearTipo(request).enqueue(object : Callback<TipoServicio> {
            override fun onResponse(call: Call<TipoServicio>, response: Response<TipoServicio>) {
                btnGuardar.isEnabled = true
                if (response.isSuccessful && response.body() != null) {
                    servicioAdapter.agregarTipo(response.body()!!)
                    mostrarLista()
                    dialog.dismiss()
                    Toast.makeText(this@AdminServiciosActivity, "Tipo de servicio creado", Toast.LENGTH_SHORT).show()
                    return
                }
                Toast.makeText(
                    this@AdminServiciosActivity,
                    "No se pudo crear (${response.code()})",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<TipoServicio>, t: Throwable) {
                btnGuardar.isEnabled = true
                Toast.makeText(this@AdminServiciosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Envía la petición para actualizar un tipo de servicio existente.
     */
    private fun actualizarTipo(
        id: Int,
        request: TipoServicioRequest,
        dialog: AlertDialog,
        btnGuardar: Button
    ) {
        tipoServicioRepository.actualizarTipo(id, request).enqueue(object : Callback<TipoServicio> {
            override fun onResponse(call: Call<TipoServicio>, response: Response<TipoServicio>) {
                btnGuardar.isEnabled = true
                if (response.isSuccessful && response.body() != null) {
                    servicioAdapter.actualizarTipo(response.body()!!)
                    dialog.dismiss()
                    Toast.makeText(this@AdminServiciosActivity, "Tipo de servicio actualizado", Toast.LENGTH_SHORT).show()
                    return
                }
                Toast.makeText(
                    this@AdminServiciosActivity,
                    "No se pudo actualizar (${response.code()})",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<TipoServicio>, t: Throwable) {
                btnGuardar.isEnabled = true
                Toast.makeText(this@AdminServiciosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Solicita confirmación para eliminar un tipo de servicio.
     */
    private fun confirmarEliminarTipo(tipo: TipoServicio) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar tipo de servicio")
            .setMessage("¿Deseas eliminar \"${tipo.nombre}\"?")
            .setPositiveButton("Eliminar") { d, _ ->
                d.dismiss()
                eliminarTipo(tipo)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Elimina un tipo de servicio y muestra mensajes según la respuesta del backend.
     */
    private fun eliminarTipo(tipo: TipoServicio) {
        tipoServicioRepository.eliminarTipo(tipo.idTipoServicio).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    servicioAdapter.eliminarTipo(tipo)
                    if (tipos.isEmpty()) {
                        mostrarVacio("No hay tipos de servicio registrados")
                    }
                    Toast.makeText(this@AdminServiciosActivity, "Tipo de servicio eliminado", Toast.LENGTH_SHORT).show()
                    return
                }
                // 409: el backend lo bloqueó porque hay órdenes usando este tipo (ver TipoServicioController)
                val mensaje = if (response.code() == 409)
                    "No se puede eliminar: hay órdenes que usan este tipo"
                else
                    "No se pudo eliminar (${response.code()})"
                Toast.makeText(this@AdminServiciosActivity, mensaje, Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AdminServiciosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Muestra el estado de carga mientras se recuperan los tipos de servicio.
     */
    private fun mostrarCargando() {
        recyclerServicios.visibility = View.GONE
        txtServiciosVacio.visibility = View.VISIBLE
        txtServiciosVacio.text = "Cargando tipos de servicio..."
    }

    private fun mostrarLista() {
        txtServiciosVacio.visibility = View.GONE
        recyclerServicios.visibility = View.VISIBLE
    }

    private fun mostrarVacio(texto: String) {
        recyclerServicios.visibility = View.GONE
        txtServiciosVacio.visibility = View.VISIBLE
        txtServiciosVacio.text = texto
    }
}