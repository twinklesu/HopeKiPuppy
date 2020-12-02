package com.example.hopekipuppy

import android.os.Bundle
import android.telephony.mbms.MbmsErrors
import androidx.appcompat.app.AppCompatActivity
import com.example.hopekipuppy.login.Login
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.json.JSONException


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var login : Login
        lateinit var storage: FirebaseStorage
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storage = Firebase.storage

    }
}