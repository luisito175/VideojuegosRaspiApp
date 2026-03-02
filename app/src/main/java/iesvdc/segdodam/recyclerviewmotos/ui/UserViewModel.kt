package iesvdc.segdodam.recyclerviewmotos.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iesvdc.segdodam.recyclerviewmotos.data.api.UpdateUserRequest
import iesvdc.segdodam.recyclerviewmotos.data.api.UserApiService
import iesvdc.segdodam.recyclerviewmotos.data.auth.AuthRepository
import iesvdc.segdodam.recyclerviewmotos.data.auth.SessionManager
import iesvdc.segdodam.recyclerviewmotos.data.local.dao.UserProfileDao
import iesvdc.segdodam.recyclerviewmotos.data.local.entities.UserProfile
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val userApiService: UserApiService,
    private val sessionManager: SessionManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    val userProfile = userProfileDao.getUserProfile().asLiveData()

    private val _updateState = MutableLiveData<UpdateState>()
    val updateState: LiveData<UpdateState> = _updateState

    private val _logoutState = MutableLiveData<LogoutState>()
    val logoutState: LiveData<LogoutState> = _logoutState

    init {
        loadInitialProfile()
    }

    private fun loadInitialProfile() {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            if (userId != null) {
                try {
                    // Cargar perfil desde servidor después de login
                    Log.d("UserViewModel", "Cargando perfil del usuario $userId desde servidor...")
                    val response = userApiService.getUser(userId)
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            // Guardar en BD local (sin imagen, ya que el servidor no la tiene)
                            userProfileDao.insertOrUpdateProfile(
                                UserProfile(
                                    id = 1,
                                    name = body.username,
                                    email = body.email,
                                    photoUri = null  // Servidor no tiene imagen
                                )
                            )
                            Log.d("UserViewModel", "✓ Perfil cargado: ${body.username}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("UserViewModel", "Error al cargar perfil: ${e.message}")
                }
            }
        }
    }

    fun updateProfile(name: String, email: String, photoBase64: String?) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            if (userId == null) {
                _updateState.value = UpdateState.Error("No se encontró el usuario autenticado")
                return@launch
            }

            try {
                // Validar que nombre y email no estén vacíos
                if (name.isBlank() || email.isBlank()) {
                    _updateState.value = UpdateState.Error("Nombre y email son requeridos")
                    return@launch
                }

                // PASO 1: Guardar en BD local PRIMERO (con imagen)
                Log.d("UpdateProfile", "1️⃣ Guardando en BD local")
                Log.d("UpdateProfile", "   - name=$name")
                Log.d("UpdateProfile", "   - email=$email")
                Log.d("UpdateProfile", "   - photoBase64 present: ${photoBase64 != null}")
                if (photoBase64 != null) {
                    Log.d("UpdateProfile", "   - photoBase64 length: ${photoBase64.length} chars")
                }
                
                val profile = UserProfile(
                    id = 1,
                    name = name,
                    email = email,
                    photoUri = photoBase64
                )
                
                userProfileDao.insertOrUpdateProfile(profile)
                Log.d("UpdateProfile", "✓ Guardado en BD local exitosamente")
                
                // Verificar que se guardó correctamente
                val savedProfile = userProfileDao.getUserProfile()
                Log.d("UpdateProfile", "Verificando guardado...")
                
                // PASO 2: Enviar nombre y email al servidor
                val request = UpdateUserRequest(
                    username = name,  // Siempre enviar, no null
                    email = email      // Siempre enviar, no null
                )
                
                Log.d("UpdateProfile", "2️⃣ Enviando PATCH al servidor: userId=$userId, name=$name, email=$email")
                val response = userApiService.updateUser(userId, request)

                if (response.isSuccessful) {
                    _updateState.value = UpdateState.Success
                    Log.d("UpdateProfile", "✓✓ Actualización exitosa: BD local + Servidor")
                } else {
                    val errorBody = try {
                        response.errorBody()?.string() ?: "Sin detalles"
                    } catch (e: Exception) {
                        e.message ?: "Error al leer respuesta"
                    }
                    
                    Log.e("UpdateProfile", "✗ Error en servidor ${response.code()}: $errorBody")
                    
                    val errorMessage = when (response.code()) {
                        400 -> "Datos inválidos en servidor: $errorBody"
                        401 -> "Sesión expirada: $errorBody"
                        403 -> "No tienes permiso: $errorBody"
                        409 -> "Email ya en uso: $errorBody"
                        500 -> "Error del servidor (500): $errorBody"
                        else -> "Error HTTP ${response.code()}: $errorBody"
                    }
                    _updateState.value = UpdateState.Error(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("UpdateProfile", "Excepción al actualizar: ${e.message}", e)
                e.printStackTrace()
                _updateState.value = UpdateState.Error("Error de conexión: ${e.message ?: "desconocido"}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            runCatching {
                authRepository.logout()
                userProfileDao.clearProfile()
            }.onSuccess {
                _logoutState.value = LogoutState.Success
            }.onFailure { error ->
                _logoutState.value = LogoutState.Error(error.localizedMessage ?: "Error al cerrar sesión")
            }
        }
    }

    sealed class UpdateState {
        object Success : UpdateState()
        data class Error(val message: String) : UpdateState()
    }

    sealed class LogoutState {
        object Success : LogoutState()
        data class Error(val message: String) : LogoutState()
    }
}
