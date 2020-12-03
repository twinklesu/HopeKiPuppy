package com.example.hopekipuppy

import android.content.Context
import android.hardware.SensorManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.hopekipuppy.databinding.FragmentSetLocationBinding
import com.example.hopekipuppy.title.LostSimple
import timber.log.Timber
import java.util.*
import kotlin.properties.Delegates


@Suppress("DEPRECATION")
class SetLocationFragment : Fragment() {

    private lateinit var binding : FragmentSetLocationBinding

    companion object{
        var lat by Delegates.notNull<Double>()
        var long by Delegates.notNull<Double>()
        var addr: String? = null
    }


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

        binding.btAddLoc.setOnClickListener{
            addr = addr + " " +binding.etDetailLoc.text
            WriteLostFragment.addr = addr.toString()
            WriteLostFragment.lat = lat
            WriteLostFragment.long = long
            findNavController().navigate(SetLocationFragmentDirections.actionSetLocationFragmentToWriteLostFragment())
        }






        return binding.root
    }


}