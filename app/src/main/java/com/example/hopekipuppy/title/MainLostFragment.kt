package com.example.hopekipuppy.title

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentMainLostBinding
import androidx.recyclerview.widget.GridLayoutManager as GridLayoutManager

class MainLostFragment : Fragment() {
    private lateinit var viewModel: LostViewModel
    private lateinit var binding : FragmentMainLostBinding

    var writinglist = arrayListOf<Writing>(
        Writing("common","test title"),
        Writing("common1","test title_2"),
        Writing("common3","test title_3")
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_main_lost, container, false)


        viewModel = ViewModelProvider(this).get(LostViewModel::class.java)

        //버튼 프래그먼트 연결
        binding.FoundGoButton.setOnClickListener {
            findNavController().navigate(MainLostFragmentDirections.actionMainLostFragmentToMainFoundFragment())
        }
        binding.FoundWriteButton.setOnClickListener {
            findNavController().navigate(MainLostFragmentDirections.actionMainLostFragmentToWriteFoundFragment())
        }
        binding.LostWriteButton.setOnClickListener {
            findNavController().navigate(MainLostFragmentDirections.actionMainLostFragmentToWriteLostFragment())
        }
        binding.SettingGoButton.setOnClickListener {
            findNavController().navigate(MainLostFragmentDirections.actionMainLostFragmentToSettingFragment())
        }
        binding.SetKeywordButton.setOnClickListener {
            findNavController().navigate(MainLostFragmentDirections.actionMainLostFragmentToSetKeywordFragment())
        }


        val LostAdapter = MainLostAdapter(binding.LostRecyclerView.context, writinglist)
        binding.LostRecyclerView.adapter = LostAdapter

        val Gm = GridLayoutManager(binding.LostRecyclerView.context,2)
        binding.LostRecyclerView.layoutManager = Gm
        binding.LostRecyclerView.setHasFixedSize(true)


        return binding.root

    }
}