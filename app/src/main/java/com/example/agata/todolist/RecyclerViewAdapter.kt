package com.example.agata.todolist

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecyclerViewAdapter(val context: Context) :
    RecyclerView.Adapter<ItemCardViewHolder>() {

    var items : ArrayList<CardItem> = arrayListOf()
    var filteredItems : ArrayList<CardItem> = arrayListOf()
    var filtered : Boolean = false

    init {
        loadData()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemCardViewHolder {
        return ItemCardViewHolder(LayoutInflater.from(p0.context), p0)
    }

    override fun onBindViewHolder(p0: ItemCardViewHolder, p1: Int) {
        if(filtered){
            p0.bind(context, filteredItems[p1])
        }else{
            p0.bind(context, items[p1])
        }
        p0.itemView.setOnLongClickListener{
            remove(items[p1])
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        if(filtered){
            return filteredItems.size
        }
        return items.size
    }

    fun sortAscending(){
        if(filtered){
            filteredItems = ArrayList(filteredItems.sortedWith(compareBy {it.deadlineTime}))
        } else {
            items = ArrayList(items.sortedWith(compareBy {it.deadlineTime}))
        }
        notifyDataSetChanged()
    }

    fun sortDescending(){
        if(filtered){
            filteredItems = ArrayList(filteredItems.sortedWith(compareBy {it.deadlineTime}).reversed())
        } else {
            items = ArrayList(items.sortedWith(compareBy {it.deadlineTime}).reversed())
        }
        notifyDataSetChanged()
    }

    fun insert(item: CardItem){
        filtered = false
        items.add(0, item)
        notifyItemInserted(0)
    }

    private fun remove(elem: CardItem){
        items.remove(elem)
        filteredItems.remove(elem)
        notifyDataSetChanged()
    }

    private fun loadData(){
        val sharedPref : SharedPreferences = context.getSharedPreferences("appData", Context.MODE_PRIVATE)
        val gson = Gson()
        val json : String? = sharedPref.getString("cardItemString", null)
        items = gson.fromJson<ArrayList<CardItem>>(json, object : TypeToken<ArrayList<CardItem>>() {}.type) ?: arrayListOf()
        notifyDataSetChanged()
    }

    fun filterItems(imgId: Int){
        if(imgId == -1){
            filtered = false
            notifyDataSetChanged()
            return
        }
        filteredItems = arrayListOf()
        filtered = true
        for (i in items){
            if(i.type == imgId){
                filteredItems.add(i)
            }
        }
        notifyDataSetChanged()
    }
}
