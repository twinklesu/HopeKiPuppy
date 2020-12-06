package com.example.hopekipuppy.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.hopekipuppy.*
import com.example.hopekipuppy.databinding.FragmentJoinBinding
import com.example.hopekipuppy.title.Found
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.util.HashMap


class JoinFragment : Fragment() {

    private lateinit var binding: FragmentJoinBinding
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_join, container, false)

        var validId = false
        var validTel = false

        binding.btIdValid.setOnClickListener {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            if (binding.etId.text.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Enter ID", Toast.LENGTH_SHORT).show()
            }
            else{
                val queue: RequestQueue = Volley.newRequestQueue(this.context)
                val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/validate-id/${binding.etId.text}/"

                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    { response ->
                        try {
                            val result = response
                            val valid = result.getBoolean("message")
                            if (valid) {
                                validId = true
                                Toast.makeText(requireContext(), "Valid ID", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(requireContext(), "Use another ID", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                ) {
                    it.printStackTrace()
                    Timber.d("id vlaid fail") }
                queue.add(jsonObjectRequest)
            }
        }

        binding.btTelValid.setOnClickListener {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            if (binding.etTel.text.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Enter your phone number", Toast.LENGTH_SHORT).show()
            }
            else{
                val queue: RequestQueue = Volley.newRequestQueue(this.context)
                val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/validate-tel/${binding.etTel.text}/"

                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    { response ->
                        try {
                            val result = response
                            val valid = result.getBoolean("message")
                            if (valid) {
                                validTel = true
                                Toast.makeText(requireContext(), "Valid phone number", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(requireContext(), "You joined already", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                ) {
                    it.printStackTrace()
                    Timber.d("tel valid fail") }
                queue.add(jsonObjectRequest)
            }
        }

        binding.btSignIn.setOnClickListener {
            if (validId && validTel && binding.etPw.text.isNotEmpty() && binding.etPw2.text.isNotEmpty()){
                val queue = Volley.newRequestQueue(this.context)
                if (binding.etPw.text.toString() != binding.etPw2.text.toString()){
                    Toast.makeText(requireContext(), "Check your PW", Toast.LENGTH_SHORT).show()
                }
                else{
                    // firebase token
                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Timber.d("Fetching FCM registration token failed ${task.exception}")
                            return@OnCompleteListener
                        }
                        // Get new FCM registration token
                        token = task.result.toString()

                        Timber.d("token: ${token}")
                        join(queue = queue)
                    })
                    findNavController().navigate(JoinFragmentDirections.actionJoinFragmentToLoginFragment())
                }
            }

        }
        return binding.root
    }

    private fun join(queue: RequestQueue){
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/join/"

        val json = HashMap<String?, String?>()

        json["user_id"] = binding.etId.text.toString()
        json["user_pw"] = binding.etPw.text.toString()
        json["user_nicknm"] = ""
        json["user_tel"] = binding.etTel.text.toString()
        json["token"] = token

        val parameter = JSONObject(json as Map<*, *>)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            parameter,
            {
                Timber.d("Post SUCCESS")
            }
        ) { error ->
            error.printStackTrace()
            Timber.d("Post FAIL")
        }
        queue.add(jsonObjectRequest)
    }




    }

