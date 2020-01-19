package com.example.filesystem

import android.graphics.Color
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class Adapter(private var list : ArrayList<Files>, val listener: (Int) -> Unit) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, listener)

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(pos: Int, listener: (Int) -> Unit) = with(itemView) {
            var name : TextView = itemView.findViewById(android.R.id.text1)
            name.text = list[pos].name
            name.setTextColor(if (list[pos].flag == 1) Color.BLUE else Color.RED)
            name.setOnClickListener {
                listener(pos)
            }
        }
    }
}