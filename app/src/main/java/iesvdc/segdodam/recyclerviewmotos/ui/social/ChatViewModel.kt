package iesvdc.segdodam.recyclerviewmotos.ui.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iesvdc.segdodam.recyclerviewmotos.data.auth.SessionManager
import iesvdc.segdodam.recyclerviewmotos.data.repositories.SocialRepository
import iesvdc.segdodam.recyclerviewmotos.domain.models.ChatMessageEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val socialRepository: SocialRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    val myUserId: Int = sessionManager.getUserId() ?: -1

    private val _messages = MutableLiveData<List<ChatMessageEntity>>(emptyList())
    val messages: LiveData<List<ChatMessageEntity>> = _messages

    private val _statusMessage = MutableLiveData<String?>()
    val statusMessage: LiveData<String?> = _statusMessage

    private var pollingJob: Job? = null

    fun loadMessagesOnce(friendId: Int) {
        viewModelScope.launch {
            fetchMessages(friendId)
        }
    }

    fun startPolling(friendId: Int) {
        if (pollingJob?.isActive == true) return
        pollingJob = viewModelScope.launch {
            while (isActive) {
                fetchMessages(friendId)
                delay(3000)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun sendMessage(friendId: Int, content: String) {
        val trimmed = content.trim()
        if (trimmed.isBlank()) return

        viewModelScope.launch {
            runCatching { socialRepository.sendMessage(friendId, trimmed) }
                .onSuccess {
                    fetchMessages(friendId)
                }
                .onFailure {
                    _statusMessage.value = it.message ?: "No se pudo enviar el mensaje"
                }
        }
    }

    private suspend fun fetchMessages(friendId: Int) {
        runCatching { socialRepository.getMessages(friendId) }
            .onSuccess { _messages.value = it }
            .onFailure {
                _statusMessage.value = it.message ?: "No se pudieron cargar los mensajes"
            }
    }
}

