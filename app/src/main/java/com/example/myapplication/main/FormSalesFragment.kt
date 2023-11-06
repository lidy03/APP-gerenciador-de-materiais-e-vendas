package com.example.myapplication.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFormSalesBinding
import com.example.myapplication.main.helper.FirebaseHelper
import com.example.myapplication.main.model.Sales

class FormSalesFragment : Fragment() {
    private val args:FormSalesFragmentArgs by navArgs()
    private var _binding: FragmentFormSalesBinding? = null
    private val binding get() = _binding!!

    private lateinit var sales: Sales
    private var newSales: Boolean = true
    private var status: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormSalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        getArgs()
    }

    private fun initListener(){
        binding.bntSave.setOnClickListener { validateSale() }
        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            status = when(id){
                R.id.check_pago -> 0
                else -> 1
            }
        }
    }


    private fun validateSale(){
        val description = binding.editDescription.text.toString().trim()
        if(description.isNotEmpty()){
            binding.progressbar.isVisible = true

            if (newSales)sales = Sales()
            sales.description = description
            sales.status = status
            saveSales()
        }
    }

    private fun saveSales(){
        FirebaseHelper
            .getDatabase()
            .child("sale")
            .child(FirebaseHelper.getIdUser()?:"")
            .child(sales.id)
            .setValue(sales)
            .addOnCompleteListener { sale ->
                if (sale.isSuccessful) {
                    if (newSales) { // NOVA TAREFA
                        findNavController().popBackStack()
                        Toast.makeText(
                            requireContext(),
                            "Venda salva com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else { // EDIÇÃO DE TAREFA
                        binding.progressbar.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Venda atualizada com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Erro ao salvar", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                binding.progressbar.isVisible = false
                Toast.makeText(requireContext(), "Erro ao salvar", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getArgs(){
        args.sale.let {
            if (it != null) {
                sales = it
                configSale()
            }
        }
    }

    private fun configSale(){
        newSales = false
        status = sales.status
        binding.textToobar.text = "Editando venda..."
        binding.editDescription.setText(sales.description)
        setStatus()
    }

    private fun setStatus(){
        binding.radioGroup.check(
            when (sales.status){
                0 ->{
                    R.id.check_pago
                }
                else ->{
                    R.id.check_pendente
                }
            }
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}