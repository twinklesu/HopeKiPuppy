package com.example.hopekipuppy.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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
import org.json.JSONException
import timber.log.Timber


class SettingFragment : Fragment() {
    private lateinit var binding : FragmentSettingBinding
    private lateinit var petAdapter : RecyclerView.Adapter<*>
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
            findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToMyPetFragment())

        }


        // pet recycler view
        petAdapter = RecyclerAdapterSettingPets()
        recyclerView = binding.recyclerPets.apply {
            setHasFixedSize(true)
            // specify an viewAdapter
            adapter = petAdapter
        }

        volley_test()
        return binding.root
    }


    private fun volley_test() {
        val queue: RequestQueue = Volley.newRequestQueue(this.context)
        val url_test = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/test/"
        var test_result: ArrayList<Test> = ArrayList()

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url_test,
            null,
            { response ->
                try {
                    val result_list = response
                    for (i in 0..response.length()-1) {
                        val result = result_list.getJSONObject(i)
                        val test_obj = Test(result.getInt("id") , result.getString("test"))
                        test_result.add(test_obj)
                        Timber.d(test_obj.toString())
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) {
            it.printStackTrace()
            Timber.d("test request fail") }

        queue.add(jsonArrayRequest)
        Timber.d(test_result.toString())
    }
}

data class Test(val id: Int, val test: String)
