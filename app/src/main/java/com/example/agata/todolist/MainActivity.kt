package com.example.agata.todolist

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val todoList : ArrayList<CardItem> = arrayListOf()

    private lateinit var todoAdapter : RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoList.add(CardItem("XXX", "YYY", "12.05.2020", "BBB"))
        todoAdapter = RecyclerViewAdapter(todoList, this@MainActivity)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoAdapter
        }
    }


    fun addItem(v: View){
        val newIntent = Intent(this, AddItemActivity::class.java)
        startActivityForResult(newIntent, addItemRequestCode)

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

                Log.d(TAG, todoAdapter.toString())
                Log.d(TAG, recyclerView.adapter.toString())
            }
        }
    }

    companion object {
        const val addItemRequestCode: Int = 134
        const val TAG: String = "MainActivity"
    }
}
