package com.example.hopekipuppy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.hopekipuppy.databinding.FragmentDetailLostBinding
import com.example.hopekipuppy.databinding.FragmentMainLostBinding
import com.example.hopekipuppy.login.LoginFragmentDirections
import com.example.hopekipuppy.title.LostSimple
import org.json.JSONException
import timber.log.Timber


class DetailLostFragment : Fragment() {

    private lateinit var binding : FragmentDetailLostBinding

    companion object{
        lateinit var lostSimple : LostSimple
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d(lostSimple.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_lost, container, false)

        val post_id = lostSimple.post_id
        // 글 가져오기
        val queue: RequestQueue = Volley.newRequestQueue(this.context)
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/write-post-lost/${post_id}/"

        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    try {
                        val result = response
                        Glide.with(this.requireContext())
                                .load(result.getString("image"))
                                .into(binding.ivImage)
                        binding.tvPostTitle.text = result.getString("title")
                        binding.tvLostLocation.text = result.getString("lost_loc")
                        binding.tvLostDate.text = result.getString("lost_date")
                        binding.tvLostName.text = result.getString("name")
                        binding.tvLostAge.text = result.getInt("age").toString()
                        binding.tvLostRegNum.text = result.getString("reg_num")
                        binding.tvLostPhoneNum.text = result.getString("phone_num")
                        binding.tvLostCharacter.text = result.getString("character")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
        ) {
            it.printStackTrace()
            Timber.d("login request fail") }
        queue.add(jsonObjectRequest)




        return binding.root
    }

}

