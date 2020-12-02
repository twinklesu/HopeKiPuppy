package com.example.hopekipuppy

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.hopekipuppy.databinding.FragmentRegisterPetBinding
import org.json.JSONObject
import timber.log.Timber
import java.util.*

class RegisterPetFragment : Fragment() {

    private lateinit var binding : FragmentRegisterPetBinding
    private lateinit var petImageUrl : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register_pet,
            container,
            false
        )

        binding.btBringImage.setOnClickListener { selectGallery() }
        binding.btRegister.setOnClickListener { registerMyPet() }
        return binding.root
    }

    private fun registerMyPet(){
        // 이름이랑 나이 없으면 안됨
        if (binding.etPetName.text.isNullOrEmpty() || binding.etPetAge.text.isNullOrEmpty()) {
            Toast.makeText(this.context, "Name and age are mandatory", Toast.LENGTH_SHORT).show()
            return
        }

        val queue = Volley.newRequestQueue(this.context)
        val url = "http://awsdjango.eba-82andig8.ap-northeast-2.elasticbeanstalk.com/reg-pet/"

        val json = HashMap<String?, String?>()

        json["user_id"] = MainActivity.login.id
        json["image"] = petImageUrl
        json["name"] = binding.etPetName.text.toString()
        json["age"] = binding.etPetAge.text.toString()
        json["variety"] = binding.etPetVariety.text.toString()
        json["reg_num"] = binding.etPetRegNum.text.toString()
        json["character"] = binding.etPetCharacter.text.toString()

        val parameter = JSONObject(json as Map<*, *>)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            parameter,
            {
                Timber.d("Post SUCCESS")
                MyPetFragment.petName = binding.etPetName.text.toString()
                findNavController().navigate(RegisterPetFragmentDirections.actionRegisterPetFragmentToMyPetFragment())
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
            binding.ivPet.setImageURI(selectedImageUri)
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

}