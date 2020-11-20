package com.example.hopekipuppy.title

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {
    private lateinit var viewModel: TitleViewModel

    private lateinit var binding : FragmentTitleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_title, container, false
        )

        viewModel = ViewModelProvider(this).get(TitleViewModel::class.java)







        return binding.root
    }


}