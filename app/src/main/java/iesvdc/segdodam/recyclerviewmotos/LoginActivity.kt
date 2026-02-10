package iesvdc.segdodam.recyclerviewmotos

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.databinding.ActivityLoginBinding
import iesvdc.segdodam.recyclerviewmotos.ui.auth.AuthState
import iesvdc.segdodam.recyclerviewmotos.ui.auth.AuthViewModel

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Verificar si ya está autenticado
        if (authViewModel.isUserLoggedIn()) {
            navigateToMain()
            return
        }
        
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeAuthState()
    }

    private fun setupUI() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtUsuario.text.toString().trim()
            val password = binding.edtPassword.text.toString()
            authViewModel.login(email, password)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Funcionalidad de recuperación no implementada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeAuthState() {
        authViewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Success -> {
                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
                AuthState.LoggedOut -> {
                    // No hacer nada
                }
                AuthState.Loading -> {
                    // Ya se maneja con el loading
                }
            }
        }

        authViewModel.loading.observe(this) { isLoading ->
            binding.btnLogin.isEnabled = !isLoading
            binding.btnLogin.text = if (isLoading) "Iniciando..." else "Login"
        }

        authViewModel.errorMessage.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
