package com.example.myapplication.main.helper

import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper {
    companion object{
        fun getDatabase()=FirebaseDatabase.getInstance().reference
        private fun getAuth()=FirebaseAuth.getInstance()
        fun getIdUser()= getAuth().uid

        fun isAutheticated() = getAuth().currentUser !=null
        fun validError(error:String):Int{
            return when{
                error.contains( "There is no user record correspinding to this identifier")-> {
                    R.string.account_not_registered_register_fragment
                }
                error.contains("The email address is badly formatted")->{
                    R.string.invalid_email_register_fragment
                }
                error.contains("The password is invalid or the user does not hava as password")->{
                    R.string.invalid_password_register_fragment
                }
                error.contains("The email address is already in use by another account")->{
                    R.string.email_in_user_register_fragment
                }
                error.contains("Password should be at least 6 characteres")->{
                    R.string.strong_password_register_fragment
                }
                else -> {
                    R.string.error_generic
                }

            }
        }
    }
}