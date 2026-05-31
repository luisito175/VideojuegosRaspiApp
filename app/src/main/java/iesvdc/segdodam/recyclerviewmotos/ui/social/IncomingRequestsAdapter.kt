package iesvdc.segdodam.recyclerviewmotos.ui.social

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iesvdc.segdodam.recyclerviewmotos.R
import iesvdc.segdodam.recyclerviewmotos.domain.models.FriendEntity

class IncomingRequestsAdapter(
    private val onAccept: (Int) -> Unit,
    private val onReject: (Int) -> Unit
) : RecyclerView.Adapter<IncomingRequestsAdapter.IncomingRequestViewHolder>() {

    private val items = mutableListOf<FriendEntity>()

    fun updateData(newItems: List<FriendEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomingRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_incoming_request, parent, false)
        return IncomingRequestViewHolder(view, onAccept, onReject)
    }

    override fun onBindViewHolder(holder: IncomingRequestViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class IncomingRequestViewHolder(
        itemView: View,
        private val onAccept: (Int) -> Unit,
        private val onReject: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvUsername: TextView = itemView.findViewById(R.id.tvRequestRowUsername)
        private val tvInfo: TextView = itemView.findViewById(R.id.tvRequestRowInfo)
        private val btnAccept: Button = itemView.findViewById(R.id.btnRequestRowAccept)
        private val btnReject: Button = itemView.findViewById(R.id.btnRequestRowReject)

        fun bind(item: FriendEntity) {
            tvUsername.text = item.username
            tvInfo.text = itemView.context.getString(
                R.string.solicitud_info,
                item.friendshipId,
                item.unreadCount
            )

            btnAccept.setOnClickListener { onAccept(item.friendshipId) }
            btnReject.setOnClickListener { onReject(item.friendshipId) }
        }
    }
}

