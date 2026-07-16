package com.servicetech.app.ui.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.servicetech.app.MainActivity
import com.servicetech.app.R
import com.servicetech.app.data.model.LoginRequest
import com.servicetech.app.data.model.LoginResponse
import com.servicetech.app.repository.UsuarioRepository
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Permite al usuario autenticarse en el sistema
 * mediante sus credenciales de acceso.
 */
class LoginActivity : AppCompatActivity() {

    // ==========================
    // Componentes de la interfaz
    // ==========================

    private lateinit var edtCorreo: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtError: TextView

    // ==========================
    // Variables de sesión
    // ==========================

    private lateinit var sessionManager: SessionManager
    private val repository = UsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_login_activity)

        // ==========================
        // Configuración de la interfaz
        // ==========================

        // Personaliza el color de la barra de estado.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor =
                ContextCompat.getColor(this, R.color.blue_500)
        }

        // Se enlazan los componentes del layout con la Activity.
        edtCorreo = findViewById(R.id.edtCorreo)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtError = findViewById(R.id.txtError)

        // Inicializa el administrador de sesión.
        sessionManager = SessionManager(this)

        // Inicia el proceso de autenticación al presionar el botón.
        btnLogin.setOnClickListener {
            login()
        }
    }

    // ==========================
    // Inicio de sesión
    // ==========================

    /**
     * Valida las credenciales del usuario
     * y solicita la autenticación al servidor.
     */
    private fun login() {

        // Oculta cualquier mensaje de error anterior.
        txtError.visibility = View.GONE

        val correo = edtCorreo.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        // Verifica que ambos campos hayan sido completados.
        if (correo.isEmpty() || password.isEmpty()) {
            mostrarError("Completa todos los campos")
            return
        }

        // Construye la solicitud que será enviada al backend.
        val request = LoginRequest(
            correo = correo,
            password = password
        )

        // Envía la solicitud de autenticación al servidor.
        repository.login(request).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {

                if (response.isSuccessful && response.body() != null) {

                    val user = response.body()!!

                    // Guarda la información del usuario para mantener la sesión.
                    sessionManager.saveUser(
                        user.idUsuario,
                        user.nombre,
                        user.correo,
                        user.rol,
                        user.token
                    )

                    // Redirige al panel correspondiente según el rol del usuario.
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            MainActivity::class.java
                        )
                    )
                    finish()

                } else {

                    // Muestra un mensaje cuando las credenciales son incorrectas.
                    mostrarError("Correo o contraseña incorrectos")
                }
            }

            override fun onFailure(
                call: Call<LoginResponse>,
                t: Throwable
            ) {

                // Informa cuando no es posible establecer conexión con el servidor.
                mostrarError("Error de conexión con el servidor")
            }
        })
    }

    /**
     * Muestra un mensaje de error
     * debajo del formulario de inicio de sesión.
     */
    private fun mostrarError(mensaje: String) {

        // Actualiza el mensaje y lo hace visible.
        txtError.text = mensaje
        txtError.visibility = View.VISIBLE
    }
}