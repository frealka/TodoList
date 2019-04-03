package com.example.agata.todolist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class RecyclerViewAdapter(val context: Context) :
    RecyclerView.Adapter<ItemCardViewHolder>( ) {

    var items : ArrayList<CardItem> = arrayListOf((CardItem("XXX", "YYY", "12.05.2020", "BBB")))

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemCardViewHolder {
        return ItemCardViewHolder(LayoutInflater.from(p0.context), p0)
    }

    override fun onBindViewHolder(p0: ItemCardViewHolder, p1: Int) {
        p0.bind(context, items[p1])
        p0.itemView.setOnLongClickListener{
            remove(p1)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun insert(item: CardItem){
        items.add(0, item)
        notifyItemInserted(0)
    }

    private fun remove(p1: Int){
        items.removeAt(p1)
        notifyItemRemoved(p1)
    }
}
