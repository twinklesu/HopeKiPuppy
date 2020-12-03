package com.example.hopekipuppy

import android.app.ActionBar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.example.hopekipuppy.databinding.FragmentTitleBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TitleFragment : Fragment() {

    val SPLASH_VIEW_TIME: Long = 2000 //2초간 스플래시 화면을 보여줌 (ms)

    private lateinit var binding : FragmentTitleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(timeMillis = SPLASH_VIEW_TIME)
            findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToLoginFragment())
        }

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_title, container, false
        )






        return binding.root
    }


}