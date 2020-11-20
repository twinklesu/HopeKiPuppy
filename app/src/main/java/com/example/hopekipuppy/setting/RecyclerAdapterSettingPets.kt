package com.example.hopekipuppy.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hopekipuppy.R

class RecyclerAdapterSettingPets : RecyclerView.Adapter<RecyclerAdapterSettingPets.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterSettingPets.Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_setting_pets, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerAdapterSettingPets.Holder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}