package iesvdc.segdodam.recyclerviewmotos.ui.settings

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.R
import iesvdc.segdodam.recyclerviewmotos.databinding.FragmentSettingsBinding
import iesvdc.segdodam.recyclerviewmotos.ui.UserViewModel
import java.io.ByteArrayOutputStream
import java.util.Base64

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private var selectedImageUri: Uri? = null
    private var selectedImageBase64: String? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PickMedia", "✓ URI seleccionado: $uri")
            selectedImageUri = uri
            binding.ivProfilePhoto.setImageURI(uri)
            
            // Convertir imagen a base64
            Log.d("PickMedia", "Iniciando conversión a base64...")
            selectedImageBase64 = uriToBase64(uri)
            Log.d("PickMedia", "Conversión completada. Base64 length: ${selectedImageBase64?.length ?: "null"}")
            
            // Otorgar permisos persistentes de lectura para el URI de la imagen
            try {
                requireContext().contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                Log.d("PickMedia", "✓ Permisos persistentes otorgados")
            } catch (e: Exception) {
                Log.e("PickMedia", "Error al otorgar permisos: ${e.message}")
            }
        } else {
            Log.d("PickMedia", "✗ No se seleccionó ninguna imagen")
        }
    }

    private fun uriToBase64(uri: Uri): String {
        return try {
            Log.d("uriToBase64", "1️⃣ Abriendo InputStream para URI: $uri")
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            
            if (inputStream == null) {
                Log.e("uriToBase64", "✗ InputStream es null")
                return ""
            }
            
            Log.d("uriToBase64", "2️⃣ Decodificando Bitmap...")
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            
            if (bitmap == null) {
                Log.e("uriToBase64", "✗ Bitmap es null después de decodificar")
                Toast.makeText(requireContext(), "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show()
                return ""
            }
            
            Log.d("uriToBase64", "✓ Bitmap decodificado: ${bitmap.width}x${bitmap.height}")
            
            // Comprimir la imagen manteniendo buena calidad
            // Primero: redimensionar a máximo 1200x1200
            Log.d("uriToBase64", "3️⃣ Escalando Bitmap...")
            var compressedBitmap = scaleBitmap(bitmap, 1200, 1200)
            Log.d("uriToBase64", "✓ Bitmap escalado: ${compressedBitmap.width}x${compressedBitmap.height}")
            
            // Segundo: comprimir a JPEG calidad 80%
            Log.d("uriToBase64", "4️⃣ Comprimiendo a JPEG (80%)...")
            var outputStream = ByteArrayOutputStream()
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            var compressedBytes = outputStream.toByteArray()
            outputStream.close()
            
            Log.d("uriToBase64", "✓ Tamaño después de comprimir 80%: ${compressedBytes.size / 1024}KB")
            
            // Si sigue siendo muy grande, comprimir más
            if (compressedBytes.size > 1_000_000) { // >1MB
                Log.d("uriToBase64", "5️⃣ Imagen demasiado grande, recomprimiendo a 65%...")
                outputStream = ByteArrayOutputStream()
                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 65, outputStream)
                compressedBytes = outputStream.toByteArray()
                outputStream.close()
                Log.d("uriToBase64", "✓ Tamaño después de comprimir 65%: ${compressedBytes.size / 1024}KB")
            }
            
            // Si SIGUE siendo muy grande, redimensionar más
            if (compressedBytes.size > 1_500_000) {
                Log.d("uriToBase64", "6️⃣ Sigue siendo muy grande, redimensionando a 900x900...")
                compressedBitmap = scaleBitmap(bitmap, 900, 900)
                outputStream = ByteArrayOutputStream()
                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                compressedBytes = outputStream.toByteArray()
                outputStream.close()
                Log.d("uriToBase64", "✓ Tamaño final: ${compressedBytes.size / 1024}KB")
            }
            
            // Convertir a base64
            Log.d("uriToBase64", "7️⃣ Codificando a Base64...")
            val base64String = Base64.getEncoder().encodeToString(compressedBytes)
            Log.d("uriToBase64", "✓✓ Conversión completada. Tamaño base64: ${base64String.length} chars (${base64String.length / 1024}KB)")
            
            base64String
        } catch (e: Exception) {
            Log.e("uriToBase64", "✗ Excepción: ${e.message}", e)
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al procesar imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            ""
        }
    }
    
    private fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        if (width <= maxWidth && height <= maxHeight) {
            return bitmap
        }
        
        val scale = Math.min(maxWidth.toFloat() / width, maxHeight.toFloat() / height)
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()
        
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupListeners()
    }

    private fun observeViewModel() {
        userViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                binding.etName.setText(profile.name)
                binding.etEmail.setText(profile.email)
                if (profile.photoUri != null) {
                    try {
                        // Si es base64, decodificar y mostrar
                        if (profile.photoUri.startsWith("data:") || profile.photoUri.startsWith("/") || !profile.photoUri.startsWith("http")) {
                            // Verificar si es base64
                            if (profile.photoUri.length > 100 && !profile.photoUri.contains("/")) {
                                // Es base64
                                val imageBytes = android.util.Base64.decode(profile.photoUri, android.util.Base64.DEFAULT)
                                val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                binding.ivProfilePhoto.setImageBitmap(bitmap)
                                selectedImageBase64 = profile.photoUri
                            } else {
                                // Es URI
                                binding.ivProfilePhoto.setImageURI(Uri.parse(profile.photoUri))
                                selectedImageUri = Uri.parse(profile.photoUri)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("SettingsFragment", "Error al cargar imagen: ${e.message}")
                        binding.ivProfilePhoto.setImageResource(R.drawable.ic_launcher_foreground)
                    }
                }
            }
        }

        userViewModel.updateState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UserViewModel.UpdateState.Success -> {
                    Toast.makeText(requireContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    binding.btnSave.isEnabled = true
                }
                is UserViewModel.UpdateState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    binding.btnSave.isEnabled = true
                }
            }
        }

    }

    private fun setupListeners() {
        binding.btnChangePhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            
            if (name.isBlank() || email.isBlank()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("SettingsFragment", "Guardando cambios:")
            Log.d("SettingsFragment", "  - name=$name")
            Log.d("SettingsFragment", "  - email=$email")
            Log.d("SettingsFragment", "  - selectedImageBase64=${selectedImageBase64?.substring(0, Math.min(50, selectedImageBase64?.length ?: 0)) ?: "null"}")
            
            // Desactivar el botón mientras se actualiza
            binding.btnSave.isEnabled = false
            
            // Enviar base64 si hay imagen, si no enviar null
            userViewModel.updateProfile(name, email, selectedImageBase64)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
