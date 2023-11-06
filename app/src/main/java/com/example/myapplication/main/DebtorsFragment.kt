package com.example.myapplication.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDebtorsBinding
import com.example.myapplication.databinding.FragmentPaidBinding
import com.example.myapplication.main.adapter.SalesAdapter
import com.example.myapplication.main.helper.FirebaseHelper
import com.example.myapplication.main.model.Sales
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DebtorsFragment : Fragment() {
    private var _binding: FragmentDebtorsBinding? = null
    private val binding get() = _binding!!

    private lateinit var salesAdapter: SalesAdapter
    private val saleList = mutableListOf<Sales>()

    private lateinit var sales: Sales

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDebtorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
        getSales()
    }
    private fun getSales() {
        FirebaseHelper
            .getDatabase()
            .child("sale") // Corrigido para "task" em letras minúsculas
            .child(FirebaseHelper.getIdUser() ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        saleList.clear()
                        for (snap in snapshot.children) {
                            val sale = snap.getValue(Sales::class.java) as Sales
                            if (sale.status == 1) saleList.add(sale)
                            binding.progressbar.isVisible = false
                            binding.textInfo.text = ""
                            saleList.reverse()
                            initAdapter()
                        }
                    }
                    else{
                        binding.textInfo.text="Nenhuma venda cadastrada"
                    }
                    binding.progressbar.isVisible = false
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun initClicks(){
        binding.addSale.setOnClickListener {
            val action = SalesFragmentDirections.actionSalesFragmentToFormSalesFragment2(null)
            findNavController().navigate(action)
        }
    }

    private fun initAdapter(){
        binding.rvDebtors.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDebtors.setHasFixedSize(true)
        salesAdapter = SalesAdapter(requireContext(), saleList){sale, select ->
            optionSelect(sale, select)
        }
        binding.rvDebtors.adapter = salesAdapter
    }

    private fun optionSelect(sale: Sales, select: Int){
        when(select){
            salesAdapter.SELECT_REMOVE ->{
                deleteSale(sale)
            }
            salesAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormSalesFragment2(sale)
                findNavController().navigate(action)
            }
            salesAdapter.SELECT_BACK -> {
                sale.status = 0
                updateSale(sale)
            }

        }
    }

    private fun updateSale(sale: Sales){
        FirebaseHelper
            .getDatabase()
            .child("sale")
            .child(FirebaseHelper.getIdUser()?:"")
            .child(sale.id)
            .setValue(sale)
            .addOnCompleteListener {sale ->
                if (sale.isSuccessful) {
                    binding.progressbar.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        "Venda editada com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), "Erro ao salvar", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                binding.progressbar.isVisible =   false
                Toast.makeText(requireContext(), "Erro base", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteSale(sale: Sales){
        FirebaseHelper
            .getDatabase()
            .child("sale")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(sale.id)
            .removeValue()
            .addOnCompleteListener { sale ->
                if (sale.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Venda removida com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), R.string.error_generic, Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener{
                binding.progressbar.isVisible = false
                Toast.makeText(requireContext(), R.string.error_generic, Toast.LENGTH_SHORT).show()
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}