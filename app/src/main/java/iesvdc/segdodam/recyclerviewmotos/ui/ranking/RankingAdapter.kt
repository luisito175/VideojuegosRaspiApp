package iesvdc.segdodam.recyclerviewmotos.ui.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iesvdc.segdodam.recyclerviewmotos.databinding.ItemRankingEntryBinding
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame

class RankingAdapter(
    private var items: List<VideoGame>,
    private var metricLabel: String
) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    class RankingViewHolder(private val binding: ItemRankingEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(videoGame: VideoGame, position: Int, metricLabel: String) {
            binding.tvRankNumber.text = "#${position + 1}"
            binding.tvRankTitle.text = videoGame.nombre
            binding.tvRankSubtitle.text = "${videoGame.plataforma} · ${String.format("%.2f €", videoGame.precio)}"
            binding.tvRankMetric.text = "$metricLabel: ${metricValue(videoGame, metricLabel)}"
        }

        private fun metricValue(videoGame: VideoGame, metricLabel: String): String {
            return if (metricLabel == "Puntuación") {
                videoGame.puntuacion.toInt().toString()
            } else {
                videoGame.visitas.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val binding = ItemRankingEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(items[position], position, metricLabel)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<VideoGame>, newMetricLabel: String) {
        items = newItems
        metricLabel = newMetricLabel
        notifyDataSetChanged()
    }
}
