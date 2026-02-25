package iesvdc.segdodam.recyclerviewmotos.Dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import iesvdc.segdodam.recyclerviewmotos.databinding.DialogVideoGameBinding
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame

class VideoGameDialogFragment(
    private val videoGame: VideoGame? = null,
    private val onSave: (VideoGame) -> Unit
) : DialogFragment() {

    private var _binding: DialogVideoGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogVideoGameBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Si estamos editando, rellenamos los campos con los datos del videojuego
        videoGame?.let {
            binding.edtNombre.setText(it.nombre)
            binding.edtPrecio.setText(it.precio.toString())
            binding.edtPlataforma.setText(it.plataforma)
            binding.edtCaracteristicas.setText(it.caracteristicas)
            binding.ratingBarDialog.rating = it.puntuacion
        }

        // Listener para el botón de Aceptar
        binding.btnAceptar.setOnClickListener {
            val precio = binding.edtPrecio.text.toString().replace(',', '.').toDoubleOrNull() ?: 0.0
            val puntuacion = binding.ratingBarDialog.rating
            val nuevoVideoGame = VideoGame(
                id = videoGame?.id ?: 0,
                nombre = binding.edtNombre.text.toString(),
                precio = precio,
                plataforma = binding.edtPlataforma.text.toString(),
                caracteristicas = binding.edtCaracteristicas.text.toString(),
                puntuacion = puntuacion,
                visitas = videoGame?.visitas ?: 0L
            )
            onSave(nuevoVideoGame) // Llama a la función lambda que se le pasó al crear el diálogo
            dismiss()
        }

        // Listener para el botón de Cancelar
        binding.btnCancelar.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar fugas de memoria
    }
}
