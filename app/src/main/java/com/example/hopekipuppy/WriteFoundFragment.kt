package com.example.hopekipuppy

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.hopekipuppy.databinding.FragmentWriteFoundBinding
import com.example.hopekipuppy.title.Found
import org.json.JSONObject
import timber.log.Timber
import java.util.*


@Suppress("DEPRECATION")
class WriteFoundFragment : Fragment() {

    private lateinit var binding : FragmentWriteFoundBinding
    private lateinit var cal: Calendar
    private lateinit var petImageUrl : String

    companion object{
        var lat : Double = 0.0
        var long: Double = 0.0
        var addr: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ft = requireFragmentManager().beginTransaction()
        ft.replace(R.id.fl_map, FoundMapsFragment().apply {
            arguments = Bundle().apply {
                putString("addr", null)
            }
        }).commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_found, container, false)

        cal = Calendar.getInstance()

        binding.btBringImage.setOnClickListener { selectGallery() }

        // 장소가져오기
        binding.btSearch.setOnClickListener {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            if(binding.etAddr.text.isNotEmpty()) {
                val ft = requireFragmentManager().beginTransaction()
                ft.replace(R.id.fl_map, FoundMapsFragment().apply {
                    arguments = Bundle().apply {
                        putString("addr", binding.etAddr.text.toString())
                    }
                }).commit()
            }
            else Toast.makeText(this.context, "Input location", Toast.LENGTH_SHORT).show()
        }

        binding.btSetLoc.setOnClickListener {
            binding.etDetailLoc.setText(addr)
        }




        // 날짜 가져오기
        binding.tvFoundDate.setOnClickListener{showDatePicker(binding.tvFoundDate)}

        binding.foundWritePost.setOnClickListener { registerMyFoundWriting() }

        return binding.root
    }

    private fun showDatePicker(view: TextView) {
        DatePickerDialog(requireContext(), { datePicker, year_, month_, date_ ->
            view.text = "${month_ + 1}월 ${date_}일" //month는 0부터 시작
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data!!.data
            Timber.d(selectedImageUri.toString())
            binding.ivTest.setImageURI(selectedImageUri)
            binding.ivTest.visibility = View.VISIBLE
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

    private fun registerMyFoundWriting(){
        // 이름이랑 장소 없으면 안됨
        if (binding.etPostTitle.text.isNullOrEmpty() || binding.etDetailLoc.text.isNullOrEmpty()) {
            Toast.makeText(this.context, "Title and Location are mandatory", Toast.LENGTH_SHORT).show()
            return
        }

        val queue = Volley.newRequestQueue(this.context)
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/write-post-found/"

        val json = HashMap<String?, String?>()

        json["user_id"] = MainActivity.login.id
        json["title"] = binding.etPostTitle.text.toString()
        json["found_loc"] = binding.etDetailLoc.text.toString()
        json["found_date"] = binding.tvFoundDate.text.toString()
        json["detail"] = binding.etLostCharacter.text.toString()
        json["image"] = petImageUrl
        json["longitude"] = long.toString()
        json["latitude"] = lat.toString()


        val parameter = JSONObject(json as Map<*, *>)
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                url,
                parameter,
                {
                    Timber.d("Post SUCCESS")
                    DetailFoundFragment.found = Found(null, binding.etPostTitle.text.toString(), binding.etDetailLoc.text.toString(),
                            binding.tvFoundDate.text.toString(), binding.etLostCharacter.text.toString(), petImageUrl)
                    findNavController().navigate(WriteFoundFragmentDirections.actionWriteFoundFragmentToDetailFoundFragment())
                }
        ) { error ->
            error.printStackTrace()
            Timber.d("Post FAIL")
        }
        queue.add(jsonObjectRequest)
    }


}
