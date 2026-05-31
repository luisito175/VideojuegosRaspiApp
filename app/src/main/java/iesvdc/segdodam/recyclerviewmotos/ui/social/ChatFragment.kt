package iesvdc.segdodam.recyclerviewmotos.ui.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.R

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels()

    private var friendId: Int = -1
    private var friendName: String = ""

    private var tvChatTitle: TextView? = null
    private var rvChatMessages: RecyclerView? = null
    private var etMessageInput: EditText? = null
    private var btnSendMessage: Button? = null
    private var tvChatEmpty: TextView? = null

    private lateinit var adapter: ChatMessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendId = arguments?.getInt("friend_id", -1) ?: -1
        friendName = arguments?.getString("friend_name").orEmpty()

        if (friendId == -1) {
            Toast.makeText(requireContext(), getString(R.string.chat_error_friend), Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        adapter = ChatMessagesAdapter(viewModel.myUserId)

        tvChatTitle = view.findViewById(R.id.tvChatTitle)
        rvChatMessages = view.findViewById(R.id.rvChatMessages)
        etMessageInput = view.findViewById(R.id.etMessageInput)
        btnSendMessage = view.findViewById(R.id.btnSendMessage)
        tvChatEmpty = view.findViewById(R.id.tvChatEmpty)

        tvChatTitle?.text = getString(
            R.string.chat_con,
            friendName.ifBlank { getString(R.string.chat_usuario_generico) }
        )
        rvChatMessages?.adapter = adapter

        btnSendMessage?.setOnClickListener {
            val content = etMessageInput?.text?.toString().orEmpty()
            viewModel.sendMessage(friendId, content)
            etMessageInput?.text?.clear()
        }

        observeViewModel()
        viewModel.loadMessagesOnce(friendId)
    }

    override fun onStart() {
        super.onStart()
        if (friendId != -1) {
            viewModel.startPolling(friendId)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopPolling()
    }

    private fun observeViewModel() {
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            adapter.updateData(messages)
            tvChatEmpty?.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE
            rvChatMessages?.post {
                if (messages.isNotEmpty()) {
                    rvChatMessages?.scrollToPosition(messages.lastIndex)
                }
            }
        }

        viewModel.statusMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tvChatTitle = null
        rvChatMessages = null
        etMessageInput = null
        btnSendMessage = null
        tvChatEmpty = null
    }
}


