package iesvdc.segdodam.recyclerviewmotos

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.databinding.ActivityRegisterBinding
import iesvdc.segdodam.recyclerviewmotos.ui.auth.RegisterViewModel

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val username = binding.fullNameInput.text?.toString().orEmpty().trim()
            val email = binding.emailInput.text?.toString().orEmpty().trim()
            val password = binding.passwordInput.text?.toString().orEmpty()
            val confirm = binding.confirmPasswordInput.text?.toString().orEmpty()

            binding.fullNameInput.error = null
            binding.emailInput.error = null
            binding.passwordInput.error = null
            binding.confirmPasswordInput.error = null

            when {
                username.isBlank() -> binding.fullNameInput.error = "Introduce un nombre"
                email.isBlank() -> binding.emailInput.error = "Introduce un email"
                password.isBlank() -> binding.passwordInput.error = "Introduce una contraseña"
                password != confirm -> binding.confirmPasswordInput.error = "Las contraseñas no coinciden"
                else -> viewModel.register(username, email, password)
            }
        }

        binding.loginLink.setOnClickListener {
            finish()
        }

        viewModel.registerState.observe(this) { result ->
            if (result.isSuccess) {
                finish()
            } else {
                binding.emailInput.error = "Registro incorrecto"
            }
        }
    }
}
