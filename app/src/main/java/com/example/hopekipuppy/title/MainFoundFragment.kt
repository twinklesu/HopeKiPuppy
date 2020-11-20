package com.example.hopekipuppy.title

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentMainFoundBinding

class MainFoundFragment : Fragment() {
    private lateinit var viewModel: FoundViewModel


    private lateinit var binding : FragmentMainFoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_main_found, container, false)


        viewModel = ViewModelProvider(this).get(FoundViewModel::class.java)


        return binding.root
    }
}