package com.example.hopekipuppy.title

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.hopekipuppy.MainActivity
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentMainLostBinding
import com.google.android.gms.location.*
import org.json.JSONException
import timber.log.Timber
import java.io.IOException
import java.util.*
import androidx.recyclerview.widget.GridLayoutManager as GridLayoutManager

class MainLostFragment : Fragment() {
    private lateinit var viewModel: LostViewModel
    private lateinit var binding : FragmentMainLostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_main_lost, container, false)

        binding.DongText.text = MainActivity.login.user_town
        Timber.d(binding.DongText.text.toString())

        binding.LostGoButton.setBackgroundColor(Color.WHITE)
        binding.LostGoButton.setTextColor(Color.BLACK)


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



        // lost 목록 가져오기
        val queue: RequestQueue = Volley.newRequestQueue(this.context)
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/get-lost-list/"
        val lost_list: ArrayList<LostSimple> = ArrayList()

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    val result_list = response
                    for (i in 0..response.length() - 1) {
                        val result = result_list.getJSONObject(i)
                        if (compareLatLong(result.getDouble("latitude"), result.getDouble("longitude"))){
                            val obj = LostSimple(result.getInt("post_id"), result.getString("title"), result.getString("image"))
                            lost_list.add(obj)
                        }
                        else {
                            Timber.d("not in 2km ${result.getString("lost_loc")}")
                        }
                    }
                    val LostAdapter = MainLostAdapter(binding.LostRecyclerView.context, lost_list)
                    binding.LostRecyclerView.adapter = LostAdapter
                    val Gm = GridLayoutManager(binding.LostRecyclerView.context,2)
                    binding.LostRecyclerView.layoutManager = Gm
                    binding.LostRecyclerView.setHasFixedSize(true)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) {
            it.printStackTrace()
            Timber.d("test request fail")
        }
        queue.add(jsonArrayRequest)
        return binding.root
    }

    private fun compareLatLong(lat: Double, long: Double): Boolean {
        val user_loc = Location("user")
        val post_loc = Location("lost")

        user_loc.longitude = MainActivity.login.longitude!!
        user_loc.latitude = MainActivity.login.latitude!!

        post_loc.longitude = long
        post_loc.latitude = lat

        val distance = user_loc.distanceTo(post_loc)
        Timber.d("distance ${distance}")

        return distance <= 2000
    }
}

data class LostSimple(val post_id: Int, val title: String, val image: String)