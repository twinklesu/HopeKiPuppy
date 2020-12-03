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
import com.example.hopekipuppy.DetailLostFragment
import com.example.hopekipuppy.MyPetFragment
import com.example.hopekipuppy.R
import com.example.hopekipuppy.setting.SettingFragmentDirections
import java.util.ArrayList

class MainLostAdapter(val context: Context, val lostList: ArrayList<LostSimple>) : RecyclerView.Adapter<MainLostAdapter.Holder>() {

    override fun getItemCount(): Int {
        return lostList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(lostList[position], context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_main_recycler, parent, false)
        return Holder(view)
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val main_Image = itemView?.findViewById<ImageView>(R.id.main_image)
        val main_Text = itemView?.findViewById<TextView>(R.id.main_text)

        fun bind(lost: LostSimple, context: Context) {
            Glide.with(context)
                    .load(lost.image)
                    .into(main_Image!!)
            main_Text?.text = lost.title
            main_Image.setOnClickListener {
                DetailLostFragment.lostSimple = lost
                val navController = Navigation.findNavController(itemView)
                navController.navigate(MainLostFragmentDirections.actionMainLostFragmentToDetailLostFragment())
            }
        }
    }
}

