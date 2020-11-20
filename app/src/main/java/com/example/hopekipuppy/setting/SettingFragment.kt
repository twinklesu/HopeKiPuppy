package com.example.hopekipuppy.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    private lateinit var binding : FragmentSettingBinding
    private lateinit var petAdapter : RecyclerView.Adapter<*>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)

        // pet recycler view
        petAdapter = RecyclerAdapterSettingPets()
        recyclerView = binding.recyclerPets.apply {
            setHasFixedSize(true)
            // specify an viewAdapter
            adapter = petAdapter
        }








        return binding.root
    }
}