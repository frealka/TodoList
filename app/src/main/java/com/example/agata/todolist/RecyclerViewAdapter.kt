package com.example.agata.todolist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.agata.todolist.R.id.parent

class RecyclerViewAdapter(val items : ArrayList<CardItem>, val context: Context) :
    RecyclerView.Adapter<ItemCardViewHolder>( ) {

    override fun onBindViewHolder(p0: ItemCardViewHolder, p1: Int) {
        p0.bind(context, items[p1])
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemCardViewHolder {
        return ItemCardViewHolder(LayoutInflater.from(p0.context), p0)
    }


    override fun getItemCount(): Int {
        return items.size
    }
}
