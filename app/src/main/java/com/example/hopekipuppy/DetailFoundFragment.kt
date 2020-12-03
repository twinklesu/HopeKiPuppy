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
import com.example.hopekipuppy.databinding.FragmentDetailFoundBinding
import com.example.hopekipuppy.databinding.FragmentDetailLostBinding
import com.example.hopekipuppy.title.Found
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.util.HashMap

class DetailFoundFragment : Fragment() {

    private lateinit var binding : FragmentDetailFoundBinding

    companion object{
        lateinit var found: Found
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_found, container, false)

        //댓글쓰기
        binding.btComment.setOnClickListener {
            if (binding.etComment.text.isNotBlank()){
                writeComment(DetailLostFragment.lostSimple!!.post_id, MainActivity.login.id, binding.etComment.text.toString())
            }
            else {
                Toast.makeText(this.context, "No comment to write", Toast.LENGTH_SHORT).show()
            }
        }

        if(found.post_id == null) {
            Glide.with(this.requireContext())
                    .load(found.image)
                    .into(binding.ivTest)
            binding.tvPostTitle.text = found.title
            binding.tvFoundLocation.text = found.found_loc
            binding.tvFoundDate.text = found.found_date
            binding.tvFoundDetail.text = found.detail
        }
        else {
            val queue: RequestQueue = Volley.newRequestQueue(this.context)
            binding.linearComment.visibility = View.VISIBLE
            // 글 가져오기
            Glide.with(this.requireContext())
                    .load(found.image)
                    .into(binding.ivTest)
            binding.tvPostTitle.text = found.title
            binding.tvFoundLocation.text = found.found_loc
            binding.tvFoundDate.text = found.found_date
            binding.tvFoundDetail.text = found.detail
            /*
            댓글 받아오기
             */
            val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/get-lost-comment/${found.post_id}"
            val comment_list: ArrayList<Comment> = ArrayList()

            val jsonArrayRequest = JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    { response ->
                        try {
                            val result_list = response
                            for (i in 0..response.length() - 1) {
                                val result = result_list.getJSONObject(i)
                                val obj = Comment(result.getString("user_id"), result.getString("comment"))
                                comment_list.add(obj)
                            }
                            Timber.d(comment_list.toString())
                            // 여기 댓글 recycler
                        } catch (e: JSONException) {
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
            ) {
                it.printStackTrace()
                Timber.d("request fail") }
            queue.add(jsonArrayRequest)
        }
        return binding.root
    }


    private fun writeComment(post_id: Int, user_id:String, comment:String){

        val queue = Volley.newRequestQueue(this.context)
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/write-comment-found/"

        val json = HashMap<String?, String?>()

        json["post_id"] = post_id.toString()
        json["user_id"] = user_id
        json["comment"] = comment

        val parameter = JSONObject(json as Map<*, *>)
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                url,
                parameter,
                {
                    Timber.d("Post SUCCESS")
                    findNavController().navigate(DetailLostFragmentDirections.actionDetailLostFragmentSelf())
                }
        ) { error ->
            error.printStackTrace()
            Timber.d("Post FAIL")
        }
        queue.add(jsonObjectRequest)

    }
}