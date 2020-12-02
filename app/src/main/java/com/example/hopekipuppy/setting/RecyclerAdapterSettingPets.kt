package com.example.hopekipuppy.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hopekipuppy.R
import com.example.hopekipuppy.title.Writing
import timber.log.Timber

class RecyclerAdapterSettingPets(val context: Context, val petList : ArrayList<Pet>) : RecyclerView.Adapter<RecyclerAdapterSettingPets.Holder>() {
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
//            if (pet.name != "") {
//                val Id = context.resources.getIdentifier(
//                    pet.name,
//                    "drawable",
//                    context.packageName
//                )
//                main_Image?.setImageResource(Id)
//            } else {
//                main_Image?.setImageResource(R.mipmap.ic_launcher)
//            }
            pet_name?.text = pet.name
            Timber.d(pet.name)
        }
    }
}