package com.servicetech.app.ui.tecnico

import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.data.remote.ApiClient
import com.servicetech.app.ui.tecnico.actualizar.ActualizarEstadoActivity
import com.servicetech.app.ui.tecnico.asignados.AsignadosActivity
import com.servicetech.app.ui.tecnico.asignados.AsignadosAdapter
import com.servicetech.app.ui.tecnico.observaciones.ObservacionesActivity
import com.servicetech.app.ui.tecnico.reportes.MisReportesActivity
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Activity principal del panel del técnico.
 *
 * Permite al técnico acceder a las principales funcionalidades del sistema,
 * como visualizar los servicios asignados, actualizar el estado de los servicios,
 * registrar observaciones técnicas y consultar sus reportes. Además, muestra
 * una lista con los próximos servicios programados y gestiona la sesión del usuario.
 */

class TecnicoMainActivity : AppCompatActivity() {

    // ==========================
    // Variables de sesión
    // ==========================

    // Maneja la sesión del usuario (guardar, obtener datos y cerrar sesión)
    private lateinit var sessionManager: SessionManager


    // ==========================
    // Componentes de la interfaz
    // ==========================

    // Botón para cerrar la sesión del técnico
    private lateinit var btnLogout: LinearLayout

    // Muestra el mensaje de bienvenida con el nombre del técnico
    private lateinit var txtBienvenida: TextView

    // Lista donde se muestran los próximos servicios asignados
    private lateinit var recyclerProximosServicios: RecyclerView

    // Mensaje mostrado cuando no existen servicios próximos o ocurre un error
    private lateinit var txtProximosVacio: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la interfaz se dibuje detrás de la barra de estado
        enableEdgeToEdge()

        // Asocia esta Activity con su diseño XML
        setContentView(R.layout.tecnico_main_activity)


        // ==========================
        // Inicialización de la sesión
        // ==========================

        sessionManager = SessionManager(this)


        // ==========================
        // Configuración de bienvenida
        // ==========================

        txtBienvenida = findViewById(R.id.txtWelcomeTecnico)
        val nombre = sessionManager.getNombre()
        txtBienvenida.text = "Bienvenido, Técnico ${nombre ?: "Usuario"}"


        // ==========================
        // Configuración de botones principales
        // ==========================

        // Accesos principales del panel técnico
        findViewById<CardView>(R.id.btnAsignadosGrid).setOnClickListener { abrirPantallaAsignados() }
        findViewById<CardView>(R.id.btnActualizarGrid).setOnClickListener { abrirPantallaActualizar() }
        findViewById<CardView>(R.id.btnObservacionesGrid).setOnClickListener { abrirPantallaObservaciones() }
        findViewById<CardView>(R.id.btnReportesGrid).setOnClickListener { abrirPantallaReportes() }


        // ==========================
        // Configuración del submenú inferior
        // ==========================

        // Accesos rápidos disponibles desde la barra inferior
        findViewById<LinearLayout>(R.id.btnAsignados).setOnClickListener { abrirPantallaAsignados() }
        findViewById<LinearLayout>(R.id.btnReportes).setOnClickListener { abrirPantallaReportes() }


        // ==========================
        // Configuración del botón cerrar sesión
        // ==========================

        btnLogout = findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener {
            sessionManager.logout()
        }


        // ==========================
        // Configuración de la lista de próximos servicios
        // ==========================

        recyclerProximosServicios = findViewById(R.id.recyclerProximosServicios)
        recyclerProximosServicios.layoutManager = LinearLayoutManager(this)

        txtProximosVacio = findViewById(R.id.txtProximosVacio)


        // ==========================
        // Ajuste automático de márgenes del sistema
        // ==========================

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    override fun onResume() {
        super.onResume()
        // Refresca la lista cada vez que la pantalla vuelve a primer plano
        // (por ejemplo, al volver desde ActualizarEstadoActivity)
        cargarProximosServicios()
    }


    // ==========================
    // Navegación entre pantallas
    // ==========================

    // Abre la pantalla de servicios asignados
    private fun abrirPantallaAsignados() {
        startActivity(Intent(this, AsignadosActivity::class.java))
    }

    // Abre la pantalla para actualizar el estado de un servicio
    private fun abrirPantallaActualizar() {
        startActivity(Intent(this, ActualizarEstadoActivity::class.java))
    }

    // Abre la pantalla de observaciones técnicas
    private fun abrirPantallaObservaciones() {
        startActivity(Intent(this, ObservacionesActivity::class.java))
    }

    // Abre la pantalla de reportes del técnico
    private fun abrirPantallaReportes() {
        startActivity(Intent(this, MisReportesActivity::class.java))
    }


    // ==========================
    // Carga de información
    // ==========================

    /**
     * Obtiene desde el servidor los próximos servicios asignados
     * al técnico autenticado y actualiza la lista mostrada en pantalla.
     */
    private fun cargarProximosServicios() {

        val idTecnico = sessionManager.getUserId()

        if (idTecnico <= 0) {
            txtProximosVacio.text = "Sesión no válida"
            txtProximosVacio.visibility = View.VISIBLE
            return
        }

        ApiClient.apiService.getServiciosProximos(idTecnico)
            .enqueue(object : Callback<List<ServicioResponse>> {

                override fun onResponse(
                    call: Call<List<ServicioResponse>>,
                    response: Response<List<ServicioResponse>>
                ) {

                    if (response.isSuccessful && response.body() != null) {

                        val lista = response.body()!!

                        if (lista.isEmpty()) {
                            txtProximosVacio.text = "No hay servicios próximos"
                            txtProximosVacio.visibility = View.VISIBLE
                            recyclerProximosServicios.visibility = View.GONE
                        } else {
                            txtProximosVacio.visibility = View.GONE
                            recyclerProximosServicios.visibility = View.VISIBLE
                            recyclerProximosServicios.adapter =
                                AsignadosAdapter(lista) { /* No requiere acción */ }
                        }
                        return
                    }

                    txtProximosVacio.text = "No se pudieron cargar los servicios próximos"
                    txtProximosVacio.visibility = View.VISIBLE
                    recyclerProximosServicios.visibility = View.GONE
                }

                override fun onFailure(call: Call<List<ServicioResponse>>, t: Throwable) {
                    txtProximosVacio.text = "Error de conexión"
                    txtProximosVacio.visibility = View.VISIBLE
                    recyclerProximosServicios.visibility = View.GONE
                }
            })
    }
}