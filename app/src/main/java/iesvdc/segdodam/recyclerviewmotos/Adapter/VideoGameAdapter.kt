package iesvdc.segdodam.recyclerviewmotos.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iesvdc.segdodam.recyclerviewmotos.databinding.ItemVideoGameBinding
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame

class VideoGameAdapter(
    private var videoGames: MutableList<VideoGame>,
    private val deleteOnClick: (Int) -> Unit,
    private val editOnClick: (Int) -> Unit,
    private val detailOnClick: (Int) -> Unit // Nuevo listener para navegar a detalles
) : RecyclerView.Adapter<VideoGameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoGameViewHolder {
        val binding = ItemVideoGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // Pasamos el nuevo listener al ViewHolder
        return VideoGameViewHolder(binding, deleteOnClick, editOnClick, detailOnClick)
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
}
