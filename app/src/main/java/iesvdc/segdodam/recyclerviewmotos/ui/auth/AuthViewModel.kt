package iesvdc.segdodam.recyclerviewmotos.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class AuthState {
    data class Success(val userId: String) : AuthState()
    object LoggedOut : AuthState()
    object Loading : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.LoggedOut)
    val authState: LiveData<AuthState> = _authState

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun login(email: String, password: String) {
        if (!validateInputs(email, password)) return

        _loading.value = true
        _errorMessage.value = null

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                _loading.value = false
                _authState.value = AuthState.Success(authResult.user?.uid ?: "")
            }
            .addOnFailureListener { exception ->
                _loading.value = false
                _errorMessage.value = exception.message ?: "Error desconocido"
            }
    }

    fun register(email: String, password: String, fullName: String) {
        if (!validateInputs(email, password)) return

        if (password.length < 6) {
            _errorMessage.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        _loading.value = true
        _errorMessage.value = null

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid ?: return@addOnSuccessListener

                // Guardar usuario en Firestore
                val userMap = mapOf(
                    "userId" to userId,
                    "email" to email,
                    "fullName" to fullName,
                    "createdAt" to System.currentTimeMillis()
                )

                firebaseFirestore.collection("users").document(userId)
                    .set(userMap)
                    .addOnSuccessListener {
                        _loading.value = false
                        _authState.value = AuthState.Success(userId)
                    }
                    .addOnFailureListener { exception ->
                        _loading.value = false
                        _errorMessage.value = "Error al guardar usuario: ${exception.message}"
                    }
            }
            .addOnFailureListener { exception ->
                _loading.value = false
                _errorMessage.value = exception.message ?: "Error al registrarse"
            }
    }

    fun logout() {
        firebaseAuth.signOut()
        _authState.value = AuthState.LoggedOut
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun getCurrentUserEmail(): String? {
        return firebaseAuth.currentUser?.email
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return when {
            email.isBlank() -> {
                _errorMessage.value = "El email no puede estar vacío"
                false
            }
            !isValidEmail(email) -> {
                _errorMessage.value = "Email inválido"
                false
            }
            password.isBlank() -> {
                _errorMessage.value = "La contraseña no puede estar vacía"
                false
            }
            else -> true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"
        return email.matches(emailRegex.toRegex())
    }
}
