package com.example.hopekipuppy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentTitleBinding
import com.example.hopekipuppy.title.MainLostFragmentDirections

class TitleFragment : Fragment() {


    private lateinit var binding : FragmentTitleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_title, container, false
        )

        binding.golostButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToMainLostFragment())
        }



        return binding.root
    }


}