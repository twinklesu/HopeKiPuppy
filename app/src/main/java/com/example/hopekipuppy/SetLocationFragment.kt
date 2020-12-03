package com.example.hopekipuppy

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.hopekipuppy.databinding.FragmentSetLocationBinding


@Suppress("DEPRECATION")
class SetLocationFragment : Fragment() {

    private lateinit var binding : FragmentSetLocationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ft = requireFragmentManager().beginTransaction()
        ft.replace(R.id.fl_map, MapsFragment().apply {
            arguments = Bundle().apply {
                putString("addr", null)
            }
        }).commit()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_set_location,
            container,
            false
        )

        binding.btSearch.setOnClickListener{
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            if(binding.etAddr.text.isNotEmpty()) {
                val ft = requireFragmentManager().beginTransaction()
                ft.replace(R.id.fl_map, MapsFragment().apply {
                    arguments = Bundle().apply {
                        putString("addr", binding.etAddr.text.toString())
                    }
                }).commit()
            }
            else Toast.makeText(this.context, "Input location", Toast.LENGTH_SHORT).show()
        }








        return binding.root
    }


}