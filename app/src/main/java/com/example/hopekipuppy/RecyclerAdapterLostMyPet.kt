package com.example.hopekipuppy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hopekipuppy.databinding.FragmentWriteLostBinding
import com.example.hopekipuppy.setting.Pet


class RecyclerAdapterLostMyPet(val context: Context, val petList: ArrayList<Pet>) : RecyclerView.Adapter<RecyclerAdapterLostMyPet.Holder>() {
    lateinit var binding : FragmentWriteLostBinding
    lateinit var writeLostFragment : WriteLostFragment
    override fun getItemCount(): Int {
        return petList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(petList[position], context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_setting_pets, parent, false)
        return Holder(view)
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val pet_Image = itemView?.findViewById<ImageView>(R.id.image_pet)
        val pet_name = itemView?.findViewById<TextView>(R.id.tv_pet_name)

        fun bind(pet: Pet, context: Context) {
            Glide.with(context)
                    .load(pet.image)
                    .into(pet_Image!!)
            pet_name?.text = pet.name
            pet_Image.setOnClickListener {
                binding.etLostName.setText(pet.name)
                binding.etLostAge.setText(pet.age.toString())
                binding.etLostCharacter.setText(pet.character)
                binding.etLostPhoneNum.setText(MainActivity.login.user_tel)
                binding.etLostPhoneNum.editableText
                binding.etLostRegNum.setText(pet.reg_num)
                WriteLostFragment.petImageUrl = pet.image
                Glide.with(context)
                        .load(pet.image)
                        .into(binding.ivTest)
                binding.ivTest.visibility = View.VISIBLE
            }
        }
    }
}