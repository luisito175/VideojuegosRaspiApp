package iesvdc.segdodam.recyclerviewmotos.ui.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.R

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    private var etSearchUsers: EditText? = null
    private var tvSearchEmpty: TextView? = null
    private var tvIncomingEmpty: TextView? = null
    private var tvAcceptedEmpty: TextView? = null

    private lateinit var searchAdapter: SearchUsersAdapter
    private lateinit var incomingAdapter: IncomingRequestsAdapter
    private lateinit var acceptedAdapter: AcceptedFriendsAdapter

    private val viewModel: FriendsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchUsersAdapter { userId ->
            viewModel.sendFriendRequest(userId)
        }
        incomingAdapter = IncomingRequestsAdapter(
            onAccept = { friendshipId -> viewModel.acceptRequest(friendshipId) },
            onReject = { friendshipId -> viewModel.rejectRequest(friendshipId) }
        )
        acceptedAdapter = AcceptedFriendsAdapter { friend ->
            val args = bundleOf(
                "friend_id" to friend.userId,
                "friend_name" to friend.username
            )
            findNavController().navigate(R.id.action_friendsFragment_to_chatFragment, args)
        }

        etSearchUsers = view.findViewById(R.id.etSearchUsers)
        tvSearchEmpty = view.findViewById(R.id.tvSearchEmpty)
        tvIncomingEmpty = view.findViewById(R.id.tvIncomingEmpty)
        tvAcceptedEmpty = view.findViewById(R.id.tvAcceptedEmpty)

        view.findViewById<RecyclerView>(R.id.rvSearchResults).adapter = searchAdapter
        view.findViewById<RecyclerView>(R.id.rvIncomingRequests).adapter = incomingAdapter
        view.findViewById<RecyclerView>(R.id.rvAcceptedFriends).adapter = acceptedAdapter

        setupListeners()
        observeViewModel()
        viewModel.loadIncomingRequests()
        viewModel.loadAcceptedFriends()
    }

    private fun setupListeners() {
        view?.findViewById<Button>(R.id.btnSearchUsers)?.setOnClickListener {
            val query = etSearchUsers?.text?.toString().orEmpty().trim()
            viewModel.searchUsers(query)
        }
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { users ->
            searchAdapter.updateData(users)
            tvSearchEmpty?.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.incomingRequests.observe(viewLifecycleOwner) { requests ->
            incomingAdapter.updateData(requests)
            tvIncomingEmpty?.visibility = if (requests.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.acceptedFriends.observe(viewLifecycleOwner) { friends ->
            acceptedAdapter.updateData(friends)
            tvAcceptedEmpty?.visibility = if (friends.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.statusMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        etSearchUsers = null
        tvSearchEmpty = null
        tvIncomingEmpty = null
        tvAcceptedEmpty = null
    }
}





