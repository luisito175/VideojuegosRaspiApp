package iesvdc.segdodam.recyclerviewmotos.Dialogs

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import iesvdc.segdodam.recyclerviewmotos.databinding.DialogMotoBinding
import iesvdc.segdodam.recyclerviewmotos.models.Moto

class MotoDialogFragment(
    private val moto: Moto? = null,
    private val onSave: (Moto) -> Unit
) : DialogFragment() {

    private var _binding: DialogMotoBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    // Lanzador para la selección de imágenes
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.edtImagen.setText(it.toString()) // Actualiza el campo de texto
            Glide.with(this).load(it).into(binding.imgPreview) // Muestra la previsualización
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMotoBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Si estamos editando, rellenamos los campos con los datos de la moto
        moto?.let {
            binding.edtMarca.setText(it.marca)
            binding.edtModelo.setText(it.modelo)
            binding.edtCaracteristicas.setText(it.caracteristicas)
            binding.edtImagen.setText(it.imagen)
            Glide.with(this).load(it.imagen).into(binding.imgPreview)
        }

        // Listener para el botón de seleccionar imagen
        binding.btnSelectImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Listener para el botón de Aceptar
        binding.btnAceptar.setOnClickListener {
            val imagen = selectedImageUri?.toString() ?: binding.edtImagen.text.toString()

            val nuevaMoto = Moto(
                marca = binding.edtMarca.text.toString(),
                modelo = binding.edtModelo.text.toString(),
                caracteristicas = binding.edtCaracteristicas.text.toString(),
                imagen = imagen
            )
            onSave(nuevaMoto) // Llama a la función lambda que se le pasó al crear el diálogo
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
