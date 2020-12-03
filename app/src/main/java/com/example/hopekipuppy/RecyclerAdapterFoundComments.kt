package com.example.hopekipuppy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapterFoundComments (val context: Context, val comments: ArrayList<Comment>) : RecyclerView.Adapter<RecyclerAdapterFoundComments.Holder>() {
    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(comments[position], context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_comment, parent, false)
        return Holder(view)
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val comment_id = itemView?.findViewById<TextView>(R.id.comments_id)
        val comment_unit = itemView?.findViewById<TextView>(R.id.comments)

        fun bind(comment: Comment, context: Context) {
            comment_unit?.text = comment.comment
            comment_id?.text = comment.user_id
        }
    }
}