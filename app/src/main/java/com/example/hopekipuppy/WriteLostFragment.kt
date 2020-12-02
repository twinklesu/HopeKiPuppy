package com.example.hopekipuppy

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.hopekipuppy.databinding.FragmentWriteLostBinding
import timber.log.Timber
import java.io.File

class WriteLostFragment : Fragment() {

    private lateinit var binding : FragmentWriteLostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_lost, container, false)

        binding.btBringImage.setOnClickListener {
            selectGallery()
        }

        return binding.root
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
        if (requestCode == 0 && resultCode == RESULT_OK) {
            //val imageBitmap = data!!.extras!!.get("data") as Bitmap
            //binding.ivTest.setImageBitmap(imageBitmap)
            val selectedImageUri = data!!.data
            Timber.d(selectedImageUri.toString())
            binding.ivTest.setImageURI(selectedImageUri)
            // upload

        }
    }

    private fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.getContentResolver().query(contentUri!!, proj, null, null, null)
            val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }
}

