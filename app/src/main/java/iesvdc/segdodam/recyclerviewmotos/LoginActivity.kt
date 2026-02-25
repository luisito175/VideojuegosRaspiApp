package iesvdc.segdodam.recyclerviewmotos

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.data.auth.SessionManager
import iesvdc.segdodam.recyclerviewmotos.databinding.ActivityLoginBinding
import iesvdc.segdodam.recyclerviewmotos.ui.auth.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!sessionManager.getAccessToken().isNullOrBlank()) {
            openMain()
            return
        }

        if (!sessionManager.getRefreshToken().isNullOrBlank()) {
            viewModel.tryRefresh()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtUsuario.text?.toString().orEmpty().trim()
            val password = binding.edtPassword.text?.toString().orEmpty()
            binding.edtUsuario.error = null
            binding.edtPassword.error = null

            when {
                email.isBlank() -> binding.edtUsuario.error = "Introduce un email"
                password.isBlank() -> binding.edtPassword.error = "Introduce una contraseña"
                else -> viewModel.login(email, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(this) { result ->
            if (result.isSuccess) {
                openMain()
            } else {
                binding.edtPassword.error = "Credenciales inválidas"
            }
        }

        viewModel.refreshState.observe(this) { result ->
            if (result.isSuccess && result.getOrNull() == true) {
                openMain()
            }
        }
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
