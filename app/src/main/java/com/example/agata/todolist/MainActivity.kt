package com.example.agata.todolist

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun addItem(view: View){
        val newIntent = Intent(this, AddItemActivity::class.java)
        startActivityForResult(newIntent, addItemRequestCode)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if((requestCode == addItemRequestCode) and (resultCode == Activity.RESULT_OK)){
            Log.d(TAG, "otrzymałem informację o kodzie $addItemRequestCode")
        }
    }

    companion object {
        const val addItemRequestCode: Int = 134
        const val TAG: String = "MainActivity"
    }
}
