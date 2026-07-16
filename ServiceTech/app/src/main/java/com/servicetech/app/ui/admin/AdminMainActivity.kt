package com.servicetech.app.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioAdminResponse
import com.servicetech.app.data.model.TecnicoResponse
import com.servicetech.app.repository.OrdenRepository
import com.servicetech.app.repository.TecnicoRepository
import com.servicetech.app.ui.admin.gestion_servicios.AdminServiciosActivity
import com.servicetech.app.ui.admin.gestion_usuarios.AdminUsuariosActivity
import com.servicetech.app.ui.admin.ordenes.AdminOrdenesActivity
import com.servicetech.app.ui.admin.tecnicos.AdminTecnicosActivity
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Pantalla principal del panel de administración.
 *
 * Muestra una vista general del sistema con accesos rápidos a usuarios,
 * técnicos, órdenes y servicios, además de alertas de seguimiento.
 */
class AdminMainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var btnLogout: LinearLayout
    private lateinit var cardUsuarios: CardView
    private lateinit var cardTecnicos: CardView
    private lateinit var cardOrdenes: CardView
    private lateinit var cardServicios: CardView
    private lateinit var btnServiciosMenu: LinearLayout
    private lateinit var btnOrdenesMenu: LinearLayout
    private lateinit var txtBienvenida: TextView

    private lateinit var contenedorAlertas: LinearLayout
    private lateinit var txtSinAlertas: TextView

    private var ordenesPendientesCache: List<ServicioAdminResponse>? = null
    private var tecnicosSaturadosCache: List<TecnicoResponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se inicializa la pantalla y se vinculan las vistas del layout.

        // Permite que la UI se dibuje detrás de la barra de estado (diseño moderno)
        enableEdgeToEdge()
        // Conecta esta Activity con su layout XML
        setContentView(R.layout.admin_main_activity)

        // Inicializamos el gestor de sesión
        sessionManager = SessionManager(this)

        // Bienvenida dinámica: toma el nombre guardado al iniciar sesión.
        txtBienvenida = findViewById(R.id.txtWelcomeAdmin)
        val nombre = sessionManager.getNombre()
        txtBienvenida.text = "Bienvenido, Admin ${nombre ?: "Usuario"}"

        // Vinculamos las vistas del XML con el código Kotlin
        btnLogout = findViewById(R.id.btnLogout)
        cardUsuarios = findViewById(R.id.cardUsuarios)
        cardTecnicos = findViewById(R.id.cardTecnicos)
        cardOrdenes = findViewById(R.id.cardOrdenes)
        btnOrdenesMenu = findViewById(R.id.btnOrdenes)
        cardServicios = findViewById(R.id.cardServicios)
        btnServiciosMenu = findViewById(R.id.btnServicios)

        contenedorAlertas = findViewById(R.id.contenedorAlertas)
        txtSinAlertas = findViewById(R.id.txtSinAlertas)

        cargarAlertas()

        // Botón principal de usuarios: abre la pantalla para gestionar cuentas del sistema.
        cardUsuarios.setOnClickListener {
            startActivity(Intent(this, AdminUsuariosActivity::class.java))
        }

        // Botón principal de técnicos: abre la pantalla para consultar y administrar técnicos.
        cardTecnicos.setOnClickListener {
            startActivity(Intent(this, AdminTecnicosActivity::class.java))
        }

        // Botón principal de órdenes: dirige al administrador a la gestión de órdenes.
        cardOrdenes.setOnClickListener {
            startActivity(Intent(this, AdminOrdenesActivity::class.java))
        }

        // Botón principal de servicios: abre la administración de tipos de servicio.
        cardServicios.setOnClickListener {
            startActivity(Intent(this, AdminServiciosActivity::class.java))
        }

        // Botón de cierre de sesión: finaliza la sesión activa del administrador.
        btnLogout.setOnClickListener {
            sessionManager.logout()
        }

        // Submenú inferior de órdenes: acceso rápido a la misma pantalla de órdenes.
        btnOrdenesMenu.setOnClickListener {
            startActivity(Intent(this, AdminOrdenesActivity::class.java))
        }

        // Submenú inferior de servicios: acceso rápido a la gestión de servicios.
        btnServiciosMenu.setOnClickListener {
            startActivity(Intent(this, AdminServiciosActivity::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // ════════════════════════════════════════════════════════════
    // ALERTAS DE SISTEMA
    // ════════════════════════════════════════════════════════════

    /**
     * Solicita los datos necesarios para construir las alertas del panel.
     * Consulta órdenes pendientes y técnicos saturados para mostrarlos al administrador.
     */
    private fun cargarAlertas() {
        OrdenRepository().listarOrdenes("PENDIENTE").enqueue(object : Callback<List<ServicioAdminResponse>> {
            override fun onResponse(
                call: Call<List<ServicioAdminResponse>>,
                response: Response<List<ServicioAdminResponse>>
            ) {
                Toast.makeText(this@AdminMainActivity, "Órdenes: código ${response.code()}", Toast.LENGTH_LONG).show()
                ordenesPendientesCache = response.body().orEmpty()
                intentarMostrarAlertas()
            }

            override fun onFailure(call: Call<List<ServicioAdminResponse>>, t: Throwable) {
                Toast.makeText(this@AdminMainActivity, "Órdenes FALLÓ: ${t.message}", Toast.LENGTH_LONG).show()
                ordenesPendientesCache = emptyList()
                intentarMostrarAlertas()
            }
        })

        TecnicoRepository().listarTecnicos().enqueue(object : Callback<List<TecnicoResponse>> {
            override fun onResponse(
                call: Call<List<TecnicoResponse>>,
                response: Response<List<TecnicoResponse>>
            ) {
                Toast.makeText(this@AdminMainActivity, "Técnicos: código ${response.code()}", Toast.LENGTH_LONG).show()
                tecnicosSaturadosCache = response.body().orEmpty().filter { it.saturado }
                intentarMostrarAlertas()
            }

            override fun onFailure(call: Call<List<TecnicoResponse>>, t: Throwable) {
                Toast.makeText(this@AdminMainActivity, "Técnicos FALLÓ: ${t.message}", Toast.LENGTH_LONG).show()
                tecnicosSaturadosCache = emptyList()
                intentarMostrarAlertas()
            }
        })
    }

    /**
     * Combina las alertas obtenidas y las muestra en la interfaz.
     * Espera a que ambas consultas hayan terminado antes de renderizar el contenido.
     */
    private fun intentarMostrarAlertas() {
        val ordenes = ordenesPendientesCache ?: return
        val tecnicos = tecnicosSaturadosCache ?: return

        // Borra solo las alertas agregadas dinámicamente, sin tocar txtSinAlertas
        for (i in contenedorAlertas.childCount - 1 downTo 0) {
            val hijo = contenedorAlertas.getChildAt(i)
            if (hijo.id != R.id.txtSinAlertas) {
                contenedorAlertas.removeView(hijo)
            }
        }

        val alertasOrdenes = ordenes.map { orden ->
            Triple(
                "Orden pendiente: ${orden.tipoServicio} — ${orden.clienteNombre}",
                R.drawable.ordenes_icono,
                "#F59E0B"
            )
        }

        val alertasTecnicos = tecnicos.map { tecnico ->
            Triple(
                "${tecnico.nombre} está en su límite (${tecnico.ordenesActivas}/${tecnico.limiteOrdenes})",
                R.drawable.tecnico_icono,
                "#DC2626"
            )
        }

        val combinadas = (alertasOrdenes + alertasTecnicos).take(5)

        if (combinadas.isEmpty()) {
            txtSinAlertas.visibility = View.VISIBLE
            return
        }

        txtSinAlertas.visibility = View.GONE


        combinadas.forEach { (texto, icono, colorHex) ->
            val itemView = layoutInflater.inflate(R.layout.admin_alerta_sistema, contenedorAlertas, false)
            itemView.findViewById<TextView>(R.id.txtAlerta).text = texto
            itemView.findViewById<ImageView>(R.id.iconoAlerta).apply {
                setImageResource(icono)
                imageTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(colorHex))
            }
            itemView.findViewById<View>(R.id.indicadorAlerta).setBackgroundColor(android.graphics.Color.parseColor(colorHex))
            contenedorAlertas.addView(itemView)
        }
    }
}