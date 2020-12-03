package com.example.hopekipuppy

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.Image
import android.media.ImageReader
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.hopekipuppy.databinding.FragmentRegisterPetBinding
import com.example.hopekipuppy.databinding.FragmentWriteLostBinding
import com.example.hopekipuppy.setting.Pet
import com.example.hopekipuppy.setting.RecyclerAdapterSettingPets
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.sql.Time
import java.util.*
import kotlin.properties.Delegates

class WriteLostFragment : Fragment() {

    private lateinit var binding : FragmentWriteLostBinding
    private lateinit var petImageUrl : String
    private lateinit var cal : Calendar

    companion object{
        var lat : Double = 0.0
        var long: Double = 0.0
        var addr: String = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_write_lost,
            container,
            false
        )
        cal = Calendar.getInstance()

        binding.btBringImage.setOnClickListener { selectGallery() }
        // 장소가져오기
        binding.tvLostLocation.setOnClickListener { findNavController().navigate(WriteLostFragmentDirections.actionWriteLostFragmentToSetLocationFragment())}
        binding.tvLostLocation.text = addr
        // 날짜 가져오기
        binding.tvLostDate.setOnClickListener{showDatePicker(binding.tvLostDate)}

        binding.lostWritePost.setOnClickListener { registerMyLostWriting() }

        val queue: RequestQueue = Volley.newRequestQueue(this.context)
        var url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/get-pet-list/${MainActivity.login.id}/"
        val pet_list: ArrayList<Pet> = ArrayList()

        var jsonArrayRequest = JsonArrayRequest(
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
                        val RecyclerAdapterLostMyPet = RecyclerAdapterLostMyPet(binding.recyclerMyPet.context, pet_list)
                        RecyclerAdapterLostMyPet.binding = binding
                        binding.recyclerMyPet.adapter = RecyclerAdapterLostMyPet
                        val manager = LinearLayoutManager(binding.recyclerMyPet.context, RecyclerView.HORIZONTAL, false)
                        binding.recyclerMyPet.layoutManager = manager
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

    private fun registerMyLostWriting(){
        // 이름이랑 장소 없으면 안됨
        if (binding.etPostTitle.text.isNullOrEmpty() || binding.tvLostLocation.text.isNullOrEmpty()) {
            Toast.makeText(this.context, "Title and Location are mandatory", Toast.LENGTH_SHORT).show()
            return
        }

        val queue = Volley.newRequestQueue(this.context)
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/write-post-lost/"

        val json = HashMap<String?, String?>()

        json["user_id"] = MainActivity.login.id
        json["title"] = binding.etPostTitle.text.toString()
        json["lost_loc"] = binding.tvLostLocation.text.toString()
        json["lost_date"] = binding.tvLostDate.text.toString()
        json["name"] = binding.etLostName.text.toString()
        json["age"] = binding.etLostAge.text.toString()
        json["reg_num"] = binding.etLostRegNum.text.toString()
        json["phone_num"] = binding.etLostPhoneNum.text.toString()
        json["character"] = binding.etLostCharacter.text.toString()
        json["image"] = petImageUrl


        val parameter = JSONObject(json as Map<*, *>)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            parameter,
            {
                Timber.d("Post SUCCESS")
                DetailLostFragment.newLost = NewLost(binding.etPostTitle.text.toString(), binding.tvLostLocation.text.toString(), binding.tvLostDate.text.toString(),
                        binding.etLostName.text.toString(), binding.etLostAge.text.toString().toInt(), binding.etLostRegNum.text.toString(), binding.etLostPhoneNum.text.toString(),
                        binding.etLostCharacter.text.toString(), petImageUrl)
                findNavController().navigate(WriteLostFragmentDirections.actionWriteLostFragmentToDetailLostFragment())
                // 근데 navigate할 때 값 넘겨줘야 된디

            }
        ) { error ->
            error.printStackTrace()
            Timber.d("Post FAIL")
        }
        queue.add(jsonObjectRequest)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data!!.data
            Timber.d(selectedImageUri.toString())
            binding.ivTest.setImageURI(selectedImageUri)
            val currentTime = Calendar.getInstance().timeInMillis
            val storage = MainActivity.storage
            val storageRef = storage.reference.child("images").child("${MainActivity.login.id}/${currentTime}.png")
            storageRef.putFile(selectedImageUri!!).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageRef.downloadUrl
            }.addOnCompleteListener { task ->
                petImageUrl = task.result.toString()
                Timber.d(petImageUrl)
            }
        }
    }

    private fun selectGallery() {
        val readPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (readPermission == PackageManager.PERMISSION_DENIED) {
            // 권한 없어서 요청
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                0
            )
        } else { // 권한 있음
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    /*
날짜 고르는 함수
 */
    private fun showDatePicker(view: TextView) {
        DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { datePicker, year_, month_, date_ ->
            view.text =  "${month_+1}월 ${date_}일" //month는 0부터 시작
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
    }

}


