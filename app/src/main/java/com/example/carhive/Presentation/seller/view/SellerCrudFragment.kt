package com.example.carhive.Presentation.seller.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carhive.Presentation.seller.viewModel.CarAdapter
import com.example.carhive.Presentation.seller.viewModel.CrudViewModel
import com.example.carhive.databinding.FragmentSellerCrudBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellerCrudFragment : Fragment() {

    private var _binding: FragmentSellerCrudBinding? = null // ViewBinding for the fragment
    private val binding get() = _binding!! // Getter for the binding object

    private val viewModel: CrudViewModel by activityViewModels() // Shared ViewModel for CRUD operations
    private lateinit var carAdapter: CarAdapter // Adapter for the RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerCrudBinding.inflate(inflater, container, false) // Inflate the binding layout
        return binding.root // Return the root view of the binding
    }

    // Function to set up the RecyclerView
    private fun setupRecyclerView() {
        carAdapter = CarAdapter(emptyList(), requireActivity(), viewModel) // Initialize the adapter with an empty list
        binding.recyclerViewCar.apply {
            layoutManager = LinearLayoutManager(context) // Set layout manager
            adapter = carAdapter // Set the adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView with its adapter and ViewModel
        carAdapter = CarAdapter(emptyList(), requireActivity(), viewModel) // Initialize the adapter again
        binding.recyclerViewCar.apply {
            layoutManager = LinearLayoutManager(context) // Set layout manager
            adapter = carAdapter // Set the adapter
        }

        // Observe changes in the car list from the ViewModel
        viewModel.carList.observe(viewLifecycleOwner) { cars ->
            carAdapter.updateCars(cars) // Update the adapter when data changes
        }

        // Observe error messages from the ViewModel
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            // Show a Toast message for any error
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }

        // Fetch cars for the current user from the ViewModel
        viewModel.fetchCarsForUser()

        // Set up the button to show the car options dialog
        binding.btnAddCar.setOnClickListener {
            showCarOptionsDialog() // Call the function to show the dialog
        }
    }

    // Function to display the car options dialog
    private fun showCarOptionsDialog() {
        val dialog = CrudDialogFragment() // Create an instance of the dialog
        dialog.show(parentFragmentManager, "CarOptionsDialog") // Show the dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference to avoid memory leaks
    }
}
