package iesvdc.segdodam.recyclerviewmotos.ui.motos.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.databinding.FragmentMotoDetailBinding
import iesvdc.segdodam.recyclerviewmotos.models.Moto
import iesvdc.segdodam.recyclerviewmotos.ui.motos.MotosViewModel

@AndroidEntryPoint
class MotoDetailFragment : Fragment() {

    private var _binding: FragmentMotoDetailBinding? = null
    private val binding get() = _binding!!

    // Obtiene la MISMA instancia del ViewModel que el fragmento de la lista
    private val viewModel: MotosViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMotoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recoge el argumento (la posición de la moto)
        val motoPosition = arguments?.getInt("moto_position", -1) ?: -1

        if (motoPosition != -1) {
            // Pide la moto al ViewModel en lugar de tener su propia lista
            val moto = viewModel.getMotoAt(motoPosition)
            if (moto != null) {
                bind(moto)
            }
        }
    }

    private fun bind(moto: Moto) {
        binding.tvDetailMarca.text = moto.marca
        binding.tvDetailModelo.text = moto.modelo
        binding.tvDetailCaracteristicas.text = moto.caracteristicas

        Glide.with(this)
            .load(moto.imagen)
            .centerCrop()
            .into(binding.ivDetailImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
