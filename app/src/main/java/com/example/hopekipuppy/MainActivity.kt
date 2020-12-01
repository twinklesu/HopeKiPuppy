package com.example.hopekipuppy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.hopekipuppy.databinding.ActivityMainBinding
import com.example.hopekipuppy.login.Login
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var login : Login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}