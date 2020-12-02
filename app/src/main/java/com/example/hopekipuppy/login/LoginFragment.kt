package com.example.hopekipuppy.login

import android.Manifest
import android.content.Context
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.btLogin.setOnClickListener {
            onClickLoginButton()
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
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainLostFragment())
                            MainActivity.login = login
                            MainActivity.login.user_nicknm = result.getString("user_nicknm")
                            MainActivity.login.user_tel = result.getString("user_tel")
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

