package com.example.hopekipuppy.title

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hopekipuppy.MainActivity
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentMainFoundBinding
import com.example.hopekipuppy.title.MainFoundFragmentDirections

class MainFoundFragment : Fragment() {

    private lateinit var binding : FragmentMainFoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_main_found, container, false)

        binding.FoundGoButton.setBackgroundColor(Color.WHITE)
        binding.FoundGoButton.setTextColor(Color.BLACK)


        //버튼 프래그먼트 연결
        binding.LostGoButton.setOnClickListener {
            findNavController().navigate(MainFoundFragmentDirections.actionMainFoundFragmentToMainLostFragment())
        }
        binding.FoundWriteButton.setOnClickListener {
            findNavController().navigate(MainFoundFragmentDirections.actionMainFoundFragmentToWriteFoundFragment())
        }
        binding.LostWriteButton.setOnClickListener {
            findNavController().navigate(MainFoundFragmentDirections.actionMainFoundFragmentToWriteLostFragment())
        }
        binding.SettingGoButton.setOnClickListener {
            findNavController().navigate(MainFoundFragmentDirections.actionMainFoundFragmentToSettingFragment())
        }

        binding.DongText.text = MainActivity.login.user_town

        // found 목록 가져오기


        return binding.root
    }
}