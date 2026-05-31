package iesvdc.segdodam.recyclerviewmotos.ui.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iesvdc.segdodam.recyclerviewmotos.data.repositories.SocialRepository
import iesvdc.segdodam.recyclerviewmotos.domain.models.FriendEntity
import iesvdc.segdodam.recyclerviewmotos.domain.models.UserSearchResultEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val socialRepository: SocialRepository
) : ViewModel() {

    private var lastSearchQuery: String = ""

    private val _acceptedFriends = MutableLiveData<List<FriendEntity>>(emptyList())
    val acceptedFriends: LiveData<List<FriendEntity>> = _acceptedFriends

    private val _searchResults = MutableLiveData<List<UserSearchResultEntity>>(emptyList())
    val searchResults: LiveData<List<UserSearchResultEntity>> = _searchResults

    private val _incomingRequests = MutableLiveData<List<FriendEntity>>(emptyList())
    val incomingRequests: LiveData<List<FriendEntity>> = _incomingRequests

    private val _statusMessage = MutableLiveData<String?>()
    val statusMessage: LiveData<String?> = _statusMessage

    fun searchUsers(query: String) {
        lastSearchQuery = query
        if (query.length < 2) {
            _searchResults.value = emptyList()
            _statusMessage.value = "Escribe al menos 2 caracteres"
            return
        }

        viewModelScope.launch {
            runCatching { socialRepository.searchUsers(query) }
                .onSuccess {
                    _searchResults.value = it
                    _statusMessage.value = if (it.isEmpty()) "No hay resultados" else null
                }
                .onFailure {
                    _statusMessage.value = it.message ?: "Error al buscar usuarios"
                }
        }
    }

    fun loadIncomingRequests() {
        viewModelScope.launch {
            runCatching { socialRepository.getIncomingRequests() }
                .onSuccess { _incomingRequests.value = it }
                .onFailure {
                    _statusMessage.value = it.message ?: "Error al cargar solicitudes"
                }
        }
    }

    fun loadAcceptedFriends() {
        viewModelScope.launch {
            runCatching { socialRepository.getAcceptedFriends() }
                .onSuccess { _acceptedFriends.value = it }
                .onFailure {
                    _statusMessage.value = it.message ?: "Error al cargar la lista de amigos"
                }
        }
    }

    fun sendFriendRequest(addresseeId: Int) {
        viewModelScope.launch {
            runCatching { socialRepository.sendFriendRequest(addresseeId) }
                .onSuccess {
                    _statusMessage.value = it
                    loadIncomingRequests()
                    loadAcceptedFriends()
                    if (lastSearchQuery.length >= 2) {
                        searchUsers(lastSearchQuery)
                    }
                }
                .onFailure {
                    _statusMessage.value = it.message ?: "No se pudo enviar la solicitud"
                }
        }
    }

    fun acceptRequest(friendshipId: Int) {
        viewModelScope.launch {
            runCatching { socialRepository.acceptFriendRequest(friendshipId) }
                .onSuccess {
                    _statusMessage.value = it
                    loadIncomingRequests()
                    loadAcceptedFriends()
                    if (lastSearchQuery.length >= 2) {
                        searchUsers(lastSearchQuery)
                    }
                }
                .onFailure {
                    _statusMessage.value = it.message ?: "No se pudo aceptar la solicitud"
                }
        }
    }

    fun rejectRequest(friendshipId: Int) {
        viewModelScope.launch {
            runCatching { socialRepository.removeFriendship(friendshipId) }
                .onSuccess {
                    _statusMessage.value = it
                    loadIncomingRequests()
                    loadAcceptedFriends()
                    if (lastSearchQuery.length >= 2) {
                        searchUsers(lastSearchQuery)
                    }
                }
                .onFailure {
                    _statusMessage.value = it.message ?: "No se pudo rechazar la solicitud"
                }
        }
    }
}



