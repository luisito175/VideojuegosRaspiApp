package iesvdc.segdodam.recyclerviewmotos.ui.social

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iesvdc.segdodam.recyclerviewmotos.R
import iesvdc.segdodam.recyclerviewmotos.domain.models.ChatMessageEntity

class ChatMessagesAdapter(
    private val myUserId: Int
) : RecyclerView.Adapter<ChatMessagesAdapter.ChatMessageViewHolder>() {

    private val items = mutableListOf<ChatMessageEntity>()

    fun updateData(newItems: List<ChatMessageEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatMessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        holder.bind(items[position], myUserId)
    }

    override fun getItemCount(): Int = items.size

    class ChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val root: LinearLayout = itemView.findViewById(R.id.chatMessageRoot)
        private val tvContent: TextView = itemView.findViewById(R.id.tvChatMessageContent)
        private val tvTime: TextView = itemView.findViewById(R.id.tvChatMessageTime)

        fun bind(item: ChatMessageEntity, myUserId: Int) {
            val isMine = item.senderId == myUserId
            root.gravity = if (isMine) Gravity.END else Gravity.START

            tvContent.text = item.content
            tvTime.text = item.createdAt

            val bubbleBackground = if (isMine) {
                R.drawable.bg_surface_card
            } else {
                R.drawable.bg_chip
            }
            tvContent.setBackgroundResource(bubbleBackground)
        }
    }
}

