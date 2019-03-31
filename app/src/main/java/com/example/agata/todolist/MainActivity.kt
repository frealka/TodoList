package com.example.agata.todolist

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val todoList : ArrayList<CardItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoList.add(CardItem("XXX", "YYY", "12.05.2020", "BBB"))
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.apply {
            adapter = RecyclerViewAdapter(todoList, context)
        }
    }


    fun addItem(view: View){
        val newIntent = Intent(this, AddItemActivity::class.java)
        startActivityForResult(newIntent, addItemRequestCode)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == addItemRequestCode){
            if (resultCode == Activity.RESULT_OK){
                Log.d(TAG, "otrzymałem informację o kodzie $addItemRequestCode")
                // create recycler item

            }
        }
    }

    companion object {
        const val addItemRequestCode: Int = 134
        const val TAG: String = "MainActivity"
    }
}
