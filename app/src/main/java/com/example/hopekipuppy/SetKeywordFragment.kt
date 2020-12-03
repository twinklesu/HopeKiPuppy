package com.example.hopekipuppy

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import com.example.hopekipuppy.databinding.FragmentSetKeywordBinding
import com.example.hopekipuppy.databinding.FragmentWriteFoundBinding

class SetKeywordFragment : Fragment() {

    private lateinit var binding : FragmentSetKeywordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_keyword, container, false)

        return binding.root
    }
}