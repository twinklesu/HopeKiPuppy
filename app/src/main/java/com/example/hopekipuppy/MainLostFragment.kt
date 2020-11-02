package com.example.hopekipuppy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hopekipuppy.databinding.FragmentMainLostBinding
import com.example.hopekipuppy.databinding.FragmentWriteFoundBinding

class MainLostFragment : Fragment() {

    private lateinit var binding : FragmentMainLostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_lost, container, false)
        return binding.root
    }
}