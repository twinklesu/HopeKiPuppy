package com.example.hopekipuppy.title

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentMainLostBinding

class MainLostFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding : FragmentMainLostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_main_lost, container, false)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        
        return binding.root

    }
}