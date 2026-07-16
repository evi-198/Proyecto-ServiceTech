package com.servicetech.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.servicetech.app.ui.admin.AdminMainActivity
import com.servicetech.app.ui.auth.LoginActivity
import com.servicetech.app.ui.cliente.ClienteMainActivity
import com.servicetech.app.ui.tecnico.TecnicoMainActivity
import com.servicetech.app.utils.SessionManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(this)
        val rol = sessionManager.getRol()?.uppercase()?.trim()

        val intent = when {
            rol == null -> Intent(this, LoginActivity::class.java)
            rol.contains("CLIENTE") -> Intent(this, ClienteMainActivity::class.java)
            rol.contains("TECNICO") -> Intent(this, TecnicoMainActivity::class.java)
            rol.contains("ADMINISTRADOR") -> Intent(this, AdminMainActivity::class.java)
            else -> Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()

    }
}
