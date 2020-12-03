package com.example.hopekipuppy.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.hopekipuppy.DetailFoundFragment
import com.example.hopekipuppy.DetailLostFragment
import com.example.hopekipuppy.R
import com.example.hopekipuppy.title.Found
import com.example.hopekipuppy.title.LostSimple

class RecyclerAdapterMyFound (val context: Context, val found: ArrayList<Found>) : RecyclerView.Adapter<RecyclerAdapterMyFound.Holder>() {
    override fun getItemCount(): Int {
        return found.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(found[position], context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_setting_my_writing, parent, false)
        return Holder(view)
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val writing_title = itemView?.findViewById<TextView>(R.id.setting_my_lost_writing)

        fun bind(writing: Found, context: Context) {

            writing_title?.text = writing.title
            writing_title?.setOnClickListener {
                DetailFoundFragment.found = writing
                val navController = Navigation.findNavController(itemView)
                navController.navigate(SettingFragmentDirections.actionSettingFragmentToDetailFoundFragment())
            }
        }
    }
}