package com.example.myapplication.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRegisterBinding
import com.example.myapplication.main.helper.FirebaseHelper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {

    //DEPOIS DE CONECTAR O FIREBASE, ATT BINDING
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        initClicks()
    }

    private fun initClicks(){
        binding.buttonRegister.setOnClickListener{validateData()}
    }
    //VALIDAR EMAIL E SENHA
    private fun validateData(){
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        if(email.isNotEmpty() && password.isNotEmpty()) {
            registerUser(email, password)
        }else if(email.isEmpty()){
                Toast.makeText(requireContext(), "Informe seu email.", Toast.LENGTH_SHORT).show()
        }else if(password.isEmpty()){
            Toast.makeText(requireContext(), "Informe sua senha.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_global_homeFragment2)
                } else {
                    //erro
                    Toast.makeText(requireContext(), FirebaseHelper.validError(task.exception?.message?:""), Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible= false
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}