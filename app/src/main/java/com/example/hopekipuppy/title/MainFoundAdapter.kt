package com.example.hopekipuppy.title

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hopekipuppy.DetailFoundFragment
import com.example.hopekipuppy.R
import java.util.ArrayList

class MainFoundAdapter (val context: Context, val FoundList: ArrayList<Found>) : RecyclerView.Adapter<MainFoundAdapter.Holder>() {

    override fun getItemCount(): Int {
        return FoundList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(FoundList[position], context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_main_recycler, parent, false)
        return Holder(view)
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val main_Image = itemView?.findViewById<ImageView>(R.id.main_image)
        val main_Text = itemView?.findViewById<TextView>(R.id.main_text)

        fun bind(found: Found, context: Context) {
            Glide.with(context)
                    .load(found.image)
                    .into(main_Image!!)
            main_Text?.text = found.title
            main_Image.setOnClickListener {
                DetailFoundFragment.found = found
                val navController = Navigation.findNavController(itemView)
                navController.navigate(MainFoundFragmentDirections.actionMainFoundFragmentToDetailFoundFragment())
            }
        }
    }
}