package com.example.myapplication.main.model

import android.os.Parcelable
import android.widget.Spinner
import com.example.myapplication.main.helper.FirebaseHelper
import com.google.firebase.ktx.Firebase
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sales(
    var id:String = "",
    var description:String ="",
    var status:Int=0
):Parcelable{
    init{
        this.id = FirebaseHelper.getDatabase().push().key?:""
    }
}
