package iesvdc.segdodam.recyclerviewmotos.ui.motos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.Adapter.AdapterMoto
import iesvdc.segdodam.recyclerviewmotos.Dialogs.MotoDialogFragment
import iesvdc.segdodam.recyclerviewmotos.R
import iesvdc.segdodam.recyclerviewmotos.databinding.FragmentMotosListBinding

@AndroidEntryPoint
class MotosListFragment : Fragment() {

    private var _binding: FragmentMotosListBinding? = null
    private val binding get() = _binding!!

    // Obtiene una instancia compartida del ViewModel.
    private val viewModel: MotosViewModel by activityViewModels()
    private lateinit var adapter: AdapterMoto

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMotosListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFab()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        // El adaptador ahora recibe una función para el clic en el item
        adapter = AdapterMoto(mutableListOf(), ::deleteMoto, ::showEditMotoDialog, ::navigateToDetail)
        binding.myRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.myRecyclerView.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAddMoto.setOnClickListener {
            showAddMotoDialog()
        }
    }

    private fun observeViewModel() {
        // Observa el LiveData del ViewModel. Cada vez que la lista de motos cambie,
        // este bloque se ejecutará y actualizará la interfaz.
        viewModel.motos.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }
    }

    private fun showAddMotoDialog() {
        val dialog = MotoDialogFragment(null) { nuevaMoto ->
            viewModel.addMoto(nuevaMoto)
            Toast.makeText(context, "Videojuego añadido correctamente", Toast.LENGTH_SHORT).show()
        }
        dialog.show(parentFragmentManager, "ADD_MOTO")
    }

    private fun showEditMotoDialog(pos: Int) {
        val moto = viewModel.getMotoAt(pos)
        if (moto != null) {
            val dialog = MotoDialogFragment(moto) { motoActualizada ->
                viewModel.updateMoto(pos, motoActualizada)
                Toast.makeText(context, "Videojuego actualizado", Toast.LENGTH_SHORT).show()
            }
            dialog.show(parentFragmentManager, "EDIT_MOTO")
        }
    }

    private fun deleteMoto(pos: Int) {
        val moto = viewModel.getMotoAt(pos)
        if (moto != null) {
            viewModel.deleteMoto(pos)
            Toast.makeText(context, "Videojuego '${moto.nombre}' eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDetail(pos: Int) {
        // Crea un Bundle para pasar la posición de la moto
        val bundle = Bundle().apply {
            putInt("moto_position", pos)
        }
        // Navega al fragmento de detalles usando la acción definida en nav_graph.xml
        findNavController().navigate(R.id.action_motosListFragment_to_motoDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
