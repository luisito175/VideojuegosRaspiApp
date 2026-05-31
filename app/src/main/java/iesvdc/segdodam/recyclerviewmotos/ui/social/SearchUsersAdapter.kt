package iesvdc.segdodam.recyclerviewmotos.ui.social

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iesvdc.segdodam.recyclerviewmotos.R
import iesvdc.segdodam.recyclerviewmotos.domain.models.UserSearchResultEntity

class SearchUsersAdapter(
    private val onSendRequest: (Int) -> Unit
) : RecyclerView.Adapter<SearchUsersAdapter.SearchUserViewHolder>() {

    private val items = mutableListOf<UserSearchResultEntity>()

    fun updateData(newItems: List<UserSearchResultEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_search_result, parent, false)
        return SearchUserViewHolder(view, onSendRequest)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class SearchUserViewHolder(
        itemView: View,
        private val onSendRequest: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvUsername: TextView = itemView.findViewById(R.id.tvUserRowUsername)
        private val tvRelation: TextView = itemView.findViewById(R.id.tvUserRowRelation)
        private val btnSend: Button = itemView.findViewById(R.id.btnUserRowSend)

        fun bind(item: UserSearchResultEntity) {
            tvUsername.text = item.username

            when (item.relation) {
                "none" -> {
                    tvRelation.setText(R.string.relacion_sin_relacion)
                    btnSend.visibility = View.VISIBLE
                    btnSend.isEnabled = true
                    btnSend.setText(R.string.enviar_solicitud)
                }
                "pending" -> {
                    tvRelation.setText(R.string.relacion_pendiente)
                    btnSend.visibility = View.VISIBLE
                    btnSend.isEnabled = false
                    btnSend.setText(R.string.relacion_pendiente)
                }
                "incoming" -> {
                    tvRelation.setText(R.string.relacion_entrante)
                    btnSend.visibility = View.VISIBLE
                    btnSend.isEnabled = false
                    btnSend.setText(R.string.relacion_entrante)
                }
                "accepted" -> {
                    tvRelation.setText(R.string.relacion_aceptada)
                    btnSend.visibility = View.VISIBLE
                    btnSend.isEnabled = false
                    btnSend.setText(R.string.relacion_aceptada)
                }
                else -> {
                    tvRelation.setText(R.string.relacion_tu_mismo)
                    btnSend.visibility = View.GONE
                }
            }

            btnSend.setOnClickListener {
                if (item.relation == "none") {
                    onSendRequest(item.userId)
                }
            }
        }
    }
}

