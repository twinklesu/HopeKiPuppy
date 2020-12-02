package com.example.hopekipuppy

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.provider.Settings.System.getString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.hopekipuppy.MainActivity.Companion.storage
import com.example.hopekipuppy.databinding.FragmentMyPetBinding
import com.example.hopekipuppy.databinding.FragmentWriteFoundBinding
import com.example.hopekipuppy.setting.SettingFragment
import com.example.hopekipuppy.setting.Test
import org.json.JSONException
import timber.log.Timber

class MyPetFragment : Fragment() {

    private lateinit var binding : FragmentMyPetBinding

    companion object{
        lateinit var petName: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_pet, container, false)

        getMyPet()

        return binding.root
    }

    private fun getMyPet() {
        val queue: RequestQueue = Volley.newRequestQueue(this.context)
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/get-pet/${MainActivity.login.id}/${petName}/"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    val result_list = response
                    val result = result_list.getJSONObject(0)
                    val url = result.getString("image")
                    Glide.with(this.requireContext())
                        .load(url)
                        .into(binding.ivPet)
                    binding.tvPetName.text = result.getString("name")
                    binding.tvPetAge.text = result.getInt("age").toString()
                    binding.tvPetVariety.text = result.getString("variety")
                    binding.tvPetRegNum.text = result.getString("reg_num")
                    binding.tvPetCharacter.text = result.getString("character")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) {
            it.printStackTrace()
            Timber.d("test request fail") }
        queue.add(jsonArrayRequest)
    }

}