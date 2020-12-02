package com.example.hopekipuppy.title

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.hopekipuppy.MainActivity
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentMainLostBinding
import com.example.hopekipuppy.setting.Pet
import com.google.android.gms.location.*
import org.json.JSONException
import timber.log.Timber
import java.io.IOException
import java.util.*
import androidx.recyclerview.widget.GridLayoutManager as GridLayoutManager

class MainLostFragment : Fragment() {
    private lateinit var viewModel: LostViewModel
    private lateinit var binding : FragmentMainLostBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    var writinglist = arrayListOf<Writing>(
        Writing("common","test title"),
        Writing("common1","test title_2"),
        Writing("common3","test title_3")
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_main_lost, container, false)


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


        getLoc()
        binding.DongText.text = MainActivity.login.user_town

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
                        val obj = LostSimple(result.getInt("post_id"), result.getString("title"), result.getString("image"))
                        lost_list.add(obj)
                    }
                    // 여기서
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


    private fun getLoc() {
        var latitude: Double
        var longitude: Double

        // get permission
        val lm = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            val PERMISSIONS = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, 100)
        }

        // get location
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) { locationResult ?: return }}
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            latitude = location.latitude
            longitude = location.longitude
            Timber.d("lat ${latitude}, long ${longitude}")
            MainActivity.login.latitude = latitude
            MainActivity.login.longitude = longitude

            // get 동
            val geocoder = Geocoder(this.context, Locale.KOREAN)
            var addressList: List<Address>? = null
            try {
                do {
                    addressList = geocoder.getFromLocation(latitude, longitude, 1)
                } while (addressList!!.isEmpty())
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val dong = addressList!![0].thoroughfare ?:"error"
            Timber.d(dong)
            MainActivity.login.user_town = dong
        }
    }
}

data class LostSimple(val post_id: Int, val title: String, val image: String)