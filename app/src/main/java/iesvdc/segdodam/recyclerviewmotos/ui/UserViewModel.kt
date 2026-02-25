package iesvdc.segdodam.recyclerviewmotos.ui

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

    fun updateProfile(name: String, email: String, photoUri: String?) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            if (userId == null) {
                _updateState.value = UpdateState.Error("No se encontró el usuario autenticado")
                return@launch
            }

            val response = userApiService.updateUser(
                userId,
                UpdateUserRequest(
                    username = name,
                    email = email,
                    avatarUrl = photoUri
                )
            )

            if (response.isSuccessful) {
                refreshFromApi(userId)
                _updateState.value = UpdateState.Success
            } else {
                _updateState.value = UpdateState.Error("Error al actualizar perfil (${response.code()})")
            }
        }
    }

    private suspend fun refreshFromApi(userId: Int) {
        val response = userApiService.getUser(userId)
        if (response.isSuccessful) {
            val body = response.body() ?: return
            userProfileDao.insertOrUpdateProfile(
                UserProfile(name = body.username, email = body.email, photoUri = body.avatarUrl)
            )
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
