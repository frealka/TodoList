package com.example.agata.todolist.recycler

import android.app.Application
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.agata.todolist.MainActivity
import com.example.agata.todolist.database.AppDatabase
import com.example.agata.todolist.database.DbWorker
import android.util.Log

class RecyclerViewAdapter(
    var items : MutableList<CardItem>,
    private val context: Context
) :
    RecyclerView.Adapter<ItemCardViewHolder>() {

    private var filteredItems : MutableList<CardItem> = mutableListOf()
    private var filtered : Boolean = false

    init {
        loadData(context.applicationContext as Application)
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
            val mDbWorker = DbWorker("dbWorkerThread")
            mDbWorker.start()
            val mDb = AppDatabase.getInstance(context.applicationContext)
            val toDelete = items[p1]
            val task = Runnable {
                Log.d("DEBUG", mDb.todoItemsDAO().getAllItems().size.toString())
                Log.d("REMOVED ID", toDelete.id.toString())
                mDb.todoItemsDAO().delete(toDelete)
            }

            mDbWorker.postTask(task)
            remove(toDelete)
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
            filteredItems = ArrayList(filteredItems.sortedWith(compareBy {it.getDeadlineTime()})).toMutableList()
        } else {
            items = ArrayList(items.sortedWith(compareBy {it.getDeadlineTime()})).toMutableList()
        }
        notifyDataSetChanged()
    }

    fun sortDescending(){
        if(filtered){
            filteredItems = ArrayList(filteredItems.sortedWith(compareBy {it.getDeadlineTime()}).reversed()).toMutableList()
        } else {
            items = ArrayList(items.sortedWith(compareBy {it.getDeadlineTime()}).reversed()).toMutableList()
        }
        notifyDataSetChanged()
    }

    private fun remove(elem: CardItem){
        items.remove(elem)
        filteredItems.remove(elem)
        notifyDataSetChanged()
    }

    private fun loadData(app: Application){
        val db : AppDatabase = AppDatabase.getInstance(app)
        val worker = DbWorker("ThreadWorker")
        worker.start()
        items = db.todoItemsDAO().getAllItems()
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

    fun setSearchedItems(newItems : MutableList<CardItem>){
        items = newItems
        notifyDataSetChanged()
    }
}
