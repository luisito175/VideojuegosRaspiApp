package iesvdc.segdodam.recyclerviewmotos.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iesvdc.segdodam.recyclerviewmotos.data.auth.RegisterRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: RegisterRepository
) : ViewModel() {

    private val _registerState = MutableLiveData<Result<Unit>>()
    val registerState: LiveData<Result<Unit>> = _registerState

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            runCatching { repository.register(username, email, password) }
                .onSuccess { _registerState.value = Result.success(Unit) }
                .onFailure { _registerState.value = Result.failure(it) }
        }
    }
}