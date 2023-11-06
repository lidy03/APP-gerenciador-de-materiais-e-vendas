package com.example.myapplication.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        //configTabLayout()
        initClicks()
    }

    private fun initClicks(){ binding.imgLogout.setOnClickListener(){logoutapp()}
        binding.imgVenda.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_salesFragment)
        }
        binding.imgGerenciamento.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_manageMaterialFragment2)
        }
    }
    private fun logoutapp(){
        auth.signOut()
        findNavController().navigate(R.id.action_homeFragment_to_authentication)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}