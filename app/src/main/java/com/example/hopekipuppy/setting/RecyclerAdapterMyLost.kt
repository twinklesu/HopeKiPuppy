package com.example.hopekipuppy.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.hopekipuppy.DetailLostFragment
import com.example.hopekipuppy.MyPetFragment
import com.example.hopekipuppy.R
import com.example.hopekipuppy.title.LostSimple

class RecyclerAdapterMyLost(val context: Context, val lostSimple: ArrayList<LostSimple>) : RecyclerView.Adapter<RecyclerAdapterMyLost.Holder>() {
    override fun getItemCount(): Int {
        return lostSimple.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(lostSimple[position], context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_setting_my_lost, parent, false)
        return Holder(view)
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val writing_title = itemView?.findViewById<TextView>(R.id.setting_my_lost_writing)

        fun bind(writing: LostSimple, context: Context) {

            writing_title?.text = writing.title
            writing_title?.setOnClickListener {
                DetailLostFragment.lostSimple = writing
                val navController = Navigation.findNavController(itemView)
                navController.navigate(SettingFragmentDirections.actionSettingFragmentToDetailLostFragment())
            }
        }
    }
}