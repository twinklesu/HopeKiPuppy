package com.example.hopekipuppy.title

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.hopekipuppy.R

class MainLostAdapter(val context: Context, val writingList : ArrayList<Writing>) : RecyclerView.Adapter<MainLostAdapter.Holder>() {

    override fun getItemCount(): Int {
        return writingList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(writingList[position], context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_main_recycler, parent, false)
        return Holder(view)
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val main_Image = itemView?.findViewById<ImageView>(R.id.main_image)
        val main_Text = itemView?.findViewById<TextView>(R.id.main_text)

        fun bind(writing: Writing, context: Context) {
            if (writing.main_image != "") {
                val Id = context.resources.getIdentifier(
                    writing.main_image,
                    "drawable",
                    context.packageName
                )
                main_Image?.setImageResource(Id)
            } else {
                main_Image?.setImageResource(R.mipmap.ic_launcher)
            }
            main_Text?.text = writing.main_text
        }
    }
}

