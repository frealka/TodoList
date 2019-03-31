package com.example.agata.todolist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class RecyclerViewAdapter(val items : ArrayList<CardItem>, val context: Context) :
    RecyclerView.Adapter<ItemCardViewHolder>( ) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemCardViewHolder {
        return ItemCardViewHolder(LayoutInflater.from(p0.context), p0)
    }

    override fun onBindViewHolder(p0: ItemCardViewHolder, p1: Int) {
        p0.bind(context, items[p1])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun insert(item: CardItem){
        items.add(0, item)
        notifyItemInserted(0)
    }

    fun remove(item: CardItem){
        val pos = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(pos)
    }
}
