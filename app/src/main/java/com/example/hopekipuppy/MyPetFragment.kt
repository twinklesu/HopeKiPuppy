package com.example.hopekipuppy

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hopekipuppy.databinding.FragmentMyPetBinding
import com.example.hopekipuppy.databinding.FragmentWriteFoundBinding
import timber.log.Timber

class MyPetFragment : Fragment() {

    private lateinit var binding : FragmentMyPetBinding
    private lateinit var petName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        petName = requireArguments().getString("pet_name").toString()
        Timber.d(petName)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_pet, container, false)


        return binding.root
    }

    private fun getMyPet() {

    }
}