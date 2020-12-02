package com.example.hopekipuppy.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.hopekipuppy.MainActivity
import com.example.hopekipuppy.MyPetFragment
import com.example.hopekipuppy.R
import com.example.hopekipuppy.RegisterPetFragment
import com.example.hopekipuppy.databinding.FragmentSettingBinding
import com.example.hopekipuppy.title.MainLostAdapter
import org.json.JSONException
import timber.log.Timber


class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var petAdapter: RecyclerView.Adapter<*>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        // my information
        binding.tvNickname.text = MainActivity.login.user_nicknm
        binding.tvPhoneNum.text = MainActivity.login.user_tel
        binding.tvMyTown.text = MainActivity.login.user_town
        // 데이터 넘기기

        binding.btRegPet.setOnClickListener { findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToRegisterPetFragment()) }
        binding.btTemp.setOnClickListener {
            MyPetFragment.petName = "Rooney"
            findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToMyPetFragment())
        }


        // pet 목록 가져오기
        val queue: RequestQueue = Volley.newRequestQueue(this.context)
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/get-pet-list/${MainActivity.login.id}/"
        val pet_list: ArrayList<Pet> = ArrayList()

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    val result_list = response
                    for (i in 0..response.length() - 1) {
                        val result = result_list.getJSONObject(i)
                        val obj = Pet(result.getString("name"), result.getInt("age"), result.getString("variety"), result.getString("image"), result.getString("reg_num"), result.getString("character"))
                        pet_list.add(obj)
                    }
                    val RecyclerAdapterSettingPets = RecyclerAdapterSettingPets(binding.recyclerPets.context, pet_list)
                    binding.recyclerPets.adapter = RecyclerAdapterSettingPets
                    val manager = LinearLayoutManager(binding.recyclerPets.context, RecyclerView.HORIZONTAL, false)
                    binding.recyclerPets.layoutManager = manager
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



}

data class Pet(val name:String, val age: Int, val variety: String, val image: String, var reg_num: String, var character: String)
