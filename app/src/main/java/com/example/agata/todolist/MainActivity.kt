package com.example.agata.todolist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Type

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
                    data.getIntExtra("priority", R.color.colorAccent),
                    data.getIntExtra("type", R.drawable.home_icon)
                )
                todoAdapter.insert(item)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    private fun saveData(){
        val sharedPref : SharedPreferences = getSharedPreferences("appData", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref.edit()
        val gson = Gson()
        val json: String = gson.toJson(todoAdapter.items)
        editor.putString("cardItemString", json)
        editor.apply()
    }

    companion object {
        const val addItemRequestCode: Int = 134
        const val TAG: String = "MainActivity"
    }
}
