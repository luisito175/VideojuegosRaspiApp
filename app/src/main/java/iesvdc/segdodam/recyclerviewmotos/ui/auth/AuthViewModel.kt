package iesvdc.segdodam.recyclerviewmotos.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iesvdc.segdodam.recyclerviewmotos.data.auth.AuthRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<Result<Unit>>()
    val loginState: LiveData<Result<Unit>> = _loginState

    private val _refreshState = MutableLiveData<Result<Boolean>>()
    val refreshState: LiveData<Result<Boolean>> = _refreshState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            runCatching { authRepository.login(email, password) }
                .onSuccess { _loginState.value = Result.success(Unit) }
                .onFailure { _loginState.value = Result.failure(it) }
        }
    }

    fun tryRefresh() {
        viewModelScope.launch {
            runCatching { authRepository.refreshAccessToken() }
                .onSuccess { _refreshState.value = Result.success(it) }
                .onFailure { _refreshState.value = Result.failure(it) }
        }
    }
}
