package com.example.agata.todolist

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.util.Log
import com.example.agata.todolist.database.AppDatabase
import com.example.agata.todolist.database.DbWorker
import com.example.agata.todolist.recycler.RecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var todoAdapter : RecyclerViewAdapter
    private var mDb : AppDatabase? = null

    companion object {
        lateinit var mDbWorker: DbWorker
        val mUiHandler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDbWorker = DbWorker("dbWorkerThread")
        mDbWorker.start()
        mDb = this.let { AppDatabase.getInstance(it) }
    }

    override fun onResume() {
        super.onResume()
        val task = Runnable {
            val items = mDb?.todoItemsDAO()?.getAllItems() ?: mutableListOf()
            todoAdapter = RecyclerViewAdapter(items, this@MainActivity)
            mUiHandler.post{
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = todoAdapter
                }
            }
        }
        mDbWorker.postTask(task)
    }

    fun addItem(v: View){
        val newIntent = Intent(this, AddItemActivity::class.java)
        startActivity(newIntent)
    }

    fun sortBy(v: View){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sort TODOlist by...")
        builder.setPositiveButton("Deadline in ascending order"){ _, _ ->
            Log.d("SORT", "sort ascending")
            todoAdapter.sortAscending()
        }
        builder.setNegativeButton("Deadline in descending order"){ _, _ ->
            Log.d("SORT", "sort descending")
            todoAdapter.sortDescending()
        }
        builder.show()
    }

    fun filterList(v: View){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Filter by...")
            .setItems(R.array.elemTypes
            ) { _, which ->
                when(which){
                    0 -> todoAdapter.filterItems(R.drawable.work_icon)
                    1 -> todoAdapter.filterItems(R.drawable.home_icon)
                    2 -> todoAdapter.filterItems(R.drawable.computer_icon)
                    else -> todoAdapter.filterItems(-1)
                }
            }
        builder.create()
        builder.show()
    }
}
