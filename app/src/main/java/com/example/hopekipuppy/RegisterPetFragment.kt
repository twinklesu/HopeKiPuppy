package com.example.hopekipuppy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hopekipuppy.databinding.FragmentRegisterPetBinding
import com.example.hopekipuppy.databinding.FragmentWriteFoundBinding

class RegisterPetFragment : Fragment() {

    private lateinit var binding : FragmentRegisterPetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_pet, container, false)
        return binding.root
    }

}