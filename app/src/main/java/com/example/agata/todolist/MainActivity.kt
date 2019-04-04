package com.example.agata.todolist

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var todoAdapter : RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoAdapter = RecyclerViewAdapter(this@MainActivity)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoAdapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList("todoList", todoAdapter.items)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        todoAdapter.items = savedInstanceState?.getParcelableArrayList("todoList") ?: arrayListOf()
    }


    fun addItem(v: View){
        val newIntent = Intent(this, AddItemActivity::class.java)
        startActivityForResult(newIntent, addItemRequestCode)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == addItemRequestCode){
            if (resultCode == Activity.RESULT_OK){
                Log.d(TAG, "otrzymałem informację o kodzie $addItemRequestCode")
                // create recycler item
                val item = CardItem(
                    data!!.getStringExtra("task"),
                    data.getStringExtra("content"),
                    data.getStringExtra("deadline"),
                    data.getStringExtra("priority")
                )
                todoAdapter.insert(item)
            }
        }
    }

    companion object {
        const val addItemRequestCode: Int = 134
        const val TAG: String = "MainActivity"
    }
}
