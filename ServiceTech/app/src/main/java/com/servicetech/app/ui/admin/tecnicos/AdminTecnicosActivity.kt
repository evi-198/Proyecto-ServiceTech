package com.servicetech.app.ui.admin.tecnicos

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.TecnicoResponse
import com.servicetech.app.repository.TecnicoRepository
import com.servicetech.app.ui.admin.gestion_servicios.AdminServiciosActivity
import com.servicetech.app.ui.admin.ordenes.AdminOrdenesActivity
import com.servicetech.app.ui.tecnico.asignados.AsignadosAdapter
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Pantalla de administración de técnicos.
 *
 * Muestra la lista de técnicos registrados y permite ver las órdenes activas asociadas a cada uno.
 */
class AdminTecnicosActivity : AppCompatActivity() {

    private lateinit var recyclerTecnicos: RecyclerView
    private lateinit var txtTecnicosVacio: TextView

    private lateinit var sessionManager: SessionManager
    private val tecnicoRepository = TecnicoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se inicializa la pantalla principal y los accesos rápidos del módulo.
        setContentView(R.layout.admin_tecnicos_activity)

        sessionManager = SessionManager(this)

        recyclerTecnicos = findViewById(R.id.recyclerTecnicos)
        txtTecnicosVacio = findViewById(R.id.txtTecnicosVacio)
        recyclerTecnicos.layoutManager = LinearLayoutManager(this)

        findViewById<android.view.View>(R.id.btnInicio).setOnClickListener { finish() }
        findViewById<android.view.View>(R.id.btnLogout).setOnClickListener {
            sessionManager.logout()
        }

        cargarTecnicos()

        findViewById<android.view.View>(R.id.btnOrdenes).setOnClickListener {
            startActivity(Intent(this, AdminOrdenesActivity::class.java))
            finish()
        }

        findViewById<android.view.View>(R.id.btnServicios).setOnClickListener {
            startActivity(Intent(this, AdminServiciosActivity::class.java))
            finish()
        }
    }

    /**
     * Carga la lista de técnicos desde el backend.
     */
    private fun cargarTecnicos() {
        tecnicoRepository.listarTecnicos().enqueue(object : Callback<List<TecnicoResponse>> {
            override fun onResponse(
                call: Call<List<TecnicoResponse>>,
                response: Response<List<TecnicoResponse>>
            ) {
                val tecnicos = response.body()
                if (response.isSuccessful && !tecnicos.isNullOrEmpty()) {
                    recyclerTecnicos.visibility = android.view.View.VISIBLE
                    txtTecnicosVacio.visibility = android.view.View.GONE
                    recyclerTecnicos.adapter = AdminTecnicosAdapter(tecnicos) { tecnico ->
                        mostrarOrdenesDelTecnico(tecnico)
                    }
                } else {
                    recyclerTecnicos.visibility = android.view.View.GONE
                    txtTecnicosVacio.visibility = android.view.View.VISIBLE
                    txtTecnicosVacio.text = "No hay técnicos registrados"
                }
            }

            override fun onFailure(call: Call<List<TecnicoResponse>>, t: Throwable) {
                txtTecnicosVacio.visibility = android.view.View.VISIBLE
                txtTecnicosVacio.text = "Error al cargar técnicos"
                Toast.makeText(this@AdminTecnicosActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Abre un diálogo con las órdenes activas del técnico seleccionado.
     */
    private fun mostrarOrdenesDelTecnico(tecnico: TecnicoResponse) {
        val recyclerDialog = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(context)
            setPadding(24, 24, 24, 24)
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Órdenes activas de ${tecnico.nombre}")
            .setView(recyclerDialog)
            .setNegativeButton("Cerrar", null)
            .create()

        tecnicoRepository.listarOrdenesDelTecnico(tecnico.idUsuario)
            .enqueue(object : Callback<List<com.servicetech.app.data.model.ServicioResponse>> {
                override fun onResponse(
                    call: Call<List<com.servicetech.app.data.model.ServicioResponse>>,
                    response: Response<List<com.servicetech.app.data.model.ServicioResponse>>
                ) {
                    val ordenes = response.body() ?: emptyList()
                    recyclerDialog.adapter = AsignadosAdapter(ordenes) { }
                }

                override fun onFailure(call: Call<List<com.servicetech.app.data.model.ServicioResponse>>, t: Throwable) {
                    Toast.makeText(this@AdminTecnicosActivity, "Error al cargar órdenes", Toast.LENGTH_SHORT).show()
                }
            })

        dialog.show()
    }
}