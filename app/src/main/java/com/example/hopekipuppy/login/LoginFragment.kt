package com.example.hopekipuppy.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentLoginBinding
import org.json.JSONException
import timber.log.Timber


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

        onClickLoginButton()

        return binding.root
    }

    /*
    on click listener for login button
     */
    private fun onClickLoginButton() {
        binding.btLogin.setOnClickListener {
            val id = binding.tvId.text.toString()
            val pw = binding.tvPw.text.toString()
            val login = Login(id, pw)
            volley_login(login)
        }
    }


    fun volley_login(login: Login) {
        val queue: RequestQueue = Volley.newRequestQueue(this.context)
        Timber.d(login.id)
        Timber.d(login.pw)
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/login/${login.id}/"

        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    try {
                        val result = response
                        val pw: String?
                        if (!result.isNull("user_pw")){
                            pw = result.getString("user_pw")
                            Timber.d("get password: ${pw}, input password: ${login.pw}}")
                            if (pw == login.pw) findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainLostFragment())
                            else Toast.makeText(this.context, "Check your PW", Toast.LENGTH_SHORT).show()
                        }
                        else Toast.makeText(this.context, "Check your ID and PW", Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
        ) {
            it.printStackTrace()
            Timber.d("login request fail") }
        queue.add(jsonObjectRequest)
    }
}

data class Login(val id: String, val pw: String)