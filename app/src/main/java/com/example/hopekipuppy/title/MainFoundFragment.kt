package com.example.hopekipuppy.title

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.hopekipuppy.MainActivity
import com.example.hopekipuppy.R
import com.example.hopekipuppy.databinding.FragmentMainFoundBinding
import com.example.hopekipuppy.title.MainFoundFragmentDirections
import org.json.JSONException
import timber.log.Timber
import java.util.ArrayList

class MainFoundFragment : Fragment() {

    private lateinit var binding : FragmentMainFoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_main_found, container, false)

        binding.FoundGoButton.setBackgroundColor(Color.WHITE)
        binding.FoundGoButton.setTextColor(Color.BLACK)


        //버튼 프래그먼트 연결
        binding.LostGoButton.setOnClickListener {
            findNavController().navigate(MainFoundFragmentDirections.actionMainFoundFragmentToMainLostFragment())
        }
        binding.FoundWriteButton.setOnClickListener {
            findNavController().navigate(MainFoundFragmentDirections.actionMainFoundFragmentToWriteFoundFragment())
        }
        binding.LostWriteButton.setOnClickListener {
            findNavController().navigate(MainFoundFragmentDirections.actionMainFoundFragmentToWriteLostFragment())
        }
        binding.SettingGoButton.setOnClickListener {
            findNavController().navigate(MainFoundFragmentDirections.actionMainFoundFragmentToSettingFragment())
        }

        binding.DongText.text = MainActivity.login.user_town

        // found 목록 가져오기
        val queue: RequestQueue = Volley.newRequestQueue(this.context)
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/get-found-list/"
        val found_list: ArrayList<Found> = ArrayList()

        val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    try {
                        val result_list = response
                        for (i in 0..response.length() - 1) {
                            val result = result_list.getJSONObject(i)
                            val obj = Found(result.getInt("post_id"), result.getString("title"), result.getString("found_loc"),
                                    result.getString("found_date"), result.getString("detail"), result.getString("image"))
                            found_list.add(obj)
                        }
                        val LostAdapter = MainFoundAdapter(binding.FoundRecyclerView.context, found_list)
                        binding.FoundRecyclerView.adapter = LostAdapter
                        val Gm = GridLayoutManager(binding.FoundRecyclerView.context,2)
                        binding.FoundRecyclerView.layoutManager = Gm
                        binding.FoundRecyclerView.setHasFixedSize(true)
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
data class Found(var post_id: Int?, val title:String, val found_loc :String, val found_date : String,val detail:String, val image:String)
