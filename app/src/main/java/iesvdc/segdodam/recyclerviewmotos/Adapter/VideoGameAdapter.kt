package iesvdc.segdodam.recyclerviewmotos.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iesvdc.segdodam.recyclerviewmotos.databinding.ItemVideoGameBinding
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame

class VideoGameAdapter(
    private var videoGames: MutableList<VideoGame>,
    private val detailOnClick: (Int) -> Unit,
    private val favoriteOnClick: (Int) -> Unit
) : RecyclerView.Adapter<VideoGameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoGameViewHolder {
        val binding = ItemVideoGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoGameViewHolder(binding, detailOnClick, favoriteOnClick)
    }

    override fun onBindViewHolder(holder: VideoGameViewHolder, position: Int) {
        holder.renderize(videoGames[position], position)
    }

    override fun getItemCount(): Int = videoGames.size

    /**
     * Actualiza la lista de datos del adaptador y notifica al RecyclerView.
     */
    fun updateData(newVideoGames: List<VideoGame>) {
        videoGames.clear()
        videoGames.addAll(newVideoGames)
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }

    fun getItemAt(position: Int): VideoGame? = videoGames.getOrNull(position)
}
