package iesvdc.segdodam.recyclerviewmotos.ui.social

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iesvdc.segdodam.recyclerviewmotos.R
import iesvdc.segdodam.recyclerviewmotos.domain.models.FriendEntity

class AcceptedFriendsAdapter(
    private val onFriendClick: (FriendEntity) -> Unit
) : RecyclerView.Adapter<AcceptedFriendsAdapter.AcceptedFriendViewHolder>() {

    private val items = mutableListOf<FriendEntity>()

    fun updateData(newItems: List<FriendEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptedFriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_accepted_friend, parent, false)
        return AcceptedFriendViewHolder(view, onFriendClick)
    }

    override fun onBindViewHolder(holder: AcceptedFriendViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class AcceptedFriendViewHolder(
        itemView: View,
        private val onFriendClick: (FriendEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvUsername: TextView = itemView.findViewById(R.id.tvAcceptedRowUsername)
        private val tvInfo: TextView = itemView.findViewById(R.id.tvAcceptedRowInfo)

        fun bind(item: FriendEntity) {
            tvUsername.text = item.username
            tvInfo.text = itemView.context.getString(R.string.amigo_info, item.userId)
            itemView.setOnClickListener { onFriendClick(item) }
        }
    }
}


