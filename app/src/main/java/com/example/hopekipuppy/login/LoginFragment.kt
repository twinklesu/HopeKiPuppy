package com.example.hopekipuppy.login

import android.Manifest
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.hopekipuppy.MainActivity
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentLoginBinding
import com.google.android.gms.location.*
import org.json.JSONException
import timber.log.Timber
import java.io.IOException
import java.util.*


class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var geocoder: Geocoder
    private lateinit var sf : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val notId = "notID"
        val notPW = "notPW"
        val getId = sharedPref?.getString("id",notId)
        val getPW = sharedPref?.getString("pw", notPW)

        if (getId != notId && getPW != notPW){
            var login = Login(id = getId!!, pw = getPW!!)
            volley_login(login)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        geocoder = Geocoder(requireContext(), Locale.KOREAN)

        binding.btLogin.setOnClickListener {
            onClickLoginButton()
        }

        binding.btSignIn.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToJoinFragment())
        }


        return binding.root
    }

    /*
    on click listener for login button
     */
    private fun onClickLoginButton() {
        val id = binding.tvId.text.toString()
        val pw = binding.tvPw.text.toString()
        val login = Login(id = id, pw = pw)

        volley_login(login)
        sf = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sf.edit()){
            putString("id",id)
            putString("pw",pw)
            commit()
        }



    }


    private fun volley_login(login: Login) {
        val queue: RequestQueue = Volley.newRequestQueue(this.context)
        Timber.d(login.id)
        Timber.d(login.pw)
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/login/${login.id}/"

        val jsonArrayList = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    val result_array = response
                    val result = result_array.getJSONObject(0)
                    val pw: String?
                    if (!result.isNull("user_pw")) {
                        pw = result.getString("user_pw")
                        Timber.d("get password: ${pw}, input password: ${login.pw}}")
                        if (pw == login.pw) {
                            MainActivity.login = login
                            MainActivity.login.user_nicknm = result.getString("user_nicknm")
                            MainActivity.login.user_tel = result.getString("user_tel")
                            getLoc()
                            // 위치 받고 넘어감
                        } else Toast.makeText(this.context, "Check your PW", Toast.LENGTH_SHORT)
                            .show()
                    } else Toast.makeText(
                        this.context,
                        "Check your ID and PW",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) {
            it.printStackTrace()
            Timber.d("login request fail") }
        queue.add(jsonArrayList)
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
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainLostFragment())
        }
    }



}

data class Login(
    val id: String,
    val pw: String,
    var user_nicknm: String? = null,
    var user_tel: String? = null,
    var user_town: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
)


