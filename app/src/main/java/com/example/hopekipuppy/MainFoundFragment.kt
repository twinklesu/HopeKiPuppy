package com.example.hopekipuppy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hopekipuppy.databinding.FragmentMainFoundBinding
import com.example.hopekipuppy.databinding.FragmentWriteFoundBinding

class MainFoundFragment : Fragment() {

    private lateinit var binding : FragmentMainFoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_found, container, false)
        return binding.root
    }
}