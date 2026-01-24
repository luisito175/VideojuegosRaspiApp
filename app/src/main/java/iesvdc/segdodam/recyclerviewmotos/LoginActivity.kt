package iesvdc.segdodam.recyclerviewmotos

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import iesvdc.segdodam.recyclerviewmotos.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsuario.text.toString()
            val password = binding.edtPassword.text.toString()

            // Comprobación de usuario y contraseña (hardcodeado por ahora)
            if (username == "admin" && password == "1234") {
                Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show()
                // Navegar a la actividad principal
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Cierra LoginActivity para que el usuario no pueda volver con el botón de atrás
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            Toast.makeText(this, "Funcionalidad de registro no implementada", Toast.LENGTH_SHORT).show()
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Funcionalidad de recuperación no implementada", Toast.LENGTH_SHORT).show()
        }
    }
}
